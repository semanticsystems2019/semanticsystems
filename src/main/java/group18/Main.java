package group18;

public class Main {
    public static void main(String[] args) throws Exception {
        Tutorial tutorial = new Rdf4jHandler();
        tutorial.loadRdfFile(Util.ONTOLOGY_FILE);

        Rdf4jHandler rdf4jHandler = new Rdf4jHandler();
        rdf4jHandler.createInstances();
        rdf4jHandler.selectQuery("demo.sparql");
    }
}
