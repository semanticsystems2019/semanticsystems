package group18;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Tutorial {

    void createRepository();

    void createInstances();

    void modifyInstances();

    void iteratingRdfData();

    void selectQuery(String queryFile);

    void constructQuery(String queryFile);

    void loadRdfFile(String inputFile) throws FileNotFoundException, IOException;

    void writeRdfFile(String outputFile) throws FileNotFoundException, IOException;

}
