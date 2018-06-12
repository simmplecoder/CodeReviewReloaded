drop database IF EXISTS CodeReviewToolTest;
create database CodeReviewToolTest;
use CodeReviewToolTest;

CREATE TABLE `user` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`email` varchar(40) NOT NULL UNIQUE,
	`password` varchar(40) NOT NULL,
	`first_name` varchar(40) NOT NULL,
	`last_name` varchar(40) NOT NULL,
	`isInstructor` bool NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `course` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`title` varchar(40) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `assignment` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`title` varchar(40) NOT NULL,
	`desc` varchar(100) NOT NULL,
	`course_id` bigint NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `enrolled_course` (
	`user_id` bigint NOT NULL,
	`course_id` bigint NOT NULL
);

CREATE TABLE `teaching_course` (
	`user_id` bigint NOT NULL,
	`course_id` bigint NOT NULL
);

CREATE TABLE `submission` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`user_id` bigint NOT NULL,
	`assignment_id` bigint NOT NULL,
	PRIMARY KEY (`id`)
);

ALTER TABLE `assignment` ADD CONSTRAINT `assignment_fk0` FOREIGN KEY (`course_id`) REFERENCES `course`(`id`);

ALTER TABLE `enrolled_course` ADD CONSTRAINT `enrolled_course_fk0` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`);

ALTER TABLE `enrolled_course` ADD CONSTRAINT `enrolled_course_fk1` FOREIGN KEY (`course_id`) REFERENCES `course`(`id`);

ALTER TABLE `teaching_course` ADD CONSTRAINT `teaching_course_fk0` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`);

ALTER TABLE `teaching_course` ADD CONSTRAINT `teaching_course_fk1` FOREIGN KEY (`course_id`) REFERENCES `course`(`id`);

ALTER TABLE `submission` ADD CONSTRAINT `submission_fk0` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`);

ALTER TABLE `submission` ADD CONSTRAINT `submission_fk1` FOREIGN KEY (`assignment_id`) REFERENCES `assignment`(`id`);

insert into user values(null, `btyler@nu.edu.kz`, `qwerty`, `Ben`, `Tyler`, 1);
insert into user values(null, `student@nu.edu.kz`, `qwerty`, `Student`, `Student`, 0);
