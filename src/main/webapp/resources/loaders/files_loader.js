function getFiles() {

	var files = [{"name":"main.cpp", "id" : "id01"}, {"name":"vector.cpp", "id" : "id01"}];
	
	let $ul = $("<ul>").addClass("w3-ul w3-hoverable w3-panel  w3-light-grey w3-leftbar w3-border-red");
	
	for (let i = 0; i < files.length; i++) {
		let file = files[i];
		let $li = $("<li>");

		$li.addClass("w3-padding-large w3-hover-pale-green");
		$li.text(file["name"]);
		$li.click(function() {
			$li.append(createColoredCode(data));
			PR.prettyPrint();
		});
		
		$ul.append($li);
	}
    
	return $ul;
}