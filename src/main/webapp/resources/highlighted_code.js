function HighlightedCode(dataa) {
	var lines = dataa["lines"];
	var arrayOfLines = [];
	
	function SortByEndline(a, b){
        var aIndex = a['end'], bIndex = b['end'];
        return ((aIndex < bIndex) ? -1 : ((aIndex > bIndex) ? 1 : 0));
    }

	var comments = dataa["comments"];
	
	var startline;
	var endline;
	
	var $ol = $("<ol>");
	
	function createLine(text, lineNumber) {
		var $li = $("<li>").addClass("code");
		
		var $pre = $("<pre>").text(text).appendTo($li);
		$li.data("lineNumber", lineNumber);
		
		$li.mousedown(function() {
			startline = $(this).data("lineNumber"); 
		});
		
		$li.mouseup(function() {
	        endline = $(this).data("lineNumber");
			createAddCommentBlock();
	    });
	    
		return $li;
	}

	function createComment(comment) {
		var $div = $("<div>").addClass("codecomment");
        var $span = $("<b>").appendTo($div).css({"color" : "purple"});
        $span.text(comment["author"]);
        $div.append(" - " + comment["comment"]);
		return $div;
	}

	var editWindowOpened = false;
	var $editBlockRef;
	function createAddCommentBlock() {
        if(editWindowOpened)
            return;
        else
    			editWindowOpened = true;
        
        if(startline > endline)
            endline = [startline, startline = endline][0];
        
        
        for (var line = startline; line <= endline; line++) {
	    		arrayOfLines[line].addClass("specialone");
	    }
        
        var $addCommentBlock = $("<div>").addClass("w3-container w3-card-4 w3-padding-small w3-section");
        var $commentLabel = $("<h6>").addClass("w3-text-green").text("Your comment:").appendTo($addCommentBlock);
        var $commentInput = $("<input>").addClass("w3-input w3-border w3-margin-bottom").appendTo($addCommentBlock);
        var $submitButton = $("<button>").addClass("w3-btn w3-green w3-margin-right").text("Submit").appendTo($addCommentBlock);
        var $cancelButton = $("<button>").addClass("w3-btn w3-red").text("Cancel").appendTo($addCommentBlock);
        
        $submitButton.click(function() {
        		var comment = {
                    "file_id" : file_id, 
                    "start" : startline,
                    "end" : endLine,
                    "comment" : $commentInput.val(),
                    "author" : "user"
            };

        	$editBlockRef.remove();
            editWindowOpened = false;
            comments.push(comment);
            console.log(comments);
            // make_request("addComment", comment); // TODO
            fillWithLineAndComments();
        });
        
        $cancelButton.click(function() {
    			$editBlockRef.remove();
            editWindowOpened = false;
            
            for (var line = startline; line <= endline; line++) {
            		arrayOfLines[line].removeClass("specialone");
            }
        });

        arrayOfLines[endline].after($addCommentBlock);
        editWindowOpened = true;
		$editBlockRef = $addCommentBlock;
    }
	
	// Filling the code with lines and comments.
	function fillWithLineAndComments() {
	    arrayOfLines = []; // An array of references to li tags.
	    $ol.empty(); // Emptying childs of ol tag.
	    comments.sort(SortByEndline);

	    var current = 0;
	    for (var line = 0; line < lines.length; line++) {
	    		var $lineRef = createLine(lines[line], line); 
	    		arrayOfLines.push($lineRef);
	    	 	$ol.append($lineRef);
	 		while(current < comments.length && comments[current]["end"] == line) {
			     $ol.append(createComment(comments[current]));
			     current += 1;
	 		}
	    }
	}
	
	fillWithLineAndComments();
	return $ol;
}