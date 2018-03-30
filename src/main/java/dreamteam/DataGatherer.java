package dreamteam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.jcraft.jsch.JSchException;

import model.Student;
import representations.AssignmentsRequestFormat;
import representations.CoursesRequestFormat;
import model.Instructor;
import model.Course;
import model.Assignment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Path("/courses")
public class DataGatherer {
	
	Connection conn;
	
	public DataGatherer() {
		try {
			conn = Dao.getConnection();
			System.out.println("conn is " + conn);
			System.out.println("DataGatherer: Connection has been successfully established.");
		} catch (ClassNotFoundException | JSchException | SQLException e) {
//			e.printStackTrace();
		}
	}
	
	public ArrayList<Student> getStudents() {
		return null;
	}
	
	public ArrayList<Instructor> getInstructors() {
		return null;
	}
	
	public ArrayList<Course> getStudentsCourses(Student student) {
		return null;
	}

	public ArrayList<Assignment> getStudentsAssignments(Student student) {
		return null;
	}
	
	// placeholder getters
	public ArrayList<Course> fetchCourses() {
		ArrayList<Course> courses = new ArrayList<Course>();

		try {
			
			Statement stmt = conn.createStatement();
			System.out.println("Checkpoint");
			String sqlQuery = "select * from code_review.course;";
			ResultSet rs = stmt.executeQuery(sqlQuery);
			
			System.out.println("resultset: " + rs);
			
			while(rs.next()) {
				String id = Integer.toString(rs.getInt("id"));
				String title = rs.getString("title");
				Course course = new Course(id,title);
				courses.add(course);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("My v zhope");
		}
		
		if(courses.size() == 0) {
			return null;
		} else {
			return courses;
		}
	}
	
	public ArrayList<Assignment> fetchAssignments() {
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		
		try {
			Statement stmt = conn.createStatement();
			String sqlQuery = "select * from code_review.assignments;";
			ResultSet rs = stmt.executeQuery(sqlQuery);
			
			while(rs.next()) {
				String id = Integer.toString(rs.getInt("id"));
				String title = rs.getString("title");
				String description = rs.getString("description"); 
				Assignment assignment = new Assignment(id,title,description);
				assignments.add(assignment);
			}
		} catch (Exception e) {
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
	public Response getCourses(String json) {
		System.out.println("got here anchik");
		
		ArrayList<Course> fetchedCourses = fetchCourses();
        CoursesRequestFormat coursesrequest = new Gson().fromJson(json, CoursesRequestFormat.class);

        String courses = new Gson().toJson(fetchedCourses);
        System.out.println(courses);
        
        return Response.ok(courses, MediaType.APPLICATION_JSON_TYPE).build();
	}

	DatabaseResource dbResource = new PlaceholderDatabaseResource();
}
