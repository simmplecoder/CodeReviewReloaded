package dreamteam;

import com.google.gson.Gson;
import representations.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

public class PlaceholderDatabaseResource implements DatabaseResource {
    private static ArrayList<Course> placeholderCourses = new ArrayList<>();
    static {
        Course course0 = new Course();
        course0.id = "id0";
        course0.title = "Computational physics";
        course0.url = "assignments";
        placeholderCourses.add(course0);

        Course course1 = new Course();
        course1.id = "id1";
        course1.title = "Programming paradigms";
        placeholderCourses.add(course1);

        Course course2 = new Course();
        course2.id = "id2";
        course2.title = "Software Engineering";
        placeholderCourses.add(course2);
    }

    private static ArrayList<Assignment> placeholderAssignments = new ArrayList<>();
    static {
        Assignment assignment0 = new Assignment();
        assignment0.description = "this is placeholder assignment description";
        assignment0.id = "hw1";
        assignment0.title = "homework #1";
        placeholderAssignments.add(assignment0);

        Assignment assignment1 = new Assignment();
        assignment1.description = "this is placeholder assignment description";
        assignment1.id = "hw2";
        assignment1.title = "homework #2";
        placeholderAssignments.add(assignment1);
    }

    private static ArrayList<Submission> placeholderSubmissions = new ArrayList<>();
    static {
        Submission submission0 = new Submission();
        submission0.id = "My submission";
        submission0.date = "March 8, 23:43";
        submission0.title = "My submission";
        placeholderSubmissions.add(submission0);
    }

    private static ArrayList<File> placeholderFiles = new ArrayList<>();
    static {
        File file = new File();
        file.id = "file0";
        file.name = "App.java";
        placeholderFiles.add(file);
    }

    @Override
    public Response getCourses(String json) {
        CoursesRequestFormat coursesrequest = new Gson().fromJson(json, CoursesRequestFormat.class);
        //insert checks here

        String courses = new Gson().toJson(placeholderCourses);
        return Response.ok(courses, MediaType.APPLICATION_JSON_TYPE).build();
    }

    @Override
    public Response getAssignments(String json) {
        AssignmentsRequestFormat assignmentsRequest = new Gson().fromJson(json, AssignmentsRequestFormat.class);
        //insert checks here

        String assignments = new Gson().toJson(placeholderAssignments);
        return Response.ok(assignments, MediaType.APPLICATION_JSON_TYPE).build();
    }

    @Override
    public Response getSubmissions(String json) {
        SubmissionsRequestFormat submissionRequest = new Gson().fromJson(json, SubmissionsRequestFormat.class);
        //insert checks here

        String submission = new Gson().toJson(placeholderSubmissions);
        return Response.ok(submission, MediaType.APPLICATION_JSON_TYPE).build();
    }

    @Override
    public Response getFiles(String json) {
        FilesRequestFormat filesRequest = new Gson().fromJson(json, FilesRequestFormat.class);
        //insert checks here

        String files = new Gson().toJson(placeholderFiles);
        return Response.ok(files, MediaType.APPLICATION_JSON_TYPE).build();
    }

    @Override
    public Response getSingleFile(String json) {
        SingleFileRequestFormat singleFileRequest = new Gson().fromJson(json, SingleFileRequestFormat.class);
        //insert checks here

        String singleFile = new Gson().toJson(new CommentedCode("some code"));
        return Response.ok(singleFile, MediaType.APPLICATION_JSON_TYPE).build();
    }
}
