package model;

import javax.persistence.*;

@Entity
@Table(name="assignment")
public class Assignment {

	@Id
    @GeneratedValue
    @Column(name = "id")
	int id;

    @Column(name = "title")
	String title;

    @Column(name = "desc")
	String desc;

    @Column(name = "course_id")
    int course_id;

    public Assignment(String title, String desc, int course_id) {
        this.title = title;
        this.desc = desc;
        this.course_id = course_id;
    }

	public Assignment(int id, String title, String desc, int course_id) {
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.course_id = course_id;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String toString() {
        return this.id + " " + this.title + " " + this.desc + " " + this.course_id;
    }
}
