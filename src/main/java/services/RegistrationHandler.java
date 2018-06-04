package services;

import com.google.gson.Gson;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

@Path("")
public class RegistrationHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    SessionFactory factory;

    public RegistrationHandler() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
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

        HttpSession session = request.getSession();
        User user = new Gson().fromJson(json, User.class);

        System.out.println(user);

        try {
            Session hsession = factory.getCurrentSession();
            hsession.beginTransaction();
            hsession.save(user);
            hsession.getTransaction().commit();

            session.setAttribute("email", user.getEmail());
            session.setAttribute("isInstructor", 0);

            return Response.ok("home.jsp", MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
    }
}
