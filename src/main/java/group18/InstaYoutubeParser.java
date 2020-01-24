package group18;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class InstaYoutubeParser {

    public void parse(Repository repo, Map<String, IRI> iris, String source) {
        ValueFactory valueFactory = repo.getValueFactory();
        File baseDirectory = new File(getClass().getClassLoader().getResource(source + "/csv/").getFile());
        int counter = 0;
        for (File csvFile : baseDirectory.listFiles()) {
            try (
                Reader reader = Files.newBufferedReader(csvFile.toPath(), Charset.forName("windows-1252"));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withDelimiter(';'));
            ) {
                for (CSVRecord csvRecord : csvParser) {
                    String idString = csvRecord.get("Id");
                    if (idString != null && idString.length() > 0) {
                        try (RepositoryConnection conn = repo.getConnection()) {
                            InstaYoutubeComment comment = new InstaYoutubeComment(csvRecord);
                            IRI commentIri = valueFactory.createIRI(Util.NS, source + "/comment#" + counter);
                            conn.add(commentIri, RDF.TYPE, iris.get("Comment"));

                            conn.add(commentIri, iris.get("hasDate"), valueFactory.createLiteral(comment.date));
                            // TODO
                        }
                    }

                    counter++;
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }


    private static class InstaYoutubeComment {
        static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

        final int id;
        final String name;
        final Date date;
        final int likes;
        final String comment;
        // final String link;

        InstaYoutubeComment(CSVRecord csvRecord) throws ParseException {
            id = Integer.parseInt(csvRecord.get("Id"));
            name = csvRecord.get("Name (click to view profile)");
            date = DATE_FORMAT.parse(csvRecord.get("Date"));
            likes = Integer.parseInt(csvRecord.get("Likes"));
            comment = csvRecord.get("Comment");
            // link = csvRecord.get("Link");
        }
    }
}
