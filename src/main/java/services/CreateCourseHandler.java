package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.Statement;

@Path("")
public class CreateCourseHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private static final Logger logger = LogManager.getLogger(CreateCourseHandler.class);

    @POST
    @Path("createcourse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCourse(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null)
            return redirection;

        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getIsInstructor() == 0) {
            logger.error("User: " + user + " is not an instructor course creation is not possible.");
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
        }

        JsonObject params = new JsonParser().parse(json).getAsJsonObject();
        String title = params.get("coursename").getAsString();

        try {
            Statement stmt = MySQLConnection.connect().createStatement();
            stmt.executeUpdate("insert into course " + "values(null, '" + title + "');");

            ResultSet rs = stmt.executeQuery("select last_insert_id();");
            rs.next();

            int courseId = Integer.parseInt(rs.getString(1));

            logger.info("Successfully created a course with id" + courseId);

            stmt.executeUpdate("insert into teaching_course " + "VALUES(" + user.getId() + ", " + courseId + ");");
            logger.info(user.getEmail() + " teaches a course with id " + courseId);
        } catch (Exception e) {
            logger.error("Failed to create a course " + title);
            logger.error("The exception: " + e);
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.ok().build();
    }
}
