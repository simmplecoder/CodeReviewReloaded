package commented_code;

public class Comment {
	private int start, end;
	private String author;
	private String comment;
	private String file_id;

	public String getFile_id() {
		return file_id;
	}

	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

	public Comment() {}

	public Comment(String comment, String author, int start, int end) {
		this.comment = comment;
		this.author = author;
		this.start = start;
		this.end = end;
	}

	public Comment(String comment, String author, int start, int end, String file_id) {
		this.comment = comment;
		this.author = author;
		this.start = start;
		this.end = end;
		this.file_id = file_id;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String toString() {
		return "'" + this.comment + "'" + " to file " + this.file_id + "at lines from " + this.start + " to " + this.end + " by " + this.author;
	}
}
