package services;

import com.google.gson.Gson;
import model.User;
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
import java.util.Date;
import java.util.List;

@Path("")
public class LoginHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

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

        LoginAttempt loginAttempt = new Gson().fromJson(json, LoginAttempt.class);
        String email = loginAttempt.username;
        String password = loginAttempt.password;

        Session hsession = factory.getCurrentSession();

        hsession.beginTransaction();
        List<User> list = hsession.createQuery("from User u where u.email='" + email + "' and u.password='" + password + "'").list();

        if (list.size() > 0) {
            session.setAttribute("user", list.get(0));
            builder = Response.ok("home.jsp", MediaType.TEXT_PLAIN);
        } else {
            builder = Response.status(Response.Status.UNAUTHORIZED);
        }

        hsession.getTransaction().commit();
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
