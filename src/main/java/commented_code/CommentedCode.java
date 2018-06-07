package commented_code;

import java.util.ArrayList;
import java.util.Arrays;

// This is for File.
public class CommentedCode {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String submissionId;
    private String name;

	private ArrayList<String> lines;
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	
	public CommentedCode(String code) {
		lines = new ArrayList<String>(Arrays.asList(code.split("\n")));
	}
	
	public void addComment(Comment comment) {
		comments.add(comment);
	}
}
