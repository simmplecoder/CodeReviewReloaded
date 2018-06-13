package services;

import com.google.gson.Gson;
import model.Assignment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import representations.AssignmentsRequestFormat;

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
import java.util.ArrayList;

@Path("")
public class AssignmentsHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private static final Logger logger = LogManager.getLogger(AssignmentsHandler.class);

    @POST
    @Path("assignments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssignments(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) {
            return redirection;
        }

        ArrayList<Assignment> assignments = new ArrayList<>();
        AssignmentsRequestFormat requestFormat = new Gson().fromJson(json, AssignmentsRequestFormat.class);
        int courseId = requestFormat.id;
        try {
            Statement stmt = MySQLConnection.connect().createStatement();
            String sqlQuery = "select * from assignment where course_id = " + courseId + ";";
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while(rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("desc");
                assignments.add(new Assignment(id, title, description, id));
            }
            logger.info("Retrieved assignments for course id " + courseId);
            logger.info("Assignments: " + assignments);
        } catch (Exception e) {
            logger.error("Could not retrieve assignments for course id " + courseId);
            logger.error("Exception message: " + e);
        }
        return Response.ok(new Gson().toJson(assignments), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
