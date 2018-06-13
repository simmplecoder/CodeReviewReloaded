package services;

import com.google.gson.Gson;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import representations.LoginAttempt;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("")
public class AuthentificationHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private static final Logger logger = LogManager.getLogger(AuthentificationHandler.class);

    SessionFactory factory;

    public AuthentificationHandler() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(String json) {
        Response redirection = new RedirectionHandler(request).redirection(false);
        if (redirection != null) {
            return redirection;
        }

        Response.ResponseBuilder builder;
        LoginAttempt attempt = new Gson().fromJson(json, LoginAttempt.class);

        Session hibernate = factory.getCurrentSession();

        hibernate.beginTransaction();
        List<User> list = (List<User>) hibernate.createQuery("from User u where u.email=:email and u.password=:password")
                .setParameter("email", attempt.email)
                .setParameter("password", attempt.password)
                .list();

        hibernate.getTransaction().commit();

        if (list.size() > 0) {
            User user = list.get(0);
            request.getSession().setAttribute("user", user);
            builder = Response.ok("home.jsp", MediaType.TEXT_PLAIN);

            logger.info("User:" + " " + user + " " + "successfully logged in!");
        } else {
            builder = Response.status(Response.Status.UNAUTHORIZED);

            logger.info("Could not authorize a user " + attempt);
        }
        return builder.build();
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(String json) throws SQLException, ClassNotFoundException {
        Response redirection = new RedirectionHandler(request).redirection(false);
        if (redirection != null) {
            return redirection;
        }

        User user = new Gson().fromJson(json, User.class);

        try {
            Session hibernate = factory.getCurrentSession();
            hibernate.beginTransaction();
            hibernate.save(user);
            hibernate.flush();
            hibernate.getTransaction().commit();

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            logger.info("A new user registered to the system " + user);

            return Response.ok("home.jsp", MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {

            logger.error("Could not save a new user: " + user);
            logger.error(e);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) {
            return redirection;
        }

        User user = (User) request.getSession().getAttribute("user");

        logger.info("A user: " + user + " successfully logged out!");

        request.getSession().removeAttribute("user");

        return Response.ok("index.jsp", MediaType.TEXT_PLAIN).build();
    }
}
