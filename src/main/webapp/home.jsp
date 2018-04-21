<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("index.jsp");
    } else
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
        	
            <%if (session.getAttribute("username") != null && session.getAttribute("isInstructor").equals(1)) { %>
        
            <div id="addCourseDiv">
            <button class="w3-border w3-button w3-green addButton" id="addCourseButton" onclick="switchAddCourse()"> Add a new course </button>
            </div>
            
            <% } %>

            <div id = "course_list" class = "w3-border w3-round"></div>
        </div>
        
        <div class="w3-col m9 l9" style="padding: 10px; height: 500px;">
        		<%if (session.getAttribute("username") != null && session.getAttribute("isInstructor").equals(1)) { %>

	        <div id="addAssignmentDiv">
    	    			<button class="w3-border w3-button  w3-green addbutton" id="addAssignmentButton" onclick="switchAddAssignment()"> Add a new assignment </button>
   	    		</div>

            <% } %>
            
            <div id = "assignment_list" class = "w3-border w3-round"> </div>
        </div>
    </div>
</div>


</body>

<script>

    $("#course_list").append(courses_loader());
    
    var idgetter = getCurrenCourseID;
    
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

    function initAddCourseBlock() {
        var $addBlock = $("<div>").appendTo("#addCourseDiv");
        $addBlock.addClass("addBlock w3-hide");

        var $input = $("<input>").attr("placeholder", "A new course name").appendTo($addBlock);
        $input.addClass("inputCourse w3-input");

        var $okButton = $("<button>").text("Submit").appendTo($addBlock).click(function() {
            var params = { "coursename" : $input.val() };
            if ($input.val() != "") {
            		make_request("createcourse", params);
            		$input.val("");
                	toggle($addBlock);
               	$("#course_list").empty();
                	$("#course_list").append(courses_loader());
            }
        });
        
        $okButton.addClass("buttonCourse w3-button w3-green");
        return $addBlock;
    }
    
    function initAddAssignmentBlock() {
        var $addBlock = $("<div>").appendTo("#addAssignmentDiv");
        $addBlock.addClass("addBlock w3-hide");

        var $input = $("<input>").attr("placeholder", "A new assignment title").appendTo($addBlock);
        $input.addClass("inputCourse w3-input");
        
        var $input2 = $("<input>").attr("placeholder", "A description").appendTo($addBlock);
        $input2.addClass("inputCourse w3-input");

        var $okButton = $("<button>").text("Submit").appendTo($addBlock).click(function() {
            var params = { 
            		"course_id" : getCurrenCourseID(), 
            		"title" : $input.val(),
            		"description" : $input2.val()
            	};
            
            if ($input.val() != "" && $input2.val() != "") {
	            make_request("createassignment", params);
	            $input.val("");
	            $input2.val("");
	            toggle($addBlock);    
            }
        });
        
        $okButton.addClass("buttonCourse w3-button w3-green");
        return $addBlock;
    }
    
    const COURSE = 0;
    const ASSIGNMENT = 1;
    const NOTDEFINED = 2;
    
    var addBlockType = NOTDEFINED;

    var $addCourseBlock = initAddCourseBlock();
    var $addAssignmentBlock = initAddAssignmentBlock();

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