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

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        if (redirection != null) {
            return redirection;
        }

        Response.ResponseBuilder builder;
        HttpSession session = request.getSession();

        LoginAttempt loginAttempt = new Gson().fromJson(json, LoginAttempt.class);
        String email = loginAttempt.username;
        String password = loginAttempt.password;

        Session hsession = factory.getCurrentSession();

        hsession.beginTransaction();
        List<User> list = hsession.createQuery("from User u where u.email='" + email + "' and u.password='" + password + "'").list();

        if (list.size() > 0) {
            session.setAttribute("isInstructor", list.get(0).getIsInstructor());
            session.setAttribute("email", email);
            System.out.println("Session user id " + list.get(0).getId());
            session.setAttribute("user_id", list.get(0).getId());
            session.setAttribute("first_name", list.get(0).getFirst_name());
            session.setAttribute("last_name", list.get(0).getLast_name());

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
        if (redirection != null) {
            return redirection;
        }
        LogManager.addLog(request.getSession().getAttribute("email")
                        + " successfully logged out.",
                dateFormat.format(new Date()), "login", context);
        request.getSession().removeAttribute("email");
        request.getSession().removeAttribute("isInstructor");
        return Response.ok("index.jsp", MediaType.TEXT_PLAIN).build();
    }
}
