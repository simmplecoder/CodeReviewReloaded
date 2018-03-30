function createColoredCode(data) {
    const TAG_LI = "<li>";
    const TAG_DIV = "<div>";
    const TAG_OL = "<ol>";
    const TAG_BOLD = "<b>";
    const TAG_PRE = "<pre>";

    var $maincode = $("<div>");

	var comments = [];

    var startLine, endLine;
    var arrayPre = [];
    var editWindowOpened = false;
    var $editBlockRef;

    REUSE = {
        createCodeLine: function(content, lineNumber) {
            var $li = $(TAG_LI).addClass("codeline").append(content)
            $li.data("lineNumber", lineNumber);
            $li.mouseup(function() {
                endLine = $(this).data("lineNumber");
                if(startLine > endLine)
                    endLine = [startLine, startLine = endLine][0];
                console.log("Selection is started from line " + startLine + " to line " + endLine);
                createEditBlock(startLine, endLine);
            });

            $li.mousedown(function() {
                startLine = $(this).data("lineNumber");
            });
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
                createEditBlock(startLine, endLine);
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

    function loadAndSortComments(data) {
            $.each( data, function( key, val ) {
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
            $pre.append(REUSE.createCodeLine(val, line));
            while(current < comments.length && comments[current]["end"] == line) {
                $ol.append(REUSE.createCommentLine(comments[current], line));
                current += 1;
            }
            line += 1;
        })
        // PR.prettyPrint();
    };

    function createEditBlock(startLine, endLine) {
        if(editWindowOpened)
            return;
        var li = "<li>Hello</li>";
        var childs = $("#test").children();

        var $editBlock = $("<div>").html("<p> Add a comment </p>");
        $editBlock.addClass("editBlock");
        var $input = $("<input>").appendTo($editBlock);

        var $okButton = $("<button>").text("OK").appendTo($editBlock).click(function() {
			var comment = {
			         "start" : startLine,
			         "end" : endLine,
			         "author" : "defaultUser",
			         "comment" : $input.val()
			}

            $editBlockRef.remove();
            editWindowOpened = false;
			comments.push(comment);
			console.log(comments);
            loadLines(data["lines"], $maincode);
            PR.prettyPrint();
        });
        var $cancelButton = $("<button>").text("Cancel").appendTo($editBlock).click(function() {
            $editBlockRef.remove();
            editWindowOpened = false;
        });

        arrayPre[endLine-1].after($editBlock);
        editWindowOpened = true;
        $editBlockRef = $editBlock;
    }

    // loadLines(data["lines"], "#maincode");
    loadLines(data["lines"], $maincode);
    // PR.prettyPrint();

    console.log("hello JS2");
    return $maincode;
}
