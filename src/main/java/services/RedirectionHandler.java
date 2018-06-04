package services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

public class RedirectionHandler {
    private HttpServletRequest request;

    public RedirectionHandler(HttpServletRequest request) {
         this.request = request;
    }

    public Response redirection(boolean needLogin) {
        URI uri = null;
        HttpSession session = request.getSession();
        if (session.getAttribute("email") == null) {
            if (needLogin) {
                try {
                    uri = new URI("/index.jsp");
                } catch (URISyntaxException e) {
                    System.out.println(e.getMessage());
                }
                return Response.temporaryRedirect(uri).build();
            }
        } else {
            if (!needLogin) {
                try {
                    uri = new URI("/home.jsp");
                } catch (URISyntaxException e) {
                    System.out.println(e.getMessage());
                }
                return Response.temporaryRedirect(uri).build();
            }
        }
        return null;
    }
}
