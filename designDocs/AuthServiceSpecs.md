## Login and Registration services

Backend for authorization will be divided into two parts. The first one, `AuthService` is just for forwarding from RESTful service to the resource, `AuthResource`. Most of the logic is concentrated in `AuthResource`. It has following responsibilities:

 - Authorize: use login and password to login
 - Check if logged in: checks if the user is logged in using provided username and hash
 - Register: create an entry in the `PasswordKeeper` for the new user, or say that the user already exists

The resource will consist of the following objects:

- `PasswordKeeper`: pseudo-safely store usernames and passwords, also make them persistent
- `HashManager`: the session manager. Logged in users will have their own hash in this manager, and get removed if inactive for long

## Communication with frontend

When entering the site, the client will either get their home page if the hash didn't expire yet, or get redirected to login. Frontend shouldn't worry about this, as browser itself can handle it.

## Login

Send `POST` request to `services/login` or `services/register`. The data should be in JSON format.

---

**Example of login:**

    POST /CodeReview/services/login HTTP/1.1
    Content-Type: application/json
    Content-Length: <length>
    
    {"username": "Ashley", "password": "Massacre"}

**Example of response to successful login**

     200 OK HTTP/1.1
     Content-type: text/plain
     
     <redirection-url>

On login failure status code 401 will be returned. Due to Jersey's implementation of `Response`, it won't be empty, but frontend is safe to ignore body of the response.

## Register

**Example of register:**

    POST /CodeReview/services/register HTTP/1.1
    Content-Type: application/json
    Content-Length: <length>

    {
        "username" : "Ashley"
        "password" : "Massacre"
        "age" : 23
        <other seconday key-value pairs to store in DB>
    }

On **successful registration** status code 200 will be returned. Body of the response can be safely ignored. Frontend should 

On **failure to register**, status code 400 will be sent, and after that it would be great if frontend will not let users send again without modifying the credentials. Body of the response can be safely ignored.

## AuthService

The class will mostly parse JSON and forward to `AuthService`. Communication protocols and formats are concentrated here. After parsing the data it forwards it to `AuthResource`.

## AuthResource

As mentioned above, uses `PasswordKeeper` and `HashManager` to control who comes in and who gets kicked out.

