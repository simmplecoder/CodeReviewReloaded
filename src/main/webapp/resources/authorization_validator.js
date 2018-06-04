function authorization_validate(params) {
    var errors = [false, false, false, false, false];
    
    errors[0] = ("first_name" in params && params.first_name.length > 30);
    errors[1] = ("last_name" in params && params.last_name.length > 30);
    errors[2] = ("password" in params && params.password.length > 30 || params.password.length < 6);  
    errors[3] = ("password2" in params && params.password2 !== params.password); 
    errors[4] = ("email" in params && params.email.search("@") === -1 || params.email.length > 30);
    
    if (errors[0] + errors[1] + errors[2] + errors[3] + errors[4] === 0)
        return true;
    else
        return errors;
}