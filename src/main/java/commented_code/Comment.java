package commented_code;

public class Comment {
	private int start, end;
	private String author;
	private String comment;
	
	public Comment(String comment, String author, int start, int end) {
		this.comment = comment;
		this.author = author;
		this.start = start;
		this.end = end;
	}
	
	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getComment() {
		return comment;
	}
	public String getAuthor() {
		return author;
	}	
}
