package services;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import commented_code.Submission;
import model.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
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

import static com.mongodb.client.model.Filters.and;

@Path("")
public class SubmissionsHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private MongoClient mongoClient = null;

    public SubmissionsHandler() {
        mongoClient = new MongoClient();
    }

    @POST
    @Path("submissions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssignments(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) { return redirection; }

        User user = (User) request.getSession().getAttribute("user");

        SubmissionsRequestFormat rf = new Gson().fromJson(json, SubmissionsRequestFormat.class);

        // a new try.s
        MongoDatabase db = mongoClient.getDatabase("CodeReviewTool");
        MongoCollection<Document> coll = db.getCollection("submissions");

//        System.out.println(rf.id);;
        Bson filter = Filters.eq("assignmentId", rf.id);
        if (user.getIsInstructor() == 0)
            filter = and(filter, Filters.eq("email", user.getEmail()));

        FindIterable<Document> findIt = coll.find(filter);

        MongoCursor<Document> cursor = findIt.iterator();

        List<Submission> submissions = new ArrayList<>();
        while (cursor.hasNext()) {
            Document temp = cursor.next();
            Submission submis = new Gson().fromJson(temp.toJson(), Submission.class);
            submis.setId(temp.get("_id").toString());
            submissions.add(submis);
            System.out.println("Submission " + submis);
        }

        return Response.ok(new Gson().toJson(submissions), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
