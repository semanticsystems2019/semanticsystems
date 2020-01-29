package group18;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.json.JSONObject;

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

    public void parse(Repository repo, Repository wikiRepo, Map<String, IRI> iris, String source) {
        ValueFactory localValueFactory = repo.getValueFactory();
        ValueFactory wikiValueFactory = wikiRepo.getValueFactory();
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

        JSONObject ids = Util.loadJSON(Util.MOVIENAMES);

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
                    if(counter > 5){
                        break;
                    }
                    String idString = csvRecord.get("Id");
                    if (idString != null && idString.length() > 0) {
                        try (RepositoryConnection conn = repo.getConnection()) {
                            InstaYoutubeComment comment = new InstaYoutubeComment(csvRecord);
                            IRI commentIri = localValueFactory.createIRI(Util.NS, source + "/comment#" + counter);
                            conn.add(commentIri, RDF.TYPE, iris.get("Comment"));

                            // Object properties
                            conn.add(commentIri, iris.get("createdBy"), localValueFactory.createLiteral(comment.name));
                            conn.add(commentIri, iris.get("hasEmotion"), localValueFactory.createIRI(iris.get("Emotion") + "/" + Util.simpleEmotionResolver()));
                            conn.add(commentIri, iris.get("hasSource"), localValueFactory.createLiteral(source));
                            conn.add(commentIri, iris.get("refersToMovie"), wikiValueFactory.createIRI(ids.get(movieNames.get(csvFile.getName())).toString()));

                            // Data properties
                            conn.add(commentIri, iris.get("hasId"), localValueFactory.createLiteral(comment.id));
                            conn.add(commentIri, iris.get("hasDate"), localValueFactory.createLiteral(comment.date));
                            conn.add(commentIri, iris.get("hasLikes"), localValueFactory.createLiteral(comment.likes));
                            conn.add(commentIri, iris.get("hasText"), localValueFactory.createLiteral(comment.text));
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
