package App;

import group18.Rdf4jHandler;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryHandler {

    Rdf4jHandler rdf4jHandler = new Rdf4jHandler();
    DecimalFormat df = new DecimalFormat("#.#");

    private QueryResponse doQuery(String sparqlFile) {
        return rdf4jHandler.selectQuery(sparqlFile);
    }
    
    /*
    Returns Movies with mostly positive responses.
    mostly = 60% likes;
     */
    public void hypeIsRealMovies() {
        String sparqlQuery = "hype_is_real.sparql";
        QueryResponse map = doQuery(sparqlQuery);
    
        for(int i=0; i<map.getLength(); i++){
            System.out.println(map.getKeys().get(i).split("\"")[1] + " - " + df.format(Float.parseFloat(map.getValues().get(i).split("\"")[1])*100) + "% positive");
        }
    }

    /*
    Returns Movies with mostly negative responses
    mostly = 60% dislikes
     */
    public void notWorthTheWait() {
        String sparqlQuery = "not_worth_the_wait.sparql";
        QueryResponse map = doQuery(sparqlQuery);
    
        for(int i=0; i<map.getLength(); i++){
            System.out.println(map.getKeys().get(i).split("\"")[1] + " - " + df.format(Float.parseFloat(map.getValues().get(i).split("\"")[1])*100) + "% negative");
        }
    }

    /*
    Comments that received much feedback
     */
    public void theCommonTongue() {
        String sparqlFile = "common_tongue.sparql";
        HashMap<Integer, HashMap<String, String>> result = rdf4jHandler.tongueSelectQuery(sparqlFile);

        System.out.println("Rank | Likes |   Movie   |   Platform   | User | Text ");
        System.out.println("--- Text ");
        for (int i : result.keySet()) {
            System.out.print(i + ". | ");
            System.out.print( result.get(i).get("likes").split("\"")[1]);
            System.out.print("   |   ");
            System.out.print( result.get(i).get("title").split("\"")[1]);
            System.out.print("   |   ");
            System.out.print( result.get(i).get("source").split("\"")[1]);
            System.out.print("   |   ");
            System.out.print( result.get(i).get("user").split("\"")[1]);
            System.out.println("");
            System.out.print("--- ");
            System.out.print( result.get(i).get("text"));
            System.out.println("");
        }
    }

    /*
    Worst ranking Movie Genres
     */
    public void bottomOfThePit() {
        String sparqlFile = "bottom_of_the_pit.sparql";
        QueryResponse map = doQuery(sparqlFile);

        for(int i=0; i<map.getLength(); i++){
            System.out.println(map.getKeys().get(i).split("\"")[1] + " - " + map.getValues().get(i).split("\"")[1]);
        }
    }

    public void trendingNow() {
        String sparqlFile = "trending.sparql";
        QueryResponse map = doQuery(sparqlFile);

        for(int i=0; i<map.getLength(); i++){
            System.out.println(map.getKeys().get(i).split("\"")[1] + " - " + map.getValues().get(i).split("\"")[1]);
        }
    }
}
