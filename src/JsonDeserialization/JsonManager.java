package src.JsonDeserialization;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonManager {

    public static void main(String[] args) {
        JsonManager manager = new JsonManager();
        try {
            SettingsSettings settings = new SettingsSettings("Pog", "ers", false);
            manager.writeJSON(settings,"student.json");
            SettingsSettings settings2 = manager.readJSON("student.json");
            System.out.println(settings2.OAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void writeJSON(SettingsSettings settings, String file) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter writer = new FileWriter(getSettingsPath()+file);

        writer.write(gson.toJson(settings));
        writer.close();
    }

    private SettingsSettings readJSON(String file) throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(getSettingsPath()+file));

        SettingsSettings student = gson.fromJson(bufferedReader, SettingsSettings.class);
        return student;
    }
    private String getSettingsPath() {
        try {
            URI resourcePathFile = this.getClass().getClassLoader().getResource("RESOURCE_PATH").toURI();
            String resourcePath = resourcePathFile.toString().substring(6);
            URI rootURI = new File("").toURI();
            URI resourceURI = new File(resourcePath).toURI();
            URI relativeResourceURI = rootURI.relativize(resourceURI);
            return relativeResourceURI.getPath().substring(relativeResourceURI.getPath().indexOf("C:"),relativeResourceURI.getPath().indexOf("SomethingThatShouldBeMadeButIsInFactNotMade.ja"));
        } catch (Exception e) {
            return null;
        }
    }
}
