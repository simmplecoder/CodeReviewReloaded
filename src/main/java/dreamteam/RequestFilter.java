package dreamteam;

import auth.PasswordKeeper;
import com.google.gson.Gson;
import model.Assignment;
import model.Course;
import representations.AssignmentsRequestFormat;
import representations.LoginAttempt;
import representations.RegisterAttempt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Path("")
public class RequestFilter {
    @Context HttpServletRequest request;
    private PasswordKeeper keeper;

    RequestFilter(PasswordKeeper keeper) {
        this.keeper = keeper;
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(String json) {
        Response redirection = redirection("login");
        if (redirection != null) {
            return redirection;
        }

        HttpSession session = request.getSession();
        LoginAttempt loginAttempt = new Gson().fromJson(json, LoginAttempt.class);
        String username = loginAttempt.username;
        String password = loginAttempt.password;
        Response.ResponseBuilder builder;
        if (!keeper.exists(username, password)) {
            builder = Response.status(Response.Status.UNAUTHORIZED);
        } else {
            session.setAttribute("username", username);
            builder = Response.ok("home.jsp", MediaType.TEXT_PLAIN_TYPE);
        }
        return builder.build();
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout(String json) {
        Response redirection = redirection("logout");
        if (redirection != null) {
            return redirection;
        }

        request.getSession().removeAttribute("username");
        return Response.ok("index.jsp", MediaType.TEXT_PLAIN_TYPE).build();
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(String json) {
        Response redirection = redirection("register");
        if (redirection != null) {
            return redirection;
        }

        HttpSession session = request.getSession();
        RegisterAttempt registerAttempt = new Gson().fromJson(json, RegisterAttempt.class);
        if (registerAttempt.email == null || registerAttempt.firstname == null
                || registerAttempt.lastname == null || registerAttempt.password == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (keeper.register(registerAttempt.email, registerAttempt.password)) {
            session.setAttribute("username", registerAttempt.email);
            return Response.ok("home.jsp", MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode(),
                "The user with the email already exists").build();
    }

    private Response redirection(String service) {
        URI uri = null;
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            if (!service.matches("login|register")) {
                try {
                    uri = new URI("/index.jsp");
                } catch (URISyntaxException e) {
                    System.out.println(e.getMessage());
                }
                return Response.temporaryRedirect(uri).build();
            }
        } else {
            if (service.matches("login|register")) {
                try {
                    uri = new URI("/home.jsp");
                } catch (URISyntaxException e) {
                    System.out.println(e.getMessage());
                }
                return Response.temporaryRedirect(uri).build();
            }
        }
        return null;
    }

    private Response courses(String json) {
        ArrayList<Course> fetchedCourses = new ArrayList<>();

        try {
            Statement stmt = Amsterdam.getConn().createStatement();
            String sqlQuery = "select * from code_review.course;";
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while(rs.next()) {
                String id = Integer.toString(rs.getInt("id"));
                String title = rs.getString("title");
                Course course = new Course(id, title);
                fetchedCourses.add(course);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (fetchedCourses.size() == 0) {
            fetchedCourses = null;
        }

        String courses = new Gson().toJson(fetchedCourses);
        return Response.ok(courses).build();
    }

    private Response assignments(String json) {
        AssignmentsRequestFormat assignmentsRequest = new Gson().fromJson(json, AssignmentsRequestFormat.class);
        ArrayList<Assignment> fetchedAssignments = new ArrayList<>();
        try {
            Statement stmt = Amsterdam.getConn().createStatement();
            String sqlQuery = "select * from code_review.assignment where course_id=" +
                    assignmentsRequest.id + ";";
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while(rs.next()) {
                String id = Integer.toString(rs.getInt("id"));
                String title = rs.getString("title");
                String description = rs.getString("description");
                Assignment assignment = new Assignment(id,title,description);
                fetchedAssignments.add(assignment);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (fetchedAssignments.size() == 0) {
            fetchedAssignments = null;
        }

        String courses = new Gson().toJson(fetchedAssignments);
        return Response.ok(courses, MediaType.APPLICATION_JSON_TYPE).build();
    }
}