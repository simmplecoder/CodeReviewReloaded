package services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger logger = LogManager.getLogger();

    public App(@Context ServletContext context) {
        singletons.add(new LoginHandler());
        singletons.add(new RegistrationHandler());
        singletons.add(new CoursesHandler());
        singletons.add(new AssignmentsHandler());
        singletons.add(new CreateCourseHandler());
        singletons.add(new CreateAssignmentHandler());
        singletons.add(new SubmissionsHandler());
        singletons.add(new FilesHandler());
        singletons.add(new UploadHandler());
        singletons.add(new CourseRegistrationHandler());
        singletons.add(new AddCommentHandler());
        singletons.add(new UsersHandler());
        singletons.add(new AppExceptionHandler());
        singletons.add(new GrantPrivilegeHandler());

        logger.info("Loaded all services");
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
