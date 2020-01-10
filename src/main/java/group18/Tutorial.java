package group18;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Tutorial {

    void createRepository();

    void createInstances();

    void modifyInstances();

    void iteratingRdfData();

    void selectQuery(String queryFile);

    void constructQuery(String queryFile);

    void loadRdfFile(File inputFile) throws FileNotFoundException, IOException;

    void writeRdfFile(String outputFile) throws FileNotFoundException, IOException;

}
