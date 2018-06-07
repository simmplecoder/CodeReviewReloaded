var current_course_id;

function getCurrenCourseID() {
	return current_course_id;
}

function courses_loader(params) {
    var courses = make_request("courses", params);

	// var courses = [{"title" : "CSCI 151", "id" : "id0"}, {"title" : "CSCI 152", "id" : "id1"}];

    console.log(courses);

	var $ul = $("<ul>").addClass("w3-ul w3-hoverable");
	
	for (let i = 0; i < courses.length; i++) {
		let course = courses[i];
		let $li = $("<li>");
		$li.addClass("w3-padding-large w3-hover-pale-green");
		$li.text(course["title"]);
		$li.click(function() {
			$("#addAssignmentButton").empty();
			$("#addAssignmentButton").text("Add a new assignment to ");
			$("#addAssignmentButton").append($("<b>").text(course["title"]));
			
			$("#addAssignmentButton").addClass("w3-show");
			$("#assignment_list").empty();
			$("#assignment_list").append(assignments_loader(course["id"]));
			current_course_id = course["id"];
		});
		
		$ul.append($li);
	}
	return $ul;
}