package group18;

import org.eclipse.rdf4j.model.Model;

public class Main {
    public static void main(String[] args) throws Exception {
        Tutorial tutorial = new Rdf4jHandler();
        Model model = tutorial.loadRdfFile(Util.ONTOLOGY_FILE);

        Rdf4jHandler rdf4jHandler = new Rdf4jHandler();
        rdf4jHandler.createInstances(model);
//        TwitterParser twp = new TwitterParser();
//        twp.parser();
    }
}

