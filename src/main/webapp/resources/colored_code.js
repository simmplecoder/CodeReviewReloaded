function createColoredCode(file_id) {
    const TAG_LI = "<li>";
    const TAG_DIV = "<div>";
    const TAG_OL = "<ol>";
    const TAG_BOLD = "<b>";
    const TAG_PRE = "<pre>";

    var $maincode = $("<div>");

    var comments = [];

    var startLine, endLine;
    var arrayPre = [];
    var arrayOfLineAndComments = [];
    var editWindowOpened = false;
    var $editBlockRef;

    // var params = { "id" : file_id };
    // var fileArr = make_request("file", params);
    // data = fileArr[0];

    REUSE = {
        createCodeLine: function(content, lineNumber) {
            var $li = $(TAG_LI).addClass("codeline");
            if (content == "")
            		content = " ";
            var $pre = $(TAG_PRE).addClass("prettyprint").text(content);
            
            $pre.appendTo($li);
            $li.data("lineNumber", lineNumber);
            
            $li.mouseup(function() {
                endLine = $(this).data("lineNumber");
                if(startLine > endLine)
                    endLine = [startLine, startLine = endLine][0];
                console.log("Selection is started from line " + startLine + " to line " + endLine);
                createAddCommentBlock(startLine, endLine);
            });

            $li.mousedown(function() {
                startLine = $(this).data("lineNumber");
            });
            
            $li.removeClass("w3-list li");
            return $li;
        },
        createCommentLine: function(data, line) {
            var $div = $(TAG_DIV).addClass("codecomment");
            var $span = $(TAG_BOLD).appendTo($div).css({"color" : "purple"});
            $span.text(data["author"]);

            $div.append(" - " + data['comment']);
            $div.data("multiply", 5);
            $div.data("full_comment", data['comment']);
            $div.data("lineNumber", line);

            $div.on("click", function() {
                var multiply = $(this).data("multiply");
                $(this).height($(this).height() * multiply);
                $(this).data("multiply", 1 / multiply);
            });

            $div.mouseup(function() {
                endLine = $(this).data("lineNumber");
                if(startLine > endLine)
                    endLine = [startLine, startLine = endLine][0];
                console.log("Selection is started from line " + startLine + " to line " + endLine);
                createAddCommentBlock(startLine, endLine);
            });

            $div.mousedown(function() {
                startLine = $(this).data("lineNumber");
                startLine++;
            });

            return $div;
        }
    };

    function SortByEndline(a, b){
        var aIndex = a['end'], bIndex = b['end'];
        return ((aIndex < bIndex) ? -1 : ((aIndex > bIndex) ? 1 : 0));
    }

    function loadAndSortComments(dataComments) {
            $.each( dataComments, function( key, val ) {
                comments.push(val);
            })
            comments.sort(SortByEndline);
    };

    loadAndSortComments(data['comments']);

    function loadLines (data, parent) {
        // $(parent).html("");

        arrayPre = [];

        parent.empty();
        var $ol = $(TAG_OL, {id:"oldMainCode"}).appendTo(parent);
        var line = 1;
        var current = 0;

        console.log($(parent));

        $.each( data, function( key, val ) {
            var $pre = $(TAG_PRE).addClass("prettyprint").appendTo($ol);
            arrayPre.push($pre);
            $pre.append(REUSE.createCodeLine(val, line));  // should be fixed.
            while(current < comments.length && comments[current]["end"] == line) {
                $ol.append(REUSE.createCommentLine(comments[current], line));
                current += 1;
            }
            line += 1;
        })
        // PR.prettyPrint();
    };
    
    function loadLines2 (data, parent) {
        arrayOfLineAndComments = [];
        parent.empty();
        var $ol = $(TAG_OL, {id:"oldMainCode"}).appendTo(parent);
        var line = 1;
        var current = 0;

        console.log($(parent));

        $.each( data, function( key, val ) {
        		var $li = REUSE.createCodeLine(val, line);
        		$li.appendTo($ol);
        		arrayOfLineAndComments.push($li);
            
            while(current < comments.length && comments[current]["end"] == line) {
                $ol.append($(TAG_LI).append(REUSE.createCommentLine(comments[current], line)));
                current += 1;
            }
            line += 1;
        })
        // PR.prettyPrint();
    };
    
    function createAddCommentBlock(startLine, endLine) {
        if(editWindowOpened)
            return;
        else
        		editWindowOpened = true;
        
        var $addCommentBlock = $("<div>").addClass("w3-container w3-card-4 w3-padding-small w3-section");
        var $commentLabel = $("<h6>").addClass("w3-text-green").text("Your comment:").appendTo($addCommentBlock);
        var $commentInput = $("<input>").addClass("w3-input w3-border w3-margin-bottom").appendTo($addCommentBlock);
        var $submitButton = $("<button>").addClass("w3-btn w3-green w3-margin-right").text("Submit").appendTo($addCommentBlock);
        var $cancelButton = $("<button>").addClass("w3-btn w3-red").text("Cancel").appendTo($addCommentBlock);
        
        $submitButton.click(function() {
        		var comment = {
                    "file_id" : file_id, 
                    "start" : startLine,
                    "end" : endLine,
                    "comment" : $commentInput.val(),
                    "author" : "user"
            };

        	$editBlockRef.remove();
            editWindowOpened = false;
            comments.push(comment);
            console.log(comments);

            // make_request("addComment", comment); // TODO

            comments.sort(SortByEndline);
            loadLines2(data["lines"], $maincode);
            PR.prettyPrint();
        });
        
        $cancelButton.click(function() {
        		$editBlockRef.remove();
            editWindowOpened = false;
        });
        
        arrayPre[endLine - 1].after($addCommentBlock);
        editWindowOpened = true;
		$editBlockRef = $addCommentBlock;
    }

    loadLines2(data["lines"], $maincode);
    return $maincode;
    
//  function createEditBlock(startLine, endLine) {
//  if(editWindowOpened)
//      return;
//  var li = "<li>Hello</li>";
//  var childs = $("#test").children();
//
//  var $editBlock = $("<div>").html("<p> Add a comment </p>");
//  $editBlock.addClass("editBlock");
//  var $input = $("<input>").appendTo($editBlock);
//
//  var $okButton = $("<button>").text("OK").appendTo($editBlock).click(function() {
//      var comment = { 
//               
//              "start" : startLine,
//              "end" : endLine,
//              "comment" : $input.val(),
//              "author" : "user"
//      }
//
//      $editBlockRef.remove();
//      editWindowOpened = false;
//      comments.push(comment);
//      console.log(comments);
//
//      // make_request("addComment", comment); // TODO
//
//      loadLines(data["lines"], $maincode);
//      PR.prettyPrint();
//  });
//  
//  var $cancelButton = $("<button>").text("Cancel").appendTo($editBlock).click(function() {
//      $editBlockRef.remove();
//      editWindowOpened = false;
//  });
//
//  arrayPre[endLine-1].after($editBlock);
//  editWindowOpened = true;
//  $editBlockRef = $editBlock;
//}
}
