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
public class CreateCourseHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

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
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
        }

        JsonObject params = new JsonParser().parse(json).getAsJsonObject();
        String coursename = params.get("coursename").getAsString();
        try {
            Statement stmt = MySQLConnection.connect().createStatement();
            String sqlQuery =
                    "SELECT * FROM user " +
                            "WHERE email='" + user.getEmail() + "';";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                int id = rs.getInt("id");
                sqlQuery = "INSERT INTO course(title) " + "VALUES('" + coursename + "');";
                stmt.executeUpdate(sqlQuery);

                sqlQuery = "SELECT LAST_INSERT_ID();";
                rs = stmt.executeQuery(sqlQuery);
                if (rs.next()) {
                    int course_id = Integer.parseInt(rs.getString(1));
                    sqlQuery = "INSERT INTO teaching_course " +
                            "VALUES(" + id + ", " + course_id + ");";
                    stmt.executeUpdate(sqlQuery);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.ok().build();
    }
}
