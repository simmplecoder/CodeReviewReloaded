package dreamteam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import model.Assignment;
import representations.AssignmentsRequestFormat;

@Path("/assignments")
public class AssignmentsDao {
	
	public ArrayList<Assignment> fetchAssignments() {
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		System.out.println("helloWorld");
		try {
			Statement stmt = Amsterdam.getConn().createStatement();
			String sqlQuery = "select * from code_review.assignment;";
			ResultSet rs = stmt.executeQuery(sqlQuery);
			
			while(rs.next()) {
				String id = Integer.toString(rs.getInt("id"));
				String title = rs.getString("title");
				String description = rs.getString("description"); 
				Assignment assignment = new Assignment(id,title,description);
				assignments.add(assignment);
			}
		} catch (Exception e) {
			System.out.println("my v derme");
			e.printStackTrace();
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
		
		ArrayList<Assignment> fetchedAssignments = fetchAssignments();
        AssignmentsRequestFormat assignmentsrequest = new Gson().fromJson(json, AssignmentsRequestFormat.class);

        String courses = new Gson().toJson(fetchedAssignments);
        
        return Response.ok(courses, MediaType.APPLICATION_JSON_TYPE).build();
	}
}
