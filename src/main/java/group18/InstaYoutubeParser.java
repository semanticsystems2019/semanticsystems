package group18;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class InstaYoutubeParser {

    public void parse(Repository repo, Model model) {
        ValueFactory valueFactory = repo.getValueFactory();
        File baseDirectory = new File(getClass().getClassLoader().getResource("instagram/csv/").getFile());
        Value commentType = model.objects().stream().filter(o -> o.stringValue().contains("Comment")).findFirst().get();
        int counter = 0;
        for (File csvFile : baseDirectory.listFiles()) {
            try (
                Reader reader = Files.newBufferedReader(csvFile.toPath(), Charset.forName("windows-1252"));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withDelimiter(';'));
            ) {
                for (CSVRecord csvRecord : csvParser) {
                    try (RepositoryConnection conn = repo.getConnection()) {
                        InstaYoutubeComment comment = new InstaYoutubeComment(csvRecord);
                        IRI commentIri = valueFactory.createIRI(Util.NS, "comment" + counter);

                        conn.add(commentIri, RDF.TYPE, commentType);
                    }

                    counter++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private static class InstaYoutubeComment {
        final int id;
        final int subId;
        final String name;
        final String data;
        final int likes;
        final String comment;
        final String link;

        InstaYoutubeComment(CSVRecord csvRecord) {
            id = Integer.parseInt(csvRecord.get("Id"));
            String subIdString = csvRecord.get("SubId");
            if (subIdString != null && subIdString.length() > 0) {
                subId = Integer.parseInt(csvRecord.get("SubId"));
            } else {
                subId = 0;
            }
            name = csvRecord.get("Name (click to view profile)");
            data = csvRecord.get("Date");
            likes = Integer.parseInt(csvRecord.get("Likes"));
            comment = csvRecord.get("Comment");
            link = csvRecord.get("Link");
        }
    }
}
