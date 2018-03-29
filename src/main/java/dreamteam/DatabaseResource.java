package dreamteam;


import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface DatabaseResource {
    @Path("/courses")
    @Produces(MediaType.APPLICATION_JSON)
    Response getCourses(String json);

    @Path("/assignments")
    Response getAssignments(String json);

    @Path("/submissions")
    Response getSubmissions(String json);

    @Path("/files")
    Response getFiles(String json);

    @Path("/file")
    Response getSingleFile(String json);
}
