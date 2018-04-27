<%@ page contentType="text/html;charset=UTF-8"%>
<%
	if (session.getAttribute("username") != null) {
		response.sendRedirect("home.jsp");
	}
%>
<!DOCTYPE html>
<html>
<head>
    <title>Login/Register</title>
    <meta charset="UTF-8">

    <!-- JQuery -->
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    
    <!--  Custom JS functions and classes -->
    <script type="text/javascript" src="resources/authorization_validator.js"></script>
    <script type="text/javascript" src="resources/loaders/make_request.js"></script>

    <!-- w3schools css -->
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>

<body>

<div>
    <div class = "w3-card w3-display-middle" id="login_div">
        <div class = "w3-container w3-green">
             <h3> Login/Register </h3>
        </div>
       
       	<div class="w3-row">
	        <form id="login" class="w3-container w3-margin-bottom w3-padding-24" >
	            <input id="loginEmail" class="w3-input " placeholder="E-mail" style="margin-top:2px;">
	            <input id="loginPassword" class="w3-input w3-margin-bottom" type="password" placeholder="Password" style="margin-top:2px;">
	            <div class = "w3-small w3-text-grey" id = "warningLogin"></div>
	        </form>
		</div>
		
		<div class="w3-row">
	        <form id="register" class="w3-container w3-padding-32" style="display: none;">
	        		<div class="w3-row">
		            <input id="firstname" class="w3-input" placeholder="First name" name="first_name" style="margin-top:2px;">
		            <div id = "firstNameWarning" class="w3-text-grey w3-small">Please enter your first name</div>
	            </div>
	            
	            <input id="lastname" class="w3-input" placeholder="Last name" name="last_name" style="margin-top:2px;">
	            <div id = "lastNameWarning" class="w3-text-grey w3-small">Please enter your last name</div>
	        
	            <input id="registerEmail" class="w3-input" placeholder="E-mail" name="email" style="margin-top:2px;">
	            <div id = "emailWarning" class="w3-text-grey w3-small">Enter any valid e-mail</div>
	        
	            <input id="registerPassword" class="w3-input" type="password" name = "password" placeholder="Password" style="margin-top:2px;">
	            <div id = "passwordWarning" class="w3-text-grey w3-small">Password length should be more than 6 symbols</div>
	        
	            <input id="registerPasswordConfirm" class="w3-input" type="password" name = "password" placeholder="Password" style="margin-top:2px;">
	           	<div id = "passwordConfirmWarning" class="w3-text-grey w3-small">Passwords should match</div> 
	           
	           	<div class="w3-row w3-middle">
	           		<div class = "w3-col">
		           		<div id = "warningRegistration" class="w3-text-grey w3-small"> no result </div>
	           		</div>
	           	</div> 
	        </form>
        </div>
        
        <div class="w3-row">
        		<div class="w3-container">
	        		<button id = "submitButton" class="w3-btn w3-display-bottommiddle w3-green " type="button">Login</button>
        		</div>
        </div>
        
        <div class="w3-row">
	        <div id = "switcher_div">
	            <a href="#" class ="w3-display-bottomright w3-margin-bottom w3-margin-right" id="switcher"> Register </a>
	        </div>
        </div>
    </div>
</div>

</body>

<script>
	"use strict";

	
    function sendLoginRequest(params)
    {
    	 	var warningText;
        var request = $.ajax(
            {
                type: "POST",
                url: "services/login",
                contentType: "application/json",
                data: JSON.stringify(params),
                statusCode: {
                		401: function() {
                			$("#warningLogin").text("Invalid username and/or password");
                      }
                  }
            }
        );

        request.success(function (data, textStatus, xhr) {
            $("#warningLogin").text("Successful login");
            window.location = data;
        });
    }

    function sendRegistrationRequest(params)
    {
        var request = $.ajax({
            type: "POST",
            url: "services/register",
            contentType: "application/json",
            async: false,
            data: JSON.stringify(params), 
            statusCode: {
        		 409: function() {
        			$("#warningLogin").text("User with such email is taken.");
              }
          }
        });

        request.success(function (data, textStatus, xhr) {
            window.location = data;
        })
        
        
    }

    function performLogin(){
        var params = {};
        params.username = $("#loginEmail").val();
        params.password = $("#loginPassword").val();

        sendLoginRequest(params);
    }

    function performRegistration() {
        var params = {};
        params.firstname = $("#firstname").val();
        params.lastname = $("#lastname").val();
        params.password = $("#registerPassword").val();
        params.password2 = $("#registerPasswordConfirm").val();
        params.email = $("#registerEmail").val();

        var authorization_result = authorization_validate(params);
        if (authorization_result === true) {
            sendRegistrationRequest(params);
        }
        else {
            $("#firstNameWarning").text(["Firstname should be less than 30 characters", "Please enter your first name"][1 - authorization_result[0]]);
            $("#lastNameWarning").text(["Lastname should be less than 30 characters", "Please enter your last name"][1 - authorization_result[1]]);
            $("#passwordWarning").text(["Invalid password.", "Password length should be more than 6 symbols"][1 - authorization_result[2]]);
            $("#passwordConfirmWarning").text(["Passwords don't match", "Passwords should match"][1 - authorization_result[3]]);
            $("#emailWarning").text(["Invalid email", "Please enter valid e-mail"][1 -authorization_result[4]]);
        }
    }

    var currentAuthorizationMethod = performLogin;
    const form_modes = ["Register", "Login"];
    const form_display_mode = ["none", "block"];
    const LOGIN = 0;
    var current_mode = LOGIN;

    $("#switcher").click(function() {
        $("#login").css("display", form_display_mode[current_mode]);
        $("#register").css("display", form_display_mode[current_mode ^ 1]);
        $("#submitButton").text(form_modes[current_mode]);
        $("#switcher").text(form_modes[current_mode ^ 1]);
        current_mode ^= 1;

        if (currentAuthorizationMethod === performLogin) {
            currentAuthorizationMethod = performRegistration;
        }
        else {
            currentAuthorizationMethod = performLogin;
        }
    });

    $("#submitButton").click(function() {
        currentAuthorizationMethod();
    })
    
	$("#login_div").keypress(function(e) {
	    if(e.which == 13) {
	    		currentAuthorizationMethod();
	    }
	});
    
</script>

</html>