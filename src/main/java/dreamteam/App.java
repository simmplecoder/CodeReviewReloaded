package dreamteam;

import auth.UnencryptedPasswordKeeper;

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
            System.out.println(path.getPath());
            String realPath = path.getPath();
            String[] splitedPath = realPath.split("/");
            realPath = "";
            for (int i = 0; i < splitedPath.length - 2; i++) {
                realPath += splitedPath[i] + "/";
            }
            realPath += "credentials.txt";
            File file = new File(realPath);
            System.out.println(realPath);
            file.createNewFile(); // if file already exists will do nothing
            UnencryptedPasswordKeeper keeper = new UnencryptedPasswordKeeper(realPath);
//            singletons.add(new LoginResource(keeper));
//            singletons.add(new RegisterResource(keeper));
//            singletons.add(new CoursesDao());
//            singletons.add(new AssignmentsDao());
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
