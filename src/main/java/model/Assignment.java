package model;

import java.util.ArrayList;

public class Assignment {
	
	String id;
	String title;
	String description;
	Instructor instructor;
	ArrayList<Student> students;
	
	public Assignment(String id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}
	
	public Assignment(String id, String title, String description, Instructor instructor, ArrayList<Student> students) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.instructor = instructor;
		this.students = students;
	}
}
