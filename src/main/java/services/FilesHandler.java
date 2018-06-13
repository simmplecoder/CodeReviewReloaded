package services;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import com.mongodb.util.JSON;
import commented_code.Comment;
import commented_code.CommentedCode;
import commented_code.FileTemp;
import commented_code.Submission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import representations.FilesRequestFormat;
import representations.SubmissionsRequestFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Path("")
public class FilesHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private MongoClient mongoClient = null;

    private static final Logger logger = LogManager.getLogger(FilesHandler.class);

    public FilesHandler() { mongoClient = new MongoClient(); }

    @POST
    @Path("files")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssignments(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) {
            return redirection;
        }

        FilesRequestFormat rf = new Gson().fromJson(json, FilesRequestFormat.class);

        MongoDatabase db = mongoClient.getDatabase("CodeReviewTool");
        MongoCollection<Document> coll = db.getCollection("commented_code");

        logger.info("Requested file belongs to submission with id: " + rf.submission_id);

        Bson bsonFilter = Filters.eq("submissionId", rf.submission_id);
        FindIterable<Document> findIt = coll.find(bsonFilter);
        MongoCursor<Document> cursor = findIt.iterator();

        List<CommentedCode> files = new ArrayList<>();
        while (cursor.hasNext()) {
            Document temp = cursor.next();
            CommentedCode file = new Gson().fromJson(temp.toJson(), commented_code.CommentedCode.class);
            file.setId(temp.get("_id").toString());
            files.add(file);
            logger.info("Related file to requested submission: " + file);
        }
        return Response.ok(new Gson().toJson(files), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
