package group18;

import org.eclipse.rdf4j.model.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Tutorial {

    void createRepository();

    void createInstances(Model model);

    void modifyInstances();

    void iteratingRdfData();

    void selectQuery(String queryFile);

    void constructQuery(String queryFile);

    Model loadRdfFile(File inputFile) throws FileNotFoundException, IOException;

    void writeRdfFile(String outputFile) throws FileNotFoundException, IOException;

}
