package dreamteam;

import auth.PasswordKeeper;
import auth.UnencryptedPasswordKeeper;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class App extends Application {
    private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> classes = new HashSet<>();

    public App(@Context ServletContext context) {
        URL credentialsPath;
        try {
            credentialsPath = context.getResource("/credentials.txt");
            PasswordKeeper keeper = new UnencryptedPasswordKeeper(credentialsPath.getPath());
            singletons.add(new LoginResource(keeper));
            singletons.add(new RegisterResource(keeper));
            singletons.add(new PlaceholderDatabaseResource());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public Set<Object> getSingletons()
    {
        return singletons;
    }

    public Set<Class<?>> getClasses()
    {
        return classes;
    }
}
