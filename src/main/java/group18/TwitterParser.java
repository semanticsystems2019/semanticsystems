package group18;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


public class TwitterParser{

    public JSONArray parser() throws IOException, ParseException {

        JSONArray ret = new JSONArray();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(getClass().getClassLoader().getResource("twitter/all.json").getPath()));
        JSONArray fullJSON = new JSONArray(obj.toString());

        for (int i = 0; i < fullJSON.length(); i++) {

            JSONObject currJSON = fullJSON.getJSONObject(i);
            JSONObject user = currJSON.getJSONObject("user");
            JSONObject reducedJSON = new JSONObject();

            reducedJSON.put("id", currJSON.get("id_str"));
            reducedJSON.put("text", currJSON.get("text"));
            reducedJSON.put("lang", currJSON.get("lang"));
            reducedJSON.put("source", "twitter");
            reducedJSON.put("user_name", user.get("name"));
            reducedJSON.put("user_id", user.get("id_str"));
            reducedJSON.put("refersTo", movieResolver((String) currJSON.get(("in_reply_to_user_id_str"))));
            reducedJSON.put("emotion", Util.simpleEmotionResolver());
            reducedJSON.put("retweetCount", currJSON.get("retweet_count"));
            reducedJSON.put("favorite_count", currJSON.get("favorite_count"));

            ret.put(reducedJSON);
        }
        return ret;
    }

    private String movieResolver(String movie_id){
        String movieName = "";

        // movie_id translates to "in_reply_to_user_id_str" of original tweet
        if (movie_id.equals("393852070")){
            movieName = "Avengers: Endgame"; }
        else if (movie_id.equals("874401319171178496")) {
            movieName = "Crazy Rich Asians"; }
        else if (movie_id.equals("634706405")) {
            movieName = "Fifty Shades Freed"; }
        else if (movie_id.equals("1038128227485532160")) {
            movieName = "The Joker"; }
        else if (movie_id.equals("20651402")) {
            movieName = "Zombieland: Double Tap"; }

        return movieName;
    }

    public void initialize(Repository repo, Map<String, IRI> iris) {
        JSONArray tweets;
        ValueFactory valueFactory = repo.getValueFactory();
        //Value commentType = model.objects().stream().filter(o -> o.stringValue().contains("Post")).findFirst().get();
        int counter = 0;

        try {
            tweets = parser();
            RepositoryConnection conn = repo.getConnection();
            for (int i = 0; i < tweets.length(); i++) {
                JSONObject thisTweet = tweets.getJSONObject(i);
                IRI tweetIri = valueFactory.createIRI(Util.NS, "twitter/post#" + (counter+i));
                //System.out.println(iris.get("Tweet"));
                conn.add(tweetIri, RDF.TYPE, iris.get("Tweet"));

                conn.add(tweetIri,
                        iris.get("createdBy"),
                        valueFactory.createLiteral( thisTweet.getString("user_name") ));
                conn.add(tweetIri,
                        iris.get("hasEmotion"),
                        valueFactory.createIRI(iris.get("Emotion") + "/" + thisTweet.getString("emotion")));
                conn.add(tweetIri,
                        iris.get("hasSource"),
                        valueFactory.createLiteral(( "Twitter")));
                conn.add(tweetIri,
                        iris.get("refersToMovie"),
                        valueFactory.createLiteral( thisTweet.getString("refersTo")));
                conn.add(tweetIri,
                        iris.get("hasId"),
                        valueFactory.createLiteral( thisTweet.getString("id")));
                conn.add(tweetIri,
                        iris.get("hasText"),
                        valueFactory.createLiteral( thisTweet.getString("text")));
                conn.add(tweetIri,
                        iris.get("hasRetweet"),
                        valueFactory.createLiteral( thisTweet.getInt("retweetCount")));
                conn.add(tweetIri,
                        iris.get("hasLikes"),
                        valueFactory.createLiteral( thisTweet.getInt("favorite_count")));


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}