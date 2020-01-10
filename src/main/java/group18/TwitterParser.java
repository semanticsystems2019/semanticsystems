package group18;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.noggit.JSONUtil;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 *	?tweet a movies:Tweet ;
 * 		movies:__-_id ?id ;
 * 		movies:__-_text ?text ;
 * 		movies:__-_lang ?lang ;
 * 		movies:__-_source ?source ;
 * 		movies:__-_created_at ?created_at ;
 * 		movies:__-_retweet_count ?retweet_count ;
 * 		movies:__-_favorite_count ?favorite_count ;
 * 		movies:__-_in_reply_to_user_id ?in_reply_to_user_id ;
 * 		movies:__-_in_reply_to_status_id ?in_reply_to_status_id ;
 * 		movies:__-_in_reply_to_user_id_str ?in_reply_to_user_id_str ;
 * 		movies:__-_in_reply_to_screen_name ?in_reply_to_screen_name ;
 * 		movies:__-_user_-_id ?user_id ;
 * 		movies:__-_user_-_screen_name ?user_name .
 * */

public class TwitterParser{

    public JSONArray parser() throws IOException, ParseException {

        JSONArray ret = new JSONArray();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("twitter/all.json"));
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
            reducedJSON.put("emotion", simpleEmotionResolver());

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

    private String simpleEmotionResolver(){
        Random rnd = new Random();
        int c = rnd.nextInt(3);
        if (c == 0)
            return "Positive";
        else if (c == 1)
            return "Indifferent";
        else if (c == 2)
            return "Negative";
        else
            return "";
    }

    public void initialize(Repository repo, Model model) {
        JSONArray tweets;
        ValueFactory valueFactory = repo.getValueFactory();
        Value commentType = model.objects().stream().filter(o -> o.stringValue().contains("Post")).findFirst().get();
        int counter = 0;

        try {
            tweets = parser();
            System.out.println(tweets.toString());
            RepositoryConnection conn = repo.getConnection();
            for (int i = 0; i < tweets.length(); i++) {
                IRI commentIri = valueFactory.createIRI(Util.NS, "post" + (counter+i));

                conn.add(commentIri, RDF.TYPE, commentType);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}