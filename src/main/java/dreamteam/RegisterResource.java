package dreamteam;

import com.google.gson.Gson;
import representations.RegisterAttempt;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("register")
public class RegisterResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(String json)
    {
        RegisterAttempt registerAttempt = new Gson().fromJson(json, RegisterAttempt.class);

        System.out.println(registerAttempt.firstname + " " + registerAttempt.lastname);
        System.out.println(registerAttempt.username + " " + registerAttempt.password);

        return Response.ok().build();
    }
}
