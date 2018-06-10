package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("")
public class LoginHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private static final Logger logger = LogManager.getLogger();

    SessionFactory factory;

    public LoginHandler() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(String json) throws SQLException {
        Response redirection = new RedirectionHandler(request).redirection(false);
        if (redirection != null) { return redirection; }

        Response.ResponseBuilder builder;
        HttpSession session = request.getSession();

        LoginAttempt attempt = new Gson().fromJson(json, LoginAttempt.class);
        System.out.println("LoginAttemp: " + attempt);

        Session hsession = factory.getCurrentSession();

        hsession.beginTransaction();
        List<User> list = hsession.createQuery("from User u where u.email=:email and u.password=:password").
                setParameter("email", attempt.email).
                setParameter("password", attempt.password).
                list();
        hsession.getTransaction().commit();

        if (list.size() > 0) {
            session.setAttribute("user", list.get(0));
            builder = Response.ok("home.jsp", MediaType.TEXT_PLAIN);

            logger.info("User" + " " + list.get(0) + " " + "successfully logged in!");
        } else {
            builder = Response.status(Response.Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) { return redirection; }
        request.getSession().removeAttribute("user");
        return Response.ok("index.jsp", MediaType.TEXT_PLAIN).build();
    }
}
