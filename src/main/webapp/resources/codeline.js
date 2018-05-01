function Codeline(text, lineNumber, handler) {
	var $li = $("<li>").addClass("code");
	var $pre = $("<pre>").text(text).appendTo($li);
	$li.data("lineNumber", lineNumber);
	
	$li.mousedown(function() {
		handler.setStartline($(this).data("lineNumber"));
	});
	
	$li.mouseup(function() {
        handler.setEndline($(this).data("lineNumber"));
		handler.createAddCommentBlock();
    });
    
	return $li;
}