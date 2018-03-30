function submissions_loader(id) {
//	var params = { "username" : "ibro@nu.edu.kz", "id" : id};
//	
//	var request = $.ajax({
//        type: "POST",
//        url: "services/submissions",
//        contentType: "application/json",
//        async: false,
//        data: JSON.stringify(params)
//    });
//
//	let submissions = [];
//    request.success(function (data, textStatus, xhr) {
//    		console.log(data);
//    		submissions = JSON.parse(data);
//    });
	
	var submissions = [{"title":"by Ibrakhim Ilyassov", "id" : "id01"}, {"title":"by Anuar Maratkhan", "id" : "id02"}];
	
	let $ul = $("<ul>").addClass("w3-ul w3-panel  w3-light-grey w3-leftbar w3-border-green");
	
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

			if ($div.hasClass("w3-hide")) {
                $div.removeClass("w3-hide");
                $div.addClass("w3-show");
                $button.addClass("w3-text-green");
            } else {
                $div.addClass("w3-hide");
                $div.removeClass("w3-show");
                $button.removeClass("w3-text-green");
            }
		});
		
		$ul.append($li);
	}
    
	return $ul;
}