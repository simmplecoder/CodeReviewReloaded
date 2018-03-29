function getFiles() {

	var files = [{"name":"main.cpp", "id" : "id01"}, {"name":"vector.cpp", "id" : "id01"}];
	
	let $ul = $("<ul>").addClass("w3-ul w3-panel  w3-light-grey w3-leftbar w3-border-red");
	
	for (let i = 0; i < files.length; i++) {
		let file = files[i];
		let $li = $("<li>");

		let $button = $("<li>").appendTo($li);
		$button.text(file["name"]);
		let $div = $("<div>").addClass("w3-hide").appendTo($li);

		$button.click(function() {
			$li.removeAttr('class');
			$div.empty();
			$div.append(createColoredCode(data));
			PR.prettyPrint();

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