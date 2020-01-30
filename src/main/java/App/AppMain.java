package App;
import java.util.Scanner;

public class AppMain {
    static Scanner scanner = new Scanner(System.in);
    static QueryHandler queryHandler = new QueryHandler();

    public static void main(String[] args) {
        while (true){
            String s = scanner.next();

            switch (s) {
                case "help" : help(); break;
                case "quit" : closeProgram(); break;
                case "hype" : queryHandler.hypeIsRealMovies(); break;
                case "lame" : queryHandler.notWorthTheWait(); break;
                case "fame" : queryHandler.theCommonTongue(); break;
                case "pit" : queryHandler.bottomOfThePit(); break;
                default: System.out.println("This is not a command!");
            }
        }
    }

    private static void help() {
        System.out.println("I AM HELPING!");
    }

    private static void closeProgram(){
        scanner.close();
        System.out.println("Bye...");
        System.exit(0);
    }
}
