function SectionController(root, left_section, right_section) {
	
	let $ul = $("<ul>").addClass("w3-ul w3-hoverable");
	
	let items = root.getItems();
	for (let i = 0; i < items.length; i++) {
		let item = items[i];
		let $li = $("<li>");
		$li.addClass("w3-padding-large w3-hover-pale-green");
		$li.text(item.getTitle());
		
//		$li.click(function() {
//			let id = item.getUrl();
//			console.log("click id: ", id);
//			assignments_loader(id, where);
//		});
		
		$ul.append($li);
	}
	$("#" + left_section).append($ul);
}