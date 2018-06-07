package services;

import com.google.gson.Gson;
import model.Assignment;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import representations.LoginAttempt;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("")
public class CreateAssignmentHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    SessionFactory factory;

    public CreateAssignmentHandler() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
    }

    @POST
    @Path("createassignment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAssignment(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) {
            return redirection;
        }
        if (request.getSession().getAttribute("isInstructor") == null
                || request.getSession().getAttribute("isInstructor").equals(0)) {
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
        }

        JsonObject params = new JsonParser().parse(json).getAsJsonObject();
        String title = params.get("title").getAsString();
        String desc = params.get("description").getAsString();
        int course_id = params.get("course_id").getAsInt();

        Assignment assignment = new Assignment(title, desc, course_id);

        System.out.println(assignment);

        try {
            Statement stmt = MySQLConnection.connect().createStatement();
            String sqlQuery = "INSERT INTO assignment " +
                        "values(null, \""+title+"\", \""+desc+"\", "+course_id+");";
            System.out.println(sqlQuery);

            stmt.executeUpdate(sqlQuery);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Response.ok().build();
    }
}
