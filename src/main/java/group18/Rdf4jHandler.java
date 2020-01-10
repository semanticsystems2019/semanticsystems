package group18;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Rdf4jHandler implements Tutorial {

    private Repository repo;
    private InstaYoutubeParser instaYoutubeParser = new InstaYoutubeParser();

    public Rdf4jHandler() {
        createRepository();
    }

    @Override
    public void createRepository() {
        repo = new HTTPRepository(Util.SERVER_URL, "semanticsystems");
        repo.init();
    }

    @Override
    public void createInstances(Model model) {
        instaYoutubeParser.parse(repo, model);
    }

    @Override
    public void modifyInstances() {

    }

    @Override
    public void iteratingRdfData() {

    }

    @Override
    public void selectQuery(String queryFile) {

    }

    @Override
    public void constructQuery(String queryFile) {

    }

    @Override
    public Model loadRdfFile(File inputFile) throws IOException {
        FileReader reader = new FileReader(inputFile);
        Model model = Rio.parse(reader, Util.NS, RDFFormat.TURTLE);
        try (RepositoryConnection conn = repo.getConnection()) {
            conn.add(model);
        }
        return model;
    }

    @Override
    public void writeRdfFile(String outputFile) throws FileNotFoundException, IOException {

    }
}
