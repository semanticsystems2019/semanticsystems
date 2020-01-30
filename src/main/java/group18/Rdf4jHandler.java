package group18;

import App.QueryResponse;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Rdf4jHandler implements Tutorial {

    private Repository localRepo;
    private Repository wikiRepo;
    public Map<String, IRI> iris;
    private InstaYoutubeParser instaYoutubeParser;
    private TwitterParser twitterParser = new TwitterParser();
    private RedditParser redditParser;

    public Rdf4jHandler() {
        createRepository();
        iris = Stream.of("createdBy", "hasEmotion", "hasSource", "isCommentOn",
                "isOriginalPosterOfPost", "refersToMovie", "hasDate", "hasId", "hasLikes", "hasRetweet", "hasText",
                "hasTitle", "hasUsername", "Action", "Comment", "Drama", "Emotion", "Horror", "Indifferent",
                "InstaPost", "Instagram", "Kids", "Movie", "Negative", "Positive", "Post", "Reddit", "RedditPost",
                "Romance", "Source", "Tweet", "Twitter", "User", "Youtube", "YoutubePost"
        ).collect(Collectors.toMap(iri -> iri, iri -> localRepo.getValueFactory().createIRI("http://www.semanticweb.org/group18/movie-ontology#" + iri)));

        instaYoutubeParser = new InstaYoutubeParser(localRepo, wikiRepo, iris);
        try (RepositoryConnection conn = localRepo.getConnection()) {
            IRI emotionIri = localRepo.getValueFactory().createIRI(iris.get("Emotion") + "/Positive");
            conn.add(emotionIri, RDF.TYPE, iris.get("Emotion"));

            emotionIri = localRepo.getValueFactory().createIRI(iris.get("Emotion") + "/Negative");
            conn.add(emotionIri, RDF.TYPE, iris.get("Emotion"));

            emotionIri = localRepo.getValueFactory().createIRI(iris.get("Emotion") + "/Indifferent");
            conn.add(emotionIri, RDF.TYPE, iris.get("Emotion"));
        }

        // init parser
        try {
            redditParser = new RedditParser(localRepo, iris);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createRepository() {
        localRepo = new HTTPRepository(Util.SERVER_URL, "semanticsystems");
//        localRepo = new SPARQLRepository("http://localhost:7200/sparql");
        localRepo.init();
        wikiRepo = new SPARQLRepository("https://query.wikidata.org/sparql");
        wikiRepo.init();
    }

    @Override
    public void createInstances() {
        instaYoutubeParser.parse("instagram");
        instaYoutubeParser.parse("youtube");
        twitterParser.initialize(localRepo, iris);
        redditParser.uploadRedditPosts();
    }

    @Override
    public void modifyInstances() {

    }

    @Override
    public void iteratingRdfData() {

    }

    @Override
    public QueryResponse selectQuery(String queryFile) {
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        try (RepositoryConnection conn = localRepo.getConnection()) {
            String queryString = readFile(queryFile, StandardCharsets.UTF_8);
            TupleQuery tupleQuery = conn.prepareTupleQuery(queryString);
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    keys.add(bindingSet.getValue("name").toString());
                    values.add(bindingSet.getValue("ratio").toString());
                }
            }
        }
        return new QueryResponse(keys, values);
    }

    @Override
    public void constructQuery(String queryFile) {

    }

    private String readFile(String queryFile, Charset encoding) {
        String content = "";
        try {
            Path path = Paths.get(this.getClass().getClassLoader().getResource("queries/" + queryFile).toURI());
            content = new String(Files.readAllBytes(path), encoding);
        }
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public void loadRdfFile(File inputFile) throws IOException {
        FileReader reader = new FileReader(inputFile);
        Model model = Rio.parse(reader, Util.NS, RDFFormat.TURTLE);
        try (RepositoryConnection conn = localRepo.getConnection()) {
            conn.add(model);
        }
    }

    @Override
    public void writeRdfFile(String outputFile) throws FileNotFoundException, IOException {

    }
}
