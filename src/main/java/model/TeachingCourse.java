package model;

import javax.persistence.*;

@Entity
@Table(name="teaching_course")
public class TeachingCourse {
    @Id
    @Column(name="course_id")
    int course_id;

    @Column(name="used_id")
    int used_id;

    public TeachingCourse(int course_id, int used_id) {
        this.course_id = course_id;
        this.used_id = used_id;
    }
}
