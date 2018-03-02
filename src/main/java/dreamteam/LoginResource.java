package dreamteam;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Path("login")
public class LoginResource {
    private static Map<String, String> users;

    static {
        users = new HashMap<>();
        users.put("Olzhas", "SilentDarkness");
        users.put("Jacob", "ScarletSkies");
        users.put("Ashley", "BlueEyes");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getIt(String json) {
        //Response.ok("drawing.html", MediaType.TEXT_PLAIN_TYPE);
        return Response.ok().build();
    }
}
