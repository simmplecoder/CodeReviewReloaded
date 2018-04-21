<%@ page contentType="text/html;charset=UTF-8"%>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("index.jsp");
    }
    if (!session.getAttribute("username").equals("admin")) {
        response.sendRedirect("admin.jsp");
    }
%>
<!DOCTYPE html>
<html>
<head>
	<title>Admin Page</title>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	
  	<link href="resources/css/jquery-ui.css" rel="stylesheet">
  	
  	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	
	<script type="text/javascript" src="resources/loaders/make_request.js"></script>

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
			<div class = "w3-col m3 l3 w3-left w3-cell-middle">
				<p> From: <input type="text" id="datepickerFrom"></p>
			</div>
			<div class = "w3-col m4 l4 w3-center w3-cell-middle">
				<p> To: <input type="text" id="datepickerTo"></p>
			</div>
			<div class = "w3-col m4 l4 w3-cell-middle">
				<p> <input class="w3-check" type="checkbox" id = "checkboxlogs" > Login logs.</p>
				<p> <input class="w3-check" type="checkbox" id = "checkboxregisters" > Register logs.</p>
				<p> <input class="w3-check" type="checkbox" id = "checkboxuploads" > Upload logs.</p>
				
			</div>
			<div class = "w3-col m1 l1 w3-right w3-cell-middle">
				<p> <button class="w3-button w3-green" onclick="findLogs();"> Find </button> </p>
			</div>
		</div>
	
		<div class = "w3-row w3-center">
			<div id = "results" class = "w3-col m12 l12" style="margin-top: 10px;">
			</div>
		</div>
	</div>
</body>

<script>

	function logout() {
        var request = $.ajax({
            type: "POST",
            url: "services/logout",
            async: false
        });

        request.success(function (data, textStatus, xhr) {
            window.location = data;
        })
    }

	$( function() {
		$( "#datepickerFrom" ).datepicker({
			dateFormat: "yy-mm-dd"
		});
	});

	$( function() {
		$( "#datepickerTo" ).datepicker({
		dateFormat: "yy-mm-dd"
		});
	});

	function findLogs() {
		var from = $("#datepickerFrom").val();
		var to = $("#datepickerTo").val();
		
		var types = [];
		if ($("#checkboxlogs").is(':checked'))
			types.push("login");
		
		if ($("#checkboxregisters").is(':checked'))
			types.push("register");
		
		if ($("#checkboxuploads").is(':checked'))
			types.push("upload");
	
		var params = {"from" : from, "to" : to, "types": types};
		
		console.log(JSON.stringify(params));
		
		var logs = make_request("loggingsearch", params);
		
		// var logs = [{"types" : "Login", "message" : "Failing login service.", "date" : "12.04.2018"}, {"types" : "Register", "message" : "Failing register service.", "date" : "20.04.2018"}, {"types" : "Upload", "message" : "Upload login service.", "date" : "04.04.2018"}];
		
		$("#results").empty();
		
		var $ul = $("<ul class ='w3-ul'>");
		for (let i = 0; i < logs.length; i++) {
			let log = logs[i];
			let $li = $("<li>");
			$li.addClass("w3-padding-small w3-hover-pale-green");
			$li.text(log["type"] + " : " + log["date"] + " : " + log["message"]);
			$ul.append($li);
		}
		
		$("#results").append($ul);
	}

</script>

</html>

    