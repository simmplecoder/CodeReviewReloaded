package dreamteam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
@Path("login")
public class LoginResource {
    private static final Map<String, String> users;
    private static final Pattern pattern = Pattern.compile(":\"\\S+\"");

    private class LoginAttempt {
        public String username;
        public String password;
    }

    static {
        users = new HashMap<>();
        users.put("Olzhas", "SilentDarkness");
        users.put("Jacob", "ScarletSkies");
        users.put("Ashley", "BlueEyes");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getIt(String json) {
        Response.ResponseBuilder builder;


//        Type type = new TypeToken<Map<String, String>>(){}.getType();
//        Map<String, String> loginAttempt = new Gson().fromJson(json, type);
        LoginAttempt loginAttempt = new Gson().fromJson(json, LoginAttempt.class);

        String username = loginAttempt.username;
        String password = loginAttempt.password;

        if (!users.containsKey(username) || !users.get(username).equals(password)) {
            builder = Response.status(Response.Status.UNAUTHORIZED);
        } else {
            builder = Response.ok("drawing.html", MediaType.TEXT_PLAIN_TYPE);
        }
        return builder.build();
    }
}
