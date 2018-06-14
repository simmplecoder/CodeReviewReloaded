package services;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import commented_code.Comment;
import commented_code.CommentedCode;
import commented_code.Submission;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.Date;

@Path("")
public class AddCommentHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private MongoClient mongoClient = null;

    private static final Logger logger = LogManager.getLogger(AddCommentHandler.class);

    public AddCommentHandler() {
        mongoClient = new MongoClient();
    }

    @POST
    @Path("addcomment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssignments(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null)
            return redirection;

        User user = (User) request.getSession().getAttribute("user");

        Comment comment = new Gson().fromJson(json, Comment.class);
        comment.setAuthor(user.getEmail());

        MongoDatabase db = mongoClient.getDatabase("CodeReviewTool");
        MongoCollection<Document> collectionOfCodes = db.getCollection("commented_code");

        Bson filter = Filters.eq("_id", new ObjectId(comment.getFile_id()));
        collectionOfCodes.updateOne(filter, new Document("$push", new Document("comments", Document.parse(new Gson().toJson(comment)))));

        logger.info("Added comment " + comment);

        return Response.ok(new Gson().toJson(comment), MediaType.APPLICATION_JSON_TYPE).build();
    }
}

