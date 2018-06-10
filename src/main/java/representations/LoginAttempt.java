package representations;

public class LoginAttempt {
    public String email;
    public String password;

    public String toString() {
        return email + " " + password;
    }
}