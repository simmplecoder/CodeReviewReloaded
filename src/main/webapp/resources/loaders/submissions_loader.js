function submissions_loader(assignment_id) {
	var params = { "id" : assignment_id};
	// var submissions = make_request("submissions", params);

	var submissions = [{"title":"by Ibrakhim Ilyassov", "id" : "id01"}, {"title":"by Anuar Maratkhan", "id" : "id02"}];
	
	var $ul = $("<ul>").addClass("w3-ul w3-panel  w3-light-grey w3-leftbar w3-border-green");
	
	for (let i = 0; i < submissions.length; i++) {
		let submission = submissions[i];
		let $li = $("<li>");
		$li.addClass("w3-padding-large w3-hover-pale-green");
		

		let $button = $("<li>").appendTo($li);
		$button.text(submission["title"]);
		let $div = $("<div>").addClass("w3-hide").appendTo($li);
		
		$button.click(function() {
			let $files = getFiles(submission["id"]);
			$div.empty();
			$div.append($files);
			toggle($div, $button);
		});
		
		$ul.append($li);
	}
    
	return $ul;
}