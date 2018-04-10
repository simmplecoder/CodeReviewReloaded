<%--
  Created by IntelliJ IDEA.
  User: dark
  Date: 4/10/18
  Time: 5:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("/index_test.jsp");
    }
%>
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
    <script type="text/javascript" src="resources/loaders/items_loader.js"></script>
    <script type="text/javascript" src="resources/loaders/toggler.js"></script>

    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="resources/css/home.css">

    <!-- Loading jsons, later should be replaced with Ajax Requests	 -->
    <script type="text/javascript" src="resources/json/data.json"></script>
    <script type="text/javascript" src="resources/json/submissions.json"></script>
    <script type="text/javascript" src="resources/json/submissions2.json"></script>

</head>
<body>

<header class="w3-container w3-green">
    <h1>Code Review Tool</h1>
</header>

<div class="w3-container">
    <div class="w3-row">
        <div class="w3-col" style="padding-left: 10px; padding-top:20px;">
            <h4 class ="w3-text-dark-grey"> <b> Your registered course: </b></h4>
        </div>
    </div>
    <div class="w3-row">
        <div class="w3-col m3 l3 w3-text-green" style="padding: 5px; height: 500px;">
            <div id = "course_list" class = "w3-border w3-round">
            </div>
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

</script>

</html>