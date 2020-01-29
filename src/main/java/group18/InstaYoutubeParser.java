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
import java.util.HashMap;
import java.util.Map;

public class InstaYoutubeParser {

    public void parse(Repository repo, Map<String, IRI> iris, String source) {
        ValueFactory valueFactory = repo.getValueFactory();
        File baseDirectory = new File(getClass().getClassLoader().getResource(source + "/csv/").getFile());

        Map<String,String> movieNames = new HashMap<>();
        movieNames.put("AQuietPlace.csv", "A Quiet Place");
        movieNames.put("AStarIsBorn.csv", "A Star is Born");
        movieNames.put("AvengersEndgame.csv", "Avengers: Endgame");
        movieNames.put("BohemianRhapsody.csv", "Bohemian Rhapsody");
        movieNames.put("Deadpool2.csv", "Deadpool 2");
        movieNames.put("FiftyShadesFreed.csv", "Fifty Shades Freed");
        movieNames.put("Joker.csv", "The Joker");
        movieNames.put("MammaMia2.csv", "Mamma Mia! Here We Go Again");
        movieNames.put("TheIncredibles2.csv", "Incredibles 2");
        movieNames.put("Vice.csv", "Vice");
        movieNames.put("Zombieland2.csv", "Zombieland: Double Tap");
        movieNames.put("Crazy Rich Asians.csv", "Crazy Rich Asians");

        int counter = 0;
        for (File csvFile : baseDirectory.listFiles()) {
            try (
                Reader reader = Files.newBufferedReader(csvFile.toPath(), Charset.forName("windows-1252"));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withDelimiter(';'));
            ) {
                counter = 0;
                for (CSVRecord csvRecord : csvParser) {
                    if(counter > 50){
                        break;
                    }
                    String idString = csvRecord.get("Id");
                    if (idString != null && idString.length() > 0) {
                        try (RepositoryConnection conn = repo.getConnection()) {
                            InstaYoutubeComment comment = new InstaYoutubeComment(csvRecord);
                            IRI commentIri = valueFactory.createIRI(Util.NS, source + "/comment#" + counter);
                            conn.add(commentIri, RDF.TYPE, iris.get("Comment"));

                            conn.add(commentIri, iris.get("hasId"), valueFactory.createLiteral(comment.id));
                            conn.add(commentIri, iris.get("createdBy"), valueFactory.createLiteral(comment.name));
                            conn.add(commentIri, iris.get("hasDate"), valueFactory.createLiteral(comment.date));
                            conn.add(commentIri, iris.get("hasLikes"), valueFactory.createLiteral(comment.likes));
                            conn.add(commentIri, iris.get("hasText"), valueFactory.createLiteral(comment.text));
                            conn.add(commentIri, iris.get("hasEmotion"), valueFactory.createLiteral(comment.emotion));
                            conn.add(commentIri, iris.get("hasSource"), valueFactory.createLiteral(source));
                            // TODO change refersToMovie to refer to movie id from moviedb
                            conn.add(commentIri, iris.get("refersToMovie"), valueFactory.createLiteral( movieNames.get(csvFile.getName()) ));
                            // TODO add more stuff?
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
        // TODO Valentin fix date and link
        final Date date;
        final int likes;
        final String text;
        final String emotion;
        // final String link;

        InstaYoutubeComment(CSVRecord csvRecord) throws ParseException {
            id = Integer.parseInt(csvRecord.get("Id"));
            name = csvRecord.get("Name (click to view profile)");
            date = DATE_FORMAT.parse(csvRecord.get("Date"));
            likes = Integer.parseInt(csvRecord.get("Likes"));
            text = csvRecord.get("Comment");
            emotion = "Positive";
            // link = csvRecord.get("Link");
        }
    }
}
