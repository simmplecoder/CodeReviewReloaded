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
				<p>From: <input type="text" id="datepickerFrom"></p>
			</div>
			<div class = "w3-col m4 l4 w3-center">
				<p>To: <input type="text" id="datepickerTo"></p>
			</div>
			<div class = "w3-col m4 l4 w3-center">
				<p> Keyword: <input type="text" name="search" > </p>
			</div>
			<div class = "w3-col m1 l1 w3-right">
				<p> <button class="w3-button w3-green" onclick="getdate();"> Find </button> </p>
			</div>
		</div>
		
		<div class = "w3-row">
			

		</div>

		<div class = "w3-row w3-center">

			<div class = "w3-col m12 l12" style="margin-top: 10px; background-color: #E8FDE1;">
				<p id = "datelog"> empty. </p>
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

	function getdate() {
		console.log($("#datepickerTo").val());
		console.log($("#datepickerFrom").val());
	}

</script>

</html>

    