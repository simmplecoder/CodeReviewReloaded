package services;

import model.Course;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

public class RedirectionHandler {
    private HttpServletRequest request;

    private static final Logger logger = LogManager.getLogger(RedirectionHandler.class);

    public RedirectionHandler(HttpServletRequest request) {
         this.request = request;
    }

    public Response redirection(boolean needLogin) {
        URI uri = null;
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            logger.info("Client is not authorized");
            if (needLogin) {
                try {
                    uri = new URI("/index.jsp");
                } catch (URISyntaxException e) {
                    System.out.println(e.getMessage());
                }
                logger.info("Redirection to index.jsp is required");
                return Response.temporaryRedirect(uri).build();
            }
        } else {
            logger.info("Client is authorized");
            if (!needLogin) {
                try {
                    uri = new URI("/home.jsp");
                } catch (URISyntaxException e) {
                    System.out.println(e.getMessage());
                }
                logger.info("Redirection to home.jsp is required");
                return Response.temporaryRedirect(uri).build();
            }
        }

        logger.info("No redirection is required");
        return null;
    }
}
