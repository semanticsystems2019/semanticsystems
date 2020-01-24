package group18;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Rdf4jHandler implements Tutorial {

    private Repository repo;
    public Map<String, IRI> iris;
    private InstaYoutubeParser instaYoutubeParser = new InstaYoutubeParser();
    private TwitterParser twitterParser = new TwitterParser();
    private RedditParser redditParser;

    public Rdf4jHandler() {
        createRepository();
        iris = Stream.of("createdBy", "hasEmotion", "hasSource", "isCommentOn",
                "isOriginalPosterOfPost", "refersToMovie", "hasDate", "hasId", "hasLikes", "hasRetweet", "hasText",
                "hasTitle", "hasUsername", "Action", "Comment", "Drama", "Emotion", "Horror", "Indifferent",
                "InstaPost", "Instagram", "Kids", "Movie", "Negative", "Positive", "Post", "Reddit", "RedditPost",
                "Romance", "Source", "Tweet", "Twitter", "User", "Youtube", "YoutubePost"
        ).collect(Collectors.toMap(iri -> iri, iri -> repo.getValueFactory().createIRI("http://www.semanticweb.org/group18/movie-ontology#" + iri)));

        // init parser
        try {
            redditParser = new RedditParser(repo, iris);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createRepository() {
        repo = new HTTPRepository(Util.SERVER_URL, "semanticsystems");
        repo.init();
    }

    @Override
    public void createInstances() {
        instaYoutubeParser.parse(repo, iris, "instagram");
        instaYoutubeParser.parse(repo, iris, "youtube");
        twitterParser.initialize(repo, iris);
        redditParser.uploadRedditPosts();
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
    public void loadRdfFile(File inputFile) throws IOException {
        FileReader reader = new FileReader(inputFile);
        Model model = Rio.parse(reader, Util.NS, RDFFormat.TURTLE);
        try (RepositoryConnection conn = repo.getConnection()) {
            conn.add(model);
        }
    }

    @Override
    public void writeRdfFile(String outputFile) throws FileNotFoundException, IOException {

    }
}
