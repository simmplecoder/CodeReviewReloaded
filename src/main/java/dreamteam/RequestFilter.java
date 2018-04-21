package dreamteam;

import datagatherer.Conn;
import com.google.gson.Gson;
import com.jcraft.jsch.JSchException;
import representations.LogRequestFormat;
import representations.LoginAttempt;
import representations.RegisterAttempt;

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
import java.util.Date;

@Path("")
public class RequestFilter {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Connection conn;

    RequestFilter() throws JSchException, SQLException, ClassNotFoundException {
        conn = Conn.getConnection();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(String json) throws SQLException {
        Response redirection = redirection("login");
        if (redirection != null) {
            return redirection;
        }

        Response.ResponseBuilder builder;
        HttpSession session = request.getSession();
        LoginAttempt loginAttempt = new Gson().fromJson(json, LoginAttempt.class);
        String username = loginAttempt.username;
        String password = loginAttempt.password;

        Statement stmt = conn.createStatement();
        String sql = "select * from code_review.user where username=\'" + username +
                "\' and password=\'" + password + "\';";
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            LogManager.addLog(username + " successfully logged in.",
                    dateFormat.format(new Date()), "login", context);
            session.setAttribute("isInstructor", rs.getInt("isInstructor"));
            session.setAttribute("username", username);
            builder = Response.ok("home.jsp", MediaType.TEXT_PLAIN);
        } else {
            LogManager.addLog(username + " tried to log in. HTTP 401 was returned.",
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
        Response redirection = redirection("logout");
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
    public Response register(String json) throws SQLException, ClassNotFoundException, JSchException {
        Response redirection = redirection("register");
        if (redirection != null) {
            return redirection;
        }

        HttpSession session = request.getSession();
        RegisterAttempt registerAttempt = new Gson().fromJson(json, RegisterAttempt.class);
        String username = registerAttempt.email;
        String password = registerAttempt.password;
        if (registerAttempt.email == null || registerAttempt.firstname == null
                || registerAttempt.lastname == null || registerAttempt.password == null) {
            LogManager.addLog("Some fields were null. HTTP 400 was returned.",
                    dateFormat.format(new Date()), "register", context);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Statement stmt = Conn.getConnection().createStatement();
        String sql = "insert into code_review.user(username, password, isInstructor)" +
                "values(\'" + username + "\', \'" + password + "\', \'" + 0 + "\')";
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
    @Path("loggingsearch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogs(String json) {
        LogRequestFormat log = new Gson().fromJson(json, LogRequestFormat.class);
        return Response.ok(LogManager.getLogs(log, context), MediaType.APPLICATION_JSON_TYPE).build();
    }

    @POST
    @Path("loggingison")
    @Produces(MediaType.TEXT_PLAIN)
    public Response loggingIsOn() {
        String status = LogManager.isOn(context) ? "YES" : "NO";
        return Response.ok(status, MediaType.TEXT_PLAIN_TYPE).build();
    }

    @POST
    @Path("loggingswitch")
    public Response switchLogStatus() {
        LogManager.setLogStatus(!LogManager.isOn(context), context);
        return Response.ok().build();
    }

//    @POST
//    @Path("courses")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getCourses(String json) {
//
//        return null;
//    }
//
//    @POST
//    @Path("assignments")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAssignments(String json) {
//
//        return null;
//    }

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
}