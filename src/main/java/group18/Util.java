package group18;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Util {
    public static final String NS = "http://www.semanticweb.org/group18/movie-ontology/";
    public static final String SERVER_URL = "http://localhost:7200/";
    public static final File ONTOLOGY_FILE = new File(Util.class.getClassLoader().getResource("MovieOntology.ttl").getFile());
    public static final String MOVIENAMES = "src/main/resources/movienames.json";

    public static String simpleEmotionResolver() {
        Random random = new Random();
        int emotion = random.nextInt(3);
        if (emotion == 0)
            return "Positive";
        else if (emotion == 1)
            return "Negative";
        else
            return "Indifferent";
    }

    public static JSONObject loadJSON(String path) {
        File file = new File(path);
        String content = null;
        try {
            content = FileUtils.readFileToString(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(content);
    }
}
