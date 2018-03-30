package auth;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class UnencryptedPasswordKeeper implements PasswordKeeper {
    private Map<String, String> userToPassword;
    private PrintWriter out;

    public UnencryptedPasswordKeeper(String filename) throws IOException {
        out = new PrintWriter(new FileWriter(filename, true));
        userToPassword = Files.lines(Paths.get(filename))
                .filter(line -> !line.isEmpty())
                .map(line -> line.split(" : "))
                .collect(Collectors.toMap(splitLine -> splitLine[0], splitLine -> splitLine[1]));
    }

    @Override
    public boolean exists(String user, String password) {
        return userToPassword.containsKey(user)
                && userToPassword.get(user).equals(password);
    }

    @Override
    public boolean register(String user, String password) {
        if (userToPassword.containsKey(user))
        {
            return false;
        }

        out.write("\n" + user + " : " + password);
        out.flush();
        userToPassword.put(user, password);
        return true;
    }

    @Override
    public String toString()
    {
        return userToPassword.toString();
    }
}
