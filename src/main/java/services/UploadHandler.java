package services;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import commented_code.Comment;
import commented_code.CommentedCode;
import commented_code.Submission;
import model.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import representations.FilesRequestFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.*;

import static com.mongodb.client.model.Filters.eq;

@Path("")
public class UploadHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private MongoClient mongoClient = null;


    public UploadHandler() {
        mongoClient = new MongoClient();
    }

    @POST
    @Path("upload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssignments(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null)
            return redirection;

        User user = (User) request.getSession().getAttribute("user");

        JSONObject submission = new JSONObject(json);
        JSONArray files = submission.getJSONArray("files");
        int assignment_id = submission.getInt("assignment_id");

        MongoDatabase db = mongoClient.getDatabase("CodeReviewTool");
        MongoCollection<Document> collectionOfSubmissions = db.getCollection("submissions");
        MongoCollection<Document> collectionOfCodes = db.getCollection("commented_code");

        Submission new_submission = new Submission();
        new_submission.setAssignmentId(assignment_id);

        HttpSession session = request.getSession();
        new_submission.setEmail(user.getEmail());

        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        new_submission.setTitle(user.getFirst_name() + " " + user.getLast_name() + " on " + dateString);

        Document submission_doc = Document.parse(new Gson().toJson(new_submission));
        collectionOfSubmissions.insertOne(submission_doc);

        String submission_id = submission_doc.get("_id").toString();

        for (int i = 0; i < files.length(); i++) {
            JSONObject file = files.getJSONObject(i);
            CommentedCode commentedCode = new CommentedCode(file.getString("content"));
            commentedCode.setSubmissionId(submission_id);
            commentedCode.setName(file.getString("filename"));
            Document commented_code_doc = Document.parse(new Gson().toJson(commentedCode));
            collectionOfCodes.insertOne(commented_code_doc);
        }

        return Response.ok(new Gson().toJson("[]"), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
