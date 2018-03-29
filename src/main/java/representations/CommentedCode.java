package representations;

import java.util.ArrayList;
import java.util.Arrays;

// This is for File.
public class CommentedCode {
	private ArrayList<String> lines;
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	
	public CommentedCode(String code) {
		lines = new ArrayList<String>(Arrays.asList(code.split("\n")));
	}
	
	public void addComment(Comment comment) {
		comments.add(comment);
	}
}
