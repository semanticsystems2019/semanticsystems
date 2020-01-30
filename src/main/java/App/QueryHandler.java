package App;

import group18.Rdf4jHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryHandler {

    Rdf4jHandler rdf4jHandler = new Rdf4jHandler();

    private QueryResponse doQuery(String sparqlFile) {
        return rdf4jHandler.selectQuery(sparqlFile);
    }

    /*
    Returns Movies with mostly positive responses.
    mostly = 60% likes;
     */
    public void hypeIsRealMovies() {
        String sparqlQuery = "empty";
        Object obj = doQuery(sparqlQuery);

        //TODO MANUALLY parse obj to String and print
        System.out.println("hypeIsRealMovies is empty");
    }

    /*
    Returns Movies with mostly negative responses
    mostly = 60% dislikes
     */
    public void notWorthTheWait() {
        String sparqlQuery = "empty";
        Object obj = doQuery(sparqlQuery);

        //TODO MANUALLY parse obj to String and print
        System.out.println("notWorthTheWait is empty");
    }

    /*
    Comments that received mostly positive feedback
    mostly = ??? danke oli
     */
    public void theCommonTongue() {
        String sparqlQuery = "empty";
        Object obj = doQuery(sparqlQuery);

        //TODO MANUALLY parse obj to String and print
        System.out.println("theCommonTongue is empty");
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
}
