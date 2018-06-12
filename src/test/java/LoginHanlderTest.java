
import com.google.gson.Gson;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import representations.LoginAttempt;
import services.App;
import org.apache.http.impl.client.HttpClients;
import services.AuthentificationHandler;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertThat;

public class LoginHanlderTest {
    @Before
    public void setUp() {
//        app = new App();
//        MySQLDatabaseTestSetup setup = new MySQLDatabaseTestSetup();
    }

    @Test
    public void testLogin() throws IOException {
        LoginAttempt attemp = new LoginAttempt();
        attemp.email = "btyler@nu.edu.kz";
        attemp.password = "qwerty";

        AuthentificationHandler handler = new AuthentificationHandler();

        Response response = handler.login(new Gson().toJson(attemp));
        System.out.print(response.getStatus());


//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost("http://localhost:8080/CodeReviewTool/services/login");
//
//        StringEntity entity = new StringEntity();
//
//        httpPost.setEntity(entity);
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-type", "application/json");
//
//        CloseableHttpResponse response = client.execute(httpPost);
//
//        System.out.println(response.getStatusLine().getStatusCode());
    }
}
