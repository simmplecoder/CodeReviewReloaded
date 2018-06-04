package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="course")
public class Course {
	@Id
	@Column(name="id")
	int id;

	@Column(name="title")
	String title;

	public Course(String title) {
        this.title = title;
    }

	public Course(int id, String title) {
		this.id = id;
		this.title = title;
	}
}
