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
import org.hibernate.query.Query;

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
public class GrantPrivilegeHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private static final Logger logger = LogManager.getLogger(GrantPrivilegeHandler.class);

    SessionFactory factory;

    public GrantPrivilegeHandler() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
    }

    @POST
    @Path("grant_privilege")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response grantPrivilege(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) { return redirection; }

        JsonObject params = new JsonParser().parse(json).getAsJsonObject();
        int user_id = params.get("user_id").getAsInt();
        int isInstructor = params.get("instructor").getAsInt();

        User user = null;
        try (Session hibernate = factory.getCurrentSession()) {
            hibernate.beginTransaction();
            List<User> users = hibernate.createQuery("select u from User u where u.id = :id")
                    .setParameter("id", user_id)
                    .list();
            user = users.get(0);
            user.setIsInstructor(isInstructor);
            hibernate.getTransaction().commit();
            if (isInstructor == 1) {
                logger.info("Granted instructor status to user: " + user);
            } else {
                logger.info("Removed instructor status from user: " + user);
            }

        } catch (Exception e) {
            logger.error("Failed to change status of user");
            logger.error("Exception message: " + e);
        }

        return Response.ok(new Gson().toJson(user), MediaType.APPLICATION_JSON_TYPE).build();
    }
}



