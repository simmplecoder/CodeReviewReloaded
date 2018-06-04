package services;

import com.google.gson.Gson;
import model.Course;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Path("")
public class CoursesHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    @POST
    @Path("courses")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourses() {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) {
            return redirection;
        }
        ArrayList<Course> courses = new ArrayList<>();
        HttpSession session = request.getSession();

        try {
            Statement stmt = MySQLConnection.connect().createStatement();
            String sqlQuery =
                    "SELECT C.* " +
                            "FROM teaching_course AS UC " +
                            "INNER JOIN user AS U " +
                            "ON UC.user_id = U.id " +
                            "INNER JOIN course AS C " +
                            "ON UC.course_id = C.id " +
                            "WHERE U.email = \"" + session.getAttribute("email") + "\";";
            ResultSet rs = stmt.executeQuery(sqlQuery);


            while(rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                Course course = new Course(id, title);
                courses.add(course);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Response.ok(new Gson().toJson(courses), MediaType.APPLICATION_JSON_TYPE).build();
    }

}
