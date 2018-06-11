package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;

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
public class CourseRegistrationHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    @POST
    @Path("registertocourse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCourse(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) {
            return redirection;
        }

        User user = (User) request.getSession().getAttribute("user");

        JsonObject params = new JsonParser().parse(json).getAsJsonObject();
        int course_id = params.get("course_id").getAsInt();

        try {
            Statement stmt = MySQLConnection.connect().createStatement();
            String sqlQuery = "INSERT INTO enrolled_course " + "VALUES(" + user.getId() + ", " + course_id + ");";
            stmt.executeUpdate(sqlQuery);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.ok().build();
    }
}
