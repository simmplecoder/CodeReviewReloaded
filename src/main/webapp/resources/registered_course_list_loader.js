function registered_course_list_loader(where) {
	let courses = [
		{"title": "Programming in C", "course_id" : "id1"}, 
		{"title": "Computational Physics", "course_id" : "id2"},
		{"title": "Operating Systems", "course_id" : "id3"},
		{"title": "Performance and Data Structures", "course_id" : "id4"}
	];
	
	let $ul = $("<ul>").addClass("w3-ul w3-hoverable");
	
	for (let i = 0; i < courses.length; i++) {
		let course = courses[i];
		let $li = $("<li>");
		$li.addClass("w3-padding-large w3-hover-pale-green");
		$li.text(course["title"]);
		$li.click(function() {
			let id = course["course_id"];
			console.log("click id: ", id);
			assignments_loader(id, where);
		});
		
		$ul.append($li);
	}
	return $ul;
}