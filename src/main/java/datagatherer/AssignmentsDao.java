package datagatherer;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import model.Assignment;
import representations.AssignmentsRequestFormat;

@Path("/assignments")
public class AssignmentsDao {
	
	public ArrayList<Assignment> fetchAssignments(String course_id) {
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		try {
			Statement stmt = Conn.getConnection().createStatement();
			String sqlQuery = "select * from code_review.assignment where course_id="+course_id+";";
			ResultSet rs = stmt.executeQuery(sqlQuery);
			
			while(rs.next()) {
				String id = Integer.toString(rs.getInt("id"));
				String title = rs.getString("title");
				String description = rs.getString("description"); 
				Assignment assignment = new Assignment(id,title,description);
				assignments.add(assignment);
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		if(assignments.size() == 0) {
			return null;
		} else {
			return assignments;
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAssignments(String json) {
		
		
        AssignmentsRequestFormat assignmentsrequest = new Gson().fromJson(json, AssignmentsRequestFormat.class);
        ArrayList<Assignment> fetchedAssignments = fetchAssignments(assignmentsrequest.id);
        
        String courses = new Gson().toJson(fetchedAssignments);
        
        return Response.ok(courses, MediaType.APPLICATION_JSON_TYPE).build();
	}
}
