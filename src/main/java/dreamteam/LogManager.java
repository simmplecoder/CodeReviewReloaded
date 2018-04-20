package dreamteam;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import representations.LogModel;
import representations.LogRequest;

import java.io.*;
import java.util.Scanner;

class LogManager {
    static void addLog(LogModel log) {
        try {
            File file = new File("logs.json");
            String content = new Scanner(file).useDelimiter("\\Z").next();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", log.message);
            jsonObject.addProperty("date", log.date);
            jsonObject.addProperty("type", log.type);

            content = content.substring(0, content.lastIndexOf(']')) + "," + jsonObject + "]";
            FileOutputStream os = new FileOutputStream(file, false);
            os.write(content.getBytes());
            os.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static String getLogs(LogRequest request) {
        try {
            String content = new Scanner(new File("logs.json")).useDelimiter("\\Z").next();
            JsonArray jsonArray = (JsonArray) new JsonParser().parse(content);
            JsonArray result = new JsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject object = element.getAsJsonObject();
                if (matches(object, request)) {
                    result.add(object);
                }
            }
            return result.getAsString();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static boolean matches(JsonObject object, LogRequest request) {
        String type = object.get("type").toString();
        String date = object.get("date").toString();

        return request.type.contains(type);
    }
}
