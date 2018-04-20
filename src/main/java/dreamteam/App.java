package dreamteam;

import com.jcraft.jsch.JSchException;
import datagatherer.AssignmentsDao;
import datagatherer.CoursesDao;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class App extends Application {
    private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> classes = new HashSet<>();

    public App(@Context ServletContext context) {
        try {
            singletons.add(new CoursesDao());
            singletons.add(new AssignmentsDao());
            singletons.add(new RequestFilter());
        } catch (JSchException | SQLException | ClassNotFoundException e) {
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
