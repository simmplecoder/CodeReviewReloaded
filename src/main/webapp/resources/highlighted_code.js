function HighlightedCode(dataa, file_id) {
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

	function createComment(comment, startline, endline) {
		var $div = $("<div>").addClass("codecomment");
        var $span = $("<b>").appendTo($div).css({"color" : "purple"});
        $span.text(comment["author"]);
        $div.append(" - " + comment["comment"]);
        $div.addClass("noselection");
        
        $div.append(" - " + data['comment']);
        $div.data("multiply", 5);
        $div.data("full_comment", data['comment']);
        $div.data("startline", comment["start"]);
        $div.data("endline", comment["end"]);
        
        console.log(comment["startline"]);
        console.log(comment["endline"]);
        
        $div.on("click", function() {
            var multiply = $(this).data("multiply");
            $(this).height($(this).height() * multiply);
            $(this).data("multiply", 1 / multiply);
            
            console.log( $(this).data("startline"));
            console.log( $(this).data("endline"));
            
            for (var line = $(this).data("startline"); line <= $(this).data("endline"); line++) {
        		if (multiply === 5)
        			arrayOfLines[line].addClass("specialone");
        		else
            		arrayOfLines[line].removeClass("specialone");
		    }
        });
        
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
                    "end" : endline,
                    "comment" : $commentInput.val(),
                    "author" : "user"
            };

        		$editBlockRef.remove();
            editWindowOpened = false;
            comments.push(comment);
            console.log(comments);
            
            // SAVING COMMENT TO BACKEND.
            // make_request("addComment", comment); // TODO
            
            for (var line = startline; line <= endline; line++) {
	        		arrayOfLines[line].removeClass("specialone");
	        }
            
            fillWithLineAndComments();
            $('pre').each(function(i, block) {
				hljs.highlightBlock(block);
			});
            document.getSelection().removeAllRanges();
        });
        
        $cancelButton.click(function() {
    			$editBlockRef.remove();
            editWindowOpened = false;
            
            for (var line = startline; line <= endline; line++) {
            		arrayOfLines[line].removeClass("specialone");
            }
            document.getSelection().removeAllRanges();
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