function HighlightedCode(dataa, file_id) {

	console.log("file id is " + file_id);

	var lines = dataa["lines"];
	var arrayOfLines = [], arrayOfComments = [];
	var comments = dataa["comments"];
	var startline, endline;
	
	var $ol = $("<ol>");

    function SortByEndline(a, b){
        var aIndex = a['end'], bIndex = b['end'];
        return ((aIndex < bIndex) ? -1 : ((aIndex > bIndex) ? 1 : 0));
    }
	
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
		let $div = $("<div>").addClass("codecomment noselection").css({"font-family": "sans-serif"});
        let $span = $("<b>").appendTo($div).text(comment["author"]).css({color:"#e33465"});
        $div.append(" : " + comment["comment"]);

        $div.data("full_comment", comment['comment']);
        $div.data("startline", comment["start"]);
        $div.data("endline", comment["end"]);
        $div.data("open", false);

        $div.on("click", function() {

            console.log("clicked");

            let start = $(this).data("startline");
            let end = $(this).data("endline");
            let open = $(this).data("open");

            console.log(open);
            console.log(start);
            console.log(end);

            for (let line = 0; line < arrayOfLines.length; line++) {
                arrayOfLines[line].removeClass("specialone");
                if (start <= line && line <= end && open === false)
                    arrayOfLines[line].addClass("specialone");
		    }

            if (open === true) {
                $(this).data["open"] = false;
            } else {
                $(this).data["open"] = true;
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
            var commentText = $commentInput.val();
        		var comment = {
                    "file_id" : file_id, 
                    "start" : startline,
                    "end" : endline,
                    "comment" : commentText,
                    "author" : "default"
                };

            $editBlockRef.remove();
            editWindowOpened = false;
            
            // SAVING COMMENT TO BACKEND.
            var promise = new Promise(function(resolve, reject) {
                var result = make_request("addcomment", comment)
                // console.log(result["comment"]);
                if (result["comment"] && result["comment"] === commentText) {
                    resolve("Comment successfully added!");
                    comments.push(result);
                    fillWithLineAndComments();
                } else {
                    reject(Error("Comment adding failed!"));
                }
            });

            promise.then(function(result) {
                console.log(result); // "Stuff worked!"
            }, function(err) {
                console.log(err); // Error: "It broke"
            });
            
            for (var line = startline; line <= endline; line++) {
                arrayOfLines[line].removeClass("specialone");
	        }

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
	 			var $commentRef = createComment(comments[current]); 
	 			arrayOfComments.push($commentRef);
			    $ol.append($commentRef);
			    current += 1;
	 		}
	    }
	}
	
	fillWithLineAndComments();
	return $ol;
}