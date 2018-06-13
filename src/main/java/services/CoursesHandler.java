package services;

import com.google.gson.Gson;
import model.Course;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import representations.CoursesRequestFormat;

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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Path("")
public class CoursesHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private static final Logger logger = LogManager.getLogger(CoursesHandler.class);

    @POST
    @Path("courses")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourses(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) {
            return redirection;
        }

        ArrayList<Course> courses = new ArrayList<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        CoursesRequestFormat requested = new Gson().fromJson(json, CoursesRequestFormat.class);

        logger.info("Requested parameters: " + requested);

        if (user != null && user.getIsInstructor() == 1) {
            logger.info("Retrieving instructor teaching course for " + user);
            try {
                Statement stmt = MySQLConnection.connect().createStatement();
                String sqlQuery =
                        "SELECT C.* " + "FROM teaching_course AS UC " + "INNER JOIN user AS U " +
                                "ON UC.user_id = U.id " + "INNER JOIN course AS C " + "ON UC.course_id = C.id " +
                                "WHERE U.email = \"" + user.getEmail() + "\";";
                ResultSet rs = stmt.executeQuery(sqlQuery);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    courses.add(new Course(id, title));
                }

                logger.info("Successfully retrieved courses for instructor: " + user);
            } catch (Exception e) {
                logger.error("Course retrieval error: " + e);
            }
        } else {
            logger.info("Retrieving courses for student: " + user);

            String sqlQuery = "";
            if (requested.registered == true) {
                sqlQuery = "select * from enrolled_course as ec, course as c  where ec.course_id = c.id and ec.user_id = " + user.getId() + ";";
                logger.info("Retrieving enrolled courses.");
            } else {
                sqlQuery = "select * from course where id not in (select ec.course_id from enrolled_course as ec, user as u where u.email = \"" +
                    user.getEmail() + "\" and u.id = ec.user_id);";
                logger.info("Retrieving unregistered courses.");
            }

            try {
                Statement stmt = MySQLConnection.connect().createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    courses.add(new Course(id, title));
                }

                logger.info("Successfully retrieved courses for student: " + user);
            } catch (Exception e) {
                logger.error("Course retrieval error: " + e);
            }
        }

        return Response.ok(new Gson().toJson(courses), MediaType.APPLICATION_JSON_TYPE).build();
    }
}