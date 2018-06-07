<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session.getAttribute("email") == null) {
        response.sendRedirect("index.jsp");
    } else
    if (session.getAttribute("email").equals("admin")) {
        response.sendRedirect("admin.jsp");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome!</title>
    
    <!-- Remote JS libraries -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.12.0/highlight.min.js"></script>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    
    <!-- Local JS libraries -->
    <script type="text/javascript" src="resources/loaders/assignments_loader.js"></script>
    <script type="text/javascript" src="resources/loaders/courses_loader.js"></script>
    <script type="text/javascript" src="resources/loaders/submissions_loader.js"></script>
    <script type="text/javascript" src="resources/loaders/list-loaders.js"></script>
    <script type="text/javascript" src="resources/loaders/upload_creator.js"></script>
    <script type="text/javascript" src="resources/loaders/files_loader.js"></script>
    <script type="text/javascript" src="resources/loaders/make_request.js"></script>
    <script type="text/javascript" src="resources/loaders/toggler.js"></script>
    
    <!-- Loading ColoredCode -->
    <script type="text/javascript" src="resources/highlighted_code.js"></script>
    <script type="text/javascript" src="resources/colored_code.js"></script>
    <script type="text/javascript" src="resources/codeline.js"></script>

	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <%--<link href="resources/pretty_style.css" rel="stylesheet">--%>
    <link href="resources/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="resources/css/home.css">
    
    <link rel="stylesheet" href="resources/css/atom-one-light.min.css">
    <link rel="stylesheet" href="resources/css/w3override.css">

    <!-- Loading jsons, later should be replaced with Ajax Requests  -->
    <script type="text/javascript" src="resources/json/data.json"></script>
    <script type="text/javascript" src="resources/json/submissions.json"></script>
    <script type="text/javascript" src="resources/json/submissions2.json"></script>
    
</head>
<body>
    
<header class="w3-container w3-green">
    <div class="w3-row">
        <div class = "w3-col m10 l10">
            <h1>Code Review Tool</h1>
        </div>
        
        <div class = "w3-col m2 l2">
        		<div class="w3-dropdown-hover w3-right w3-cell-middle w3-center w3-green" style="height:100%;">
    			 	<div> <% 	String name = "some text";
        					if (session.getAttribute("email") != null) {
        						name = session.getAttribute("email").toString();
        					}
        				%> 		
        				<h4> <%=name%> </h4>
  				</div>
  				
	      		<div class="w3-dropdown-content w3-bar-block w3-card" style="right:0">
                    <button class = "w3-button w3-text-green w3-hover-green w3-block " onclick="redirect_to_home();"> Home </button>
       				<button class = "w3-button w3-text-green w3-hover-green w3-block "> Settings </button>
       				<button class = "w3-button w3-text-green w3-hover-green w3-block" onclick="logout();"> Exit </button>
	       		</div>
        		</div>
        </div>
    </div>
</header>

<div class="w3-container">
  <div class="w3-row">
        <div class="w3-col" style="padding-left: 10px; padding-top:20px;">
            <h4 class ="w3-text-dark-grey"> <b> Available courses where you are not registered: </b></h4>
        </div>
  </div>
  <div class="w3-row">
        <div class="w3-col m12 l12 w3-text-green" style="padding: 5px; height: 500px;">
            <div id = "course_list" class = "w3-border w3-round"></div>
        </div>
    </div>
</div>


</body>

<script>

    var parameters = {"registered" : false};
    $("#course_list").append(not_enrolled_course_loader(parameters));

    function not_enrolled_course_loader(params) {
        var courses = make_request("courses", params);

        var $ul = $("<ul>").addClass("w3-ul w3-hoverable");

        for (let i = 0; i < courses.length; i++) {
            let course = courses[i];
            let $li = $("<li>");
            $li.addClass("w3-padding-large w3-hover-pale-green");
            $li.text(course["title"]);

            let $button = $("<button>");
            $button.addClass("w3-button w3-green w3-right w3-tiny");
            $button.text("Register");
            $li.append($button);

            $button.click(function() {
                let selected_course_id = course["id"];
                console.log("registering to course id " + selected_course_id);
                var result = make_request("registertocourse", {"course_id":selected_course_id});
                console.log(result);
                $("#course_list").empty();
                $("#course_list").append(not_enrolled_course_loader(parameters));
            });
            $ul.append($li);
        }
        return $ul;
    }

    function redirect_to_home() {
        window.location = "home.jsp";
    }
    
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

</script>

</html>