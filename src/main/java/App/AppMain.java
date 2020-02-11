package App;
import java.util.Scanner;

public class AppMain {
    static Scanner scanner = new Scanner(System.in);
    static QueryHandler queryHandler = new QueryHandler();

    public static void main(String[] args) {
        help();
        while (true){
            String s = scanner.next();

            switch (s) {
                case "help" : help(); break;
                case "quit" : closeProgram(); break;
                case "hype" : pleaseWait(); queryHandler.hypeIsRealMovies(); break;
                case "lame" : pleaseWait(); queryHandler.notWorthTheWait(); break;
                case "fame" : pleaseWait(); queryHandler.theCommonTongue(); break;
                case "pit" : pleaseWait(); queryHandler.bottomOfThePit(); break;
                case "trend" : pleaseWait(); queryHandler.trendingNow(); break;
                default: System.out.println("This is not a command! Type 'help' for a list of commands.");
            }
        }
    }

    private static void help() {
        System.out.println("The following commands are available:\n" +
                "- help .. shows this help message\n" +
                "- hype .. shows movies with the most positive responses\n" +
                "- lame .. shows movies with the most negative responses\n" +
                "- fame .. shows most liked comments\n" +
                "- pit .. shows least liked genres\n" +
                "- trend .. shows movies with the most comments\n" +
                "- quit .. quits the program\n");
    }
    private static void pleaseWait(){ System.out.println("Please wait while the query is processed.."); }

    private static void closeProgram(){
        scanner.close();
        System.out.println("Bye...");
        System.exit(0);
    }
}
