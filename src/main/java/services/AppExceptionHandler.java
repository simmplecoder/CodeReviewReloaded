package services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionHandler implements ExceptionMapper<NotFoundException> {

    private static final Logger logger = LogManager.getLogger(AppExceptionHandler.class);

    @Override
    public Response toResponse(NotFoundException e) {
        logger.info("Not exception happened: " + e);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("This is an invalid request. At least one field format is not readable by the system.")
                .type( MediaType.TEXT_PLAIN)
                .build();
    }
}