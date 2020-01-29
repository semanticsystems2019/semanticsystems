package App;

public class QueryHandler {

    private Object doQuery(String sparql) {
        //TODO insert RD4JHandler magic
        //TODO define return type!
        return null;
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
        String sparqlQuery = "empty";
        Object obj = doQuery(sparqlQuery);

        //TODO MANUALLY parse obj to String and print
        System.out.println("bottomOfThePit is empty");
    }
}
