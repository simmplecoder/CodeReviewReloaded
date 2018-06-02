package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Assignment;
import model.Course;
import representations.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Path("")
public class RequestFilter {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RequestFilter() {
        Conn.connect();
    }

    private Response redirection(boolean needLogin) {
        URI uri = null;
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            if (needLogin) {
                try {
                    uri = new URI("/index.jsp");
                } catch (URISyntaxException e) {
                    System.out.println(e.getMessage());
                }
                return Response.temporaryRedirect(uri).build();
            }
        } else {
            if (!needLogin) {
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

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(String json) throws SQLException {
        Response redirection = redirection(false);
        if (redirection != null) {
            return redirection;
        }
        Response.ResponseBuilder builder;
        HttpSession session = request.getSession();
        LoginAttempt loginAttempt = new Gson().fromJson(json, LoginAttempt.class);
        String email = loginAttempt.username;
        String password = loginAttempt.password;

        Statement stmt = Conn.connect().createStatement();
        String sql = "select * from user where email=\'" + email +
                "\' and password=\'" + password + "\';";
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("email: " + email + ", password: " + password);

        if(rs.next()) {
            LogManager.addLog(email + " successfully logged in.",
                    dateFormat.format(new Date()), "login", context);
            session.setAttribute("isInstructor", rs.getInt("isInstructor"));
            session.setAttribute("username", email);
            builder = Response.ok("home.jsp", MediaType.TEXT_PLAIN);
        } else {
            LogManager.addLog(email + " tried to log in. HTTP 401 was returned.",
                    dateFormat.format(new Date()), "login", context);
            builder = Response.status(Response.Status.UNAUTHORIZED);
        }

        return builder.build();
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout(String json) {
        Response redirection = redirection(true);
        if (redirection != null) {
            return redirection;
        }
        LogManager.addLog(request.getSession().getAttribute("username")
                        + " successfully logged out.",
                dateFormat.format(new Date()), "login", context);
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("isInstructor");
        return Response.ok("index.jsp", MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(String json) throws SQLException, ClassNotFoundException {
        Response redirection = redirection(false);
        if (redirection != null) {
            return redirection;
        }
        HttpSession session = request.getSession();
        RegisterAttempt registerAttempt = new Gson().fromJson(json, RegisterAttempt.class);
        String username = registerAttempt.email;
        String password = registerAttempt.password;
        String firstname = registerAttempt.firstname;
        String lastname = registerAttempt.lastname;
        if (registerAttempt.email == null || registerAttempt.firstname == null
                || registerAttempt.lastname == null || registerAttempt.password == null) {
            LogManager.addLog("Some fields were null. HTTP 400 was returned.",
                    dateFormat.format(new Date()), "register", context);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Statement stmt = Conn.connect().createStatement();
        String sql = "insert into user(email, password, first_name, last_name, isInstructor)" +
                "values(\'" + username + "\', \'" + password + "\', \'" + firstname + "\', \'" + lastname + "\', \'" + 0 + "\')";
        try {
            stmt.executeUpdate(sql);
            LogManager.addLog(username + " was successfully registered.",
                    dateFormat.format(new Date()), "register", context);
            session.setAttribute("username", username);
            session.setAttribute("isInstructor", 0);
            return Response.ok("home.jsp", MediaType.TEXT_PLAIN).build();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                LogManager.addLog(username + " was taken. HTTP 409 was returned",
                        dateFormat.format(new Date()), "register", context);
                return Response.status(Response.Status.CONFLICT.getStatusCode(),
                        "The user with the email already exists").build();
            }
            LogManager.addLog("Insert to the database failed.",
                    dateFormat.format(new Date()), "register", context);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
    }

    @POST
    @Path("courses")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourses() {
        Response redirection = redirection(true);
        if (redirection != null) {
            return redirection;
        }
        ArrayList<Course> courses = new ArrayList<>();
        HttpSession session = request.getSession();
        try {
            Statement stmt = Conn.connect().createStatement();
            String sqlQuery =
                    "SELECT C.* " +
                    "FROM teaching_course AS UC " +
                    "INNER JOIN user AS U " +
                    "ON UC.user_id = U.id " +
                    "INNER JOIN course AS C " +
                    "ON UC.course_id = C.id " +
                    "WHERE U.email = \"" + session.getAttribute("username") + "\";";
            ResultSet rs = stmt.executeQuery(sqlQuery);



            while(rs.next()) {
                String id = Integer.toString(rs.getInt("id"));
                String title = rs.getString("title");
                Course course = new Course(id, title);
                courses.add(course);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Response.ok(new Gson().toJson(courses), MediaType.APPLICATION_JSON_TYPE).build();
    }
//
//    @POST
//    @Path("assignments")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAssignments(String json) {
//        Response redirection = redirection(true);
//        if (redirection != null) {
//            return redirection;
//        }
//        AssignmentsRequestFormat requestFormat = new Gson().fromJson(json, AssignmentsRequestFormat.class);
//        ArrayList<Assignment> assignments = new ArrayList<>();
//        try {
//            Statement stmt = Conn.getConnection().createStatement();
//            String sqlQuery =
//                    "select * " +
//                    "from code_review.assignment " +
//                    "where course_id="+requestFormat.id+";";
//            ResultSet rs = stmt.executeQuery(sqlQuery);
//
//            while(rs.next()) {
//                String id = Integer.toString(rs.getInt("id"));
//                String title = rs.getString("title");
//                String description = rs.getString("description");
//                Assignment assignment = new Assignment(id,title,description);
//                assignments.add(assignment);
//            }
//        } catch (JSchException | SQLException | ClassNotFoundException e) {
//            System.out.println(e.getMessage());
//        }
//        return Response.ok(new Gson().toJson(assignments), MediaType.APPLICATION_JSON_TYPE).build();
//    }
//
    @POST
    @Path("createcourse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCourse(String json) {
        Response redirection = redirection(true);
        if (redirection != null) {
            return redirection;
        }
        if (request.getSession().getAttribute("isInstructor") == null
                || request.getSession().getAttribute("isInstructor").equals(0)) {
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
        }
        JsonObject params = new JsonParser().parse(json).getAsJsonObject();
        String title = params.get("coursename").getAsString();
        String instructor = (String) request.getSession().getAttribute("username");
        try {
            Statement stmt = Conn.connect().createStatement();
            String sqlQuery =
                    "SELECT * FROM user " +
                    "WHERE email='" + instructor + "';";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                int id = rs.getInt("id");
                sqlQuery =
                        "INSERT INTO course(title) " +
                                "VALUES('" + title + "');";
                stmt.executeUpdate(sqlQuery);

                sqlQuery = "SELECT LAST_INSERT_ID();";
                rs = stmt.executeQuery(sqlQuery);
                if (rs.next()) {
                    int course_id = Integer.parseInt(rs.getString(1));
                    sqlQuery =
                            "INSERT INTO teaching_course(user_id, course_id)" +
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
//
//    @POST
//    @Path("createassignment")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createAssignment(String json) {
//        Response redirection = redirection(true);
//        if (redirection != null) {
//            return redirection;
//        }
//        if (request.getSession().getAttribute("isInstructor") == null
//                || request.getSession().getAttribute("isInstructor").equals(0)) {
//            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
//        }
//        JsonObject params = new JsonParser().parse(json).getAsJsonObject();
//        String description = params.get("description").getAsString();
//        String course_id = params.get("course_id").getAsString();
//        String title = params.get("title").getAsString();
//
//        try {
//            Statement stmt = Conn.getConnection().createStatement();
//            String sqlQuery =
//                    "SELECT * FROM code_review.user " +
//                            "WHERE username='" + request.getSession().getAttribute("username") + "';";
//            ResultSet rs = stmt.executeQuery(sqlQuery);
//            if (rs.next()) {
//                int id = rs.getInt("id");
//                sqlQuery =
//                        "INSERT INTO code_review.assignment(title, description, course_id, course_instructor_id) " +
//                        "VALUES('"+title+"', '"+description+"', "+course_id+", "+id+");";
//                stmt.executeUpdate(sqlQuery);
//            }
//        } catch (SQLException | JSchException | ClassNotFoundException e) {
//            System.out.println(e.getMessage());
//        }
//        return Response.ok().build();
//    }
}