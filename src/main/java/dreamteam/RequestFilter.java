package dreamteam;

import auth.PasswordKeeper;
import com.google.gson.Gson;
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
        return Response.ok("index.jsp", MediaType.TEXT_PLAIN).build();
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
            return Response.ok("home.jsp", MediaType.TEXT_PLAIN).build();
        }
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode(),
                "The user with the email already exists").build();
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