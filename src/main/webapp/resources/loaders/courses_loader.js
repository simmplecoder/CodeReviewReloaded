function courses_loader() {
	var params = { "username" : "ibro@nu.edu.kz" };
    var courses = items_loader("courses", params);

//	var courses = [{"title" : "CSCI 151", "id" : "id0"}, {"title" : "CSCI 152", "id" : "id1"}];
	
	var $ul = $("<ul>").addClass("w3-ul w3-hoverable");
	
	for (let i = 0; i < courses.length; i++) {
		let course = courses[i];
		let $li = $("<li>");
		$li.addClass("w3-padding-large w3-hover-pale-green");
		$li.text(course["title"]);
		$li.click(function() {
			$("#assignment_list").empty();
			$("#assignment_list").append(assignments_loader(course["id"]));
		});
		
		$ul.append($li);
	}
	return $ul;
}