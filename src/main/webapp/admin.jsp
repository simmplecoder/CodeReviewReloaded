<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    

<!DOCTYPE html>
<html>
<head>
	<title>Admin Page</title>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>

	<link rel="stylesheet" href="jquery-ui.css">
  	
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

	<div class = "w3-container" style="width: 70%; margin:auto;">
		<div class = "w3-row">
			<div class = "w3-col m3 l3 w3-left">
				<p> From: <input type="text" id="datepickerFrom"></p>
			</div>
			<div class = "w3-col m4 l4 w3-center">
				<p> To: <input type="text" id="datepickerTo"></p>
			</div>
			<div class = "w3-col m4 l4">
				<p> <input class="w3-check" type="checkbox" id = "checkboxlogs" > Login logs.</p>
				<p> <input class="w3-check" type="checkbox" id = "checkboxregisters" > Register logs.</p>
				<p> <input class="w3-check" type="checkbox" id = "checkboxuploads" > Upload logs.</p>
				
			</div>
			<div class = "w3-col m1 l1 w3-right">
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
		
		var types = [$("#checkboxlogs").is(':checked'), $("#checkboxregisters").is(':checked'), $("#checkboxuploads").is(':checked')];
		
		var params = {"from" : from, "to" : to, "types" : types};
		
		/* var logs = make_request("loggingsearch", params); */
		
		var logs = [{"title" : "Failing login service."}, {"title" : "Worked on logout button."}, {"title" : "Crashing course to login service."}];
		
		$("#results").empty();
		
		var $ul = $("<ul>");
		for (let i = 0; i < logs.length; i++) {
			let log = logs[i];
			let $li = $("<li>");
			$li.addClass("w3-padding-small w3-hover-pale-green");
			$li.text(log["title"]);
			$ul.append($li);
		}
		
		$("#results").append($ul);
	}

</script>

</html>

    