package model;

import java.util.ArrayList;

public class Course {
	String id;
	String title;
	Instructor courseInstructor;
	ArrayList<Student> registeredStudents;
	
	public Course(String id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public Course(String id, String title, Instructor courseInstructor, ArrayList<Student> registeredStudents) {
		this.id = id;
		this.title = title;
		this.courseInstructor = courseInstructor;
		this.registeredStudents = registeredStudents;
	}
}
