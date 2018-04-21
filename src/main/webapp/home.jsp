<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("index.jsp");
    }
    if (session.getAttribute("username").equals("admin")) {
        response.sendRedirect("admin.jsp");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome!</title>
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    
    <!-- Loading ColoredCode -->
    <script type="text/javascript" src="resources/colored_code.js"></script>
    <script type="text/javascript" src="resources/prettify.js"></script>
    <link href="resources/pretty_style.css" rel="stylesheet">
    <link href="resources/css/style.css" rel="stylesheet">

    <!-- Loading loaders JS -->
    <script type="text/javascript" src="resources/loaders/assignments_loader.js"></script>
    <script type="text/javascript" src="resources/loaders/courses_loader.js"></script>
    <script type="text/javascript" src="resources/loaders/submissions_loader.js"></script>
    <script type="text/javascript" src="resources/loaders/list-loaders.js"></script>
    <script type="text/javascript" src="resources/loaders/upload_creator.js"></script>
    <script type="text/javascript" src="resources/loaders/files_loader.js"></script>
    <script type="text/javascript" src="resources/loaders/make_request.js"></script>
    <script type="text/javascript" src="resources/loaders/toggler.js"></script>

    
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="resources/css/home.css">

    <!-- Loading jsons, later should be replaced with Ajax Requests  -->
    <script type="text/javascript" src="resources/json/data.json"></script>
    <script type="text/javascript" src="resources/json/submissions.json"></script>
    <script type="text/javascript" src="resources/json/submissions2.json"></script>
    
</head>
<body>
    
<header class="w3-container w3-green">
    <div class="w3-row">
        <div class = "w3-col m11 l11">
            <h1>Code Review Tool</h1>
        </div>
        <div class = "w3-col m1 l1">
        <p>
              <button class = "w3-button w3-white" onclick="logout();"> Exit
              </button>
              </p>
        </div>
    </div>
</header>

<div class="w3-container">
  <div class="w3-row">
        <div class="w3-col" style="padding-left: 10px; padding-top:20px;">
            <h4 class ="w3-text-dark-grey"> <b> Your registered course: </b></h4>
        </div>
  </div>
  <div class="w3-row">
        <div class="w3-col m3 l3 w3-text-green" style="padding: 5px; height: 500px;">
        	
            <%if (session.getAttribute("isInstructor").equals(1)) { %>
        
            <div id="addCourseDiv">
            <button class="w3-border w3-button" id="addCourseButton" onclick="switchAddCourse()"> &#8724; course </button>
            <button class="w3-border w3-button" id="addAssignmentButton" onclick="switchAddAssignment()"> &#8724; assignment </button>
            </div>
            
            <% } %>

            <div id = "course_list" class = "w3-border w3-round"></div>
        </div>
        <div class="w3-col m9 l9" style="padding: 10px; height: 500px;">
            <div id = "assignment_list" class = "w3-border w3-round">
            </div>
        </div>
    </div>
</div>


</body>

<script>

    $("#course_list").append(courses_loader());
    
    function logout() {
        var request = $.ajax({
            type: "POST",
            url: "services/logout",
            async: false
        });
        console.log(data);
        console.log(request.status);
        request.success(function (data, textStatus, xhr) {
            window.location = data;
        })
    }

    
    var courseWin = false;

    function initBlock(placeholder, placeholder2) {
        var $addBlock = $("<div>").appendTo("#addCourseDiv");
        $addBlock.addClass("addBlock w3-hide");

        var $input = $("<input>").attr("placeholder", placeholder).appendTo($addBlock);
        $input.addClass("inputCourse");
        
        var $input2;
        if (placeholder2) {
        		$input2 = $("<input>").attr("placeholder", placeholder2).appendTo($addBlock);
            $input2.addClass("inputCourse");
        }

        var $okButton = $("<button>").text("OK").appendTo($addBlock).click(function() {
            var course = {
                "course" : $input.val()
            }
            
            // make_request("addCourse", course) // TODO
	
            $input.val("");
            toggle($addBlock);    
        });
        
        $okButton.addClass("buttonCourse");

        var $cancelButton = $("<button>").text("Cancel").appendTo($addBlock).click(function() {
            $input.val("");
            toggle($addBlock);    
        });
        
        $cancelButton.addClass("buttonCourse");
        
        return $addBlock;
    }
    
    const COURSE = 0;
    const ASSIGNMENT = 1;
    const NOTDEFINED = 2;
    
    var addBlockType = NOTDEFINED;

    var $addCourseBlock = initBlock("Course name", null);
    var $addAssignmentBlock = initBlock("Assignment name", "Assignment description");

    function switchAddCourse() {
    		if (addBlockType == NOTDEFINED) {
    			toggle($addCourseBlock);
    			addBlockType = COURSE;
    		} else if (addBlockType == COURSE) {
    			toggle($addCourseBlock);
    			addBlockType = NOTDEFINED;
    		} else {
    			toggle($addCourseBlock);
    			toggle($addAssignmentBlock);
    			addBlockType = COURSE;
    		}
    }
    
	function switchAddAssignment() {
		if (addBlockType == NOTDEFINED) {
			toggle($addAssignmentBlock);
			addBlockType = ASSIGNMENT;
		} else if (addBlockType == ASSIGNMENT) {
			toggle($addAssignmentBlock);
			addBlockType = NOTDEFINED;
		} else {
			toggle($addCourseBlock);
			toggle($addAssignmentBlock);
			addBlockType = ASSIGNMENT;
		}
    }

</script>

</html>