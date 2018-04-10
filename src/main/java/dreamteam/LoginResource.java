package dreamteam;

import auth.PasswordKeeper;
import com.google.gson.Gson;
import representations.LoginAttempt;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.regex.Pattern;

@Path("/login")
public class LoginResource {
    private static final Pattern pattern = Pattern.compile(":\"\\S+\"");
    private PasswordKeeper keeper;

    public LoginResource(PasswordKeeper keeper)
    {
        this.keeper = keeper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getIt(String json) {
        Response.ResponseBuilder builder;

        LoginAttempt loginAttempt = new Gson().fromJson(json, LoginAttempt.class);

        String username = loginAttempt.username;
        String password = loginAttempt.password;

        if (!keeper.exists(username, password)) {
            builder = Response.status(Response.Status.UNAUTHORIZED);
        } else {
            builder = Response.ok("home.jsp", MediaType.TEXT_PLAIN_TYPE);
        }

        return builder.build();
    }
}
