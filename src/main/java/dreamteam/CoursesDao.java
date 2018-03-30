package dreamteam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import model.Student;
import representations.AssignmentsRequestFormat;
import representations.CoursesRequestFormat;
import model.Instructor;
import model.Course;
import model.Assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

@Path("/courses")
public class CoursesDao {

	public ArrayList<Course> fetchCourses() {
		ArrayList<Course> courses = new ArrayList<Course>();

		try {			
			Statement stmt = Amsterdam.getConn().createStatement();
			String sqlQuery = "select * from code_review.course;";
			ResultSet rs = stmt.executeQuery(sqlQuery);
			
			while(rs.next()) {
				String id = Integer.toString(rs.getInt("id"));
				String title = rs.getString("title");
				Course course = new Course(id,title);
				courses.add(course);
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		if(courses.size() == 0) {
			return null;
		} else {
			return courses;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response getCourses(String json) {
		
		ArrayList<Course> fetchedCourses = fetchCourses();
        CoursesRequestFormat coursesrequest = new Gson().fromJson(json, CoursesRequestFormat.class);

        String courses = new Gson().toJson(fetchedCourses);
        
        return Response.ok(courses, MediaType.APPLICATION_JSON_TYPE).build();
	}
}
