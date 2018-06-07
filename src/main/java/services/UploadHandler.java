package services;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import commented_code.Comment;
import commented_code.CommentedCode;
import commented_code.Submission;
import org.bson.Document;
import org.bson.conversions.Bson;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.*;

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

        JSONObject submission = new JSONObject(json);
        JSONArray files = submission.getJSONArray("files");
        int assignment_id = submission.getInt("assignment_id");

        MongoDatabase db = mongoClient.getDatabase("CodeReviewTool");
        MongoCollection<Document> coll = db.getCollection("submissions");
        MongoCollection<Document> coll2 = db.getCollection("commented_code");


        Submission new_submission = new Submission();
        new_submission.setAssignmentId(assignment_id);

        HttpSession session = request.getSession();
        new_submission.setEmail(session.getAttribute("email").toString());
        Date date = new Date();
        new_submission.setTitle("by " + new_submission.getEmail() + " on " + date.toString());

        Document submission_doc = Document.parse(new Gson().toJson(new_submission));
        coll.insertOne(submission_doc);

        String submission_id = submission_doc.get("_id").toString();

        for (int i = 0; i < files.length(); i++) {
            JSONObject file = files.getJSONObject(i);
            CommentedCode commentedCode = new CommentedCode(file.getString("content"));
            commentedCode.setSubmissionId(submission_id);
            commentedCode.setName(file.getString("filename"));
            Document commented_code_doc = Document.parse(new Gson().toJson(commentedCode));
            coll2.insertOne(commented_code_doc);
        }

        // a new try.s
//        MongoDatabase db = mongoClient.getDatabase("CodeReviewTool");
//        MongoCollection<Document> coll = db.getCollection("commented_code");
//
//        System.out.println("request submission id is " + rf.submission_id);
//
//        Bson bsonFilter = Filters.eq("submissionId", rf.submission_id);
//        FindIterable<Document> findIt = coll.find(bsonFilter);
//        MongoCursor<Document> cursor = findIt.iterator();
//
//        List<CommentedCode> files = new ArrayList<>();
//        while (cursor.hasNext()) {
//            Document temp = cursor.next();
//            CommentedCode file = new Gson().fromJson(temp.toJson(), commented_code.CommentedCode.class);
////            System.out.println("submission id of file " + file.getSubmissionId());
//            file.setId(temp.get("_id").toString());
//            files.add(file);
//        }

        return Response.ok(new Gson().toJson("[]"), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
