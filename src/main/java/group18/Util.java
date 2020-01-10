package group18;

import java.io.File;

public class Util {
    public static final String NS = "http://www.semanticweb.org/group18/movie-ontology";
    public static final String SERVER_URL = "http://localhost:7200/";
    public static final File ONTOLOGY_FILE = new File(Util.class.getClassLoader().getResource("MovieOntology.ttl").getFile());
}
