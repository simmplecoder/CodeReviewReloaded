<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getEmail().equals("admin") == false) {
        response.sendRedirect("index.jsp");
    }
%>

<!DOCTYPE html>
<html>
<head>
	<title>Admin Page</title>

	<!-- Remote JS libraries -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
  	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

	<link rel="stylesheet" href="resources/css/jquery-ui.css">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	
	<!-- Local JS libraries -->
	<script type="text/javascript" src="resources/loaders/make_request.js"></script>
    <script type="text/javascript" src="resources/loaders/users_loader.js"></script>

</head>
<body>

	<header class="w3-container w3-green">
		<div class="w3-row">
	        <div class = "w3-col m11 l11">
	            <h1>Admin Page</h1>
	        </div>
	        <div class = "w3-col m1 l1">
	              <p> <button class = "w3-button w3-white" onclick="logout();"> Exit
	              </button> </p>
	        </div>
	    </div>
	</header>

	<div class = "w3-container">
		<div class = "w3-row">
            <h2 class = "w3-pale-green w3-padding-large" >Instructors </h2>

            <div class = "w3-container" id = "instructor_list">

            </div>

        </div>

        <div class = "w3-row">
            <h2 class = "w3-pale-green w3-padding-large" >Students </h2>

            <div class = "w3-container" id = "student_list">

            </div>

        </div>
	</div>
</body>

<script>


    $("#instructor_list").append(users_loader(1));
    $("#student_list").append(users_loader(0));
	
	function logout() {
        var request = $.ajax({
            type: "POST",
            url: "services/logout",
            async: false
        });
        request.success(function (data, textStatus, xhr) {
            window.location = data;
        });
    }

</script>

</html>

    