package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Course;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("")
public class UsersHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    SessionFactory factory;

    public UsersHandler() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).addAnnotatedClass(Course.class).buildSessionFactory();
    }

    @POST
    @Path("users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getusers(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) { return redirection; }

        JsonObject params = new JsonParser().parse(json).getAsJsonObject();
        int isInstructors = params.get("instructors").getAsInt();
        Session hsession = factory.getCurrentSession();
        hsession.beginTransaction();
        List<User> list = hsession.createQuery("from User").list();
        List<User> finallist = new ArrayList<>();
        hsession.getTransaction().commit();

        for (User u : list) {
            if (u.getIsInstructor() == isInstructors) {
                finallist.add(u);
            }
        }
        return Response.ok(new Gson().toJson(finallist), MediaType.APPLICATION_JSON_TYPE).build();
    }
}


