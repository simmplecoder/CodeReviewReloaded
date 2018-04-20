package dreamteam;

import com.google.gson.Gson;
import com.jcraft.jsch.JSchException;
import datagatherer.Conn;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Path("")
public class RequestFilter {
    @Context HttpServletRequest request;
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
        String sql =
                "select * from code_review.user where username=\'" + username +
                "\' and password=\'" + password + "\';";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            session.setAttribute("isInstructor", rs.getInt("isInstructor"));
            session.setAttribute("username", username);
        }

        if (session.getAttribute("username") == null) {
            builder = Response.status(Response.Status.UNAUTHORIZED);
        } else {
            builder = Response.ok("home.jsp", MediaType.TEXT_PLAIN);
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
        String email = registerAttempt.email;
        String password = registerAttempt.password;
        if (registerAttempt.email == null || registerAttempt.firstname == null
                || registerAttempt.lastname == null || registerAttempt.password == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Statement stmt = Conn.getConnection().createStatement();
        String sql = "insert into code_review.user(username, password, isInstructor)" +
                "values(\'" + email + "\', \'" + password + "\', \'" + 0 + "\')";
        try {
            stmt.executeUpdate(sql);
            session.setAttribute("username", email);
            session.setAttribute("isInstructor", 0);
            return Response.ok("home.jsp", MediaType.TEXT_PLAIN).build();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return Response.status(Response.Status.CONFLICT.getStatusCode(),
                        "The user with the email already exists").build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
    }

    @POST
    @Path("courses")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourses(String json) {

        return null;
    }

    @POST
    @Path("assignments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssignments(String json) {

        return null;
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
}