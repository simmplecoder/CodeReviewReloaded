function getFiles(submission_id) {
	var params = { "id" : submission_id};
	// var files = make_request("files", params);

	var files = [{"name":"main.cpp", "id" : "id01"}, {"name":"vector.cpp", "id" : "id01"}];
	
	let $ul = $("<ul>").addClass("w3-ul w3-panel  w3-light-grey w3-leftbar w3-border-red");
	
	for (let i = 0; i < files.length; i++) {
		let file = files[i];
		let $li = $("<li>");

		let $button = $("<li>").appendTo($li);
		$button.text(file["name"]);
		let $div = $("<div>").addClass("w3-hide scrollmenu").appendTo($li);

		$button.click(function() {
			$li.removeAttr('class');
			$div.empty();
			$div.append(HighlightedCode(data, file["id"]));

			hljs.configure({tabReplace: '    '});
			$('pre').each(function(i, block) {
				hljs.highlightBlock(block);
			});

			// PR.prettyPrint();

			toggle($div, $button);
		});
		
		$ul.append($li);
	}
    
	return $ul;
}