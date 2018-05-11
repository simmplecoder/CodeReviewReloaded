package dreamteam;

import auth.UnencryptedPasswordKeeper;
import datagatherer.AssignmentService;
import datagatherer.CourseService;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class App extends Application {
    private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> classes = new HashSet<>();

    public App(@Context ServletContext context) {
        try {
            URL path = UnencryptedPasswordKeeper.class.getClassLoader().getResource(".");
            String realPath = path.getPath();
            String[] splittedPath = realPath.split("/");
            realPath = "";
            for (int i = 0; i < splittedPath.length - 2; i++) {
                realPath += splittedPath[i] + "/";
            }
            realPath += "credentials.txt";
            File file = new File(realPath);
            file.createNewFile(); // if file already exists will do nothing
            UnencryptedPasswordKeeper keeper = new UnencryptedPasswordKeeper(realPath);
            singletons.add(new CourseService());
            singletons.add(new AssignmentService());
            singletons.add(new RequestFilter(keeper));
        } catch (IOException e) {
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
