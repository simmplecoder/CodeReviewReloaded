package commented_code;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Submission {
    private String id;
    private String title;
    private String email;
    private int assignmentId;
    private List<String> fileIds;

    public Submission() { }

    public Submission(String title, String email, int assignmentId) {
        this.title = title;
        this.email = email;
        this.assignmentId = assignmentId;
        this.fileIds = new ArrayList<>();
    }

    public Submission(int id, String title, String email, int assignmentId, ArrayList<String> fileIds) {
        this.title = title;
        this.email = email;
        this.assignmentId = assignmentId;
        this.fileIds = fileIds;
    }

    public Submission(String title, String email, int assignmentId, ArrayList<String> fileIds) {
        this.title = title;
        this.email = email;
        this.assignmentId = assignmentId;
        this.fileIds = fileIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public void addFileId(String id) {
        this.fileIds.add(id);
    }

    public String toString() {
        return this.title + " " + this.email + " " + this.assignmentId + " " + this.fileIds;
    }
}
