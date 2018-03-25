package dreamteam;

import com.google.gson.Gson;
import representations.RegisterAttempt;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/register")
public class RegisterResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(String json)
    {
        RegisterAttempt registerAttempt = new Gson().fromJson(json, RegisterAttempt.class);

        if (registerAttempt.email == null || registerAttempt.firstname == null
                || registerAttempt.lastname == null || registerAttempt.password == null)
        {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        return Response.ok("home.html", MediaType.TEXT_PLAIN_TYPE).build();
    }
}
