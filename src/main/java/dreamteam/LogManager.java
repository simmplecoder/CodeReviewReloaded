package dreamteam;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import representations.LogRequestFormat;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

class LogManager {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static void setLogStatus(boolean on, ServletContext context) {
        try {
            String status = on ? "on" : "off";
            URL url = context.getResource("/WEB-INF/");
            File file = new File(url.getPath() + "logStatus.json");
            FileOutputStream os = new FileOutputStream(file, false);
            file.createNewFile();
            os.write(("{\"status\":\"" + status + "\"}").getBytes());
            os.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static void addLog(String message, String date, String type, ServletContext context) {
        if (!isOn(context)) {
            return;
        }
        try {
            File file = getLogFile(context);
            String content = new Scanner(file).useDelimiter("\\Z").next();
            JsonArray logs = new JsonParser().parse(content).getAsJsonArray();

            JsonObject log = new JsonObject();
            log.addProperty("message", message);
            log.addProperty("date", date);
            log.addProperty("type", type);
            logs.add(log);

            FileOutputStream os = new FileOutputStream(file, false);
            os.write(logs.toString().getBytes());
            os.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static String getLogs(LogRequestFormat request, ServletContext context) {
        try {
            File file = getLogFile(context);
            String content = new Scanner(file).useDelimiter("\\Z").next();
            JsonArray jsonArray = (JsonArray) new JsonParser().parse(content);
            JsonArray result = new JsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject object = element.getAsJsonObject();
                if (matches(object, request)) {
                    result.add(object);
                }
            }
            return result.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "[]";
        }
    }

    static boolean isOn(ServletContext context) {
        try {
            URL url = context.getResource("/WEB-INF/");
            File file = new File(url.getPath() + "logStatus.json");
            if (file.createNewFile()) {
                FileOutputStream os = new FileOutputStream(file, false);
                os.write("{\"status\":\"on\"}".getBytes());
                os.close();
            }
            String content = new Scanner(file).useDelimiter("\\Z").next();
            JsonObject logStatus = new JsonParser().parse(content).getAsJsonObject();
            String status = logStatus.get("status").getAsString();
            return status.equals("on");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static boolean matches(JsonObject object, LogRequestFormat request) {
        boolean typeCheck = request.types.contains(object.get("type").getAsString());
        boolean fromCheck = false, toCheck = false;
        Date date = null, from = null, to = null;
        try {
            date = dateFormat.parse(object.get("date").getAsString());
            from = dateFormat.parse(request.from + " 00:00:00");
        } catch (ParseException e) {
            fromCheck = true;
        }
        try {
            to = dateFormat.parse(request.to + " 23:59:59");
        } catch (ParseException e) {
            toCheck = true;
        }
        assert date != null;
        if (!fromCheck) {
            fromCheck = date.compareTo(from) >= 0;
        }
        if (!toCheck) {
            toCheck = date.compareTo(to) <= 0;
        }
        return typeCheck && fromCheck && toCheck;
    }

    private static File getLogFile(ServletContext context) throws IOException {
        URL url = context.getResource("/WEB-INF/");
        File file = new File(url.getPath() + "logs.json");
        if (file.createNewFile()) {
            FileOutputStream os = new FileOutputStream(file, false);
            os.write("[]".getBytes());
            os.close();
        }
        return file;
    }
}
