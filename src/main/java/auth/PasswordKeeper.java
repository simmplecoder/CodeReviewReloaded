package auth;

public interface PasswordKeeper {
    boolean exists(String user, String password);
    boolean register(String user, String password);
}
