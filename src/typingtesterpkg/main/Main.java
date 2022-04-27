package typingtesterpkg.main;

import java.util.Scanner;

/**
 * Very primitive program that tests typing speed.
 * Bridges class between the terminal/ what the user sees and code/ entrance point via main method.
 * Command prompt --> directory should be from 'src' folder
 *
 * @author Justin Kocur
 */
public class Main {

    /**
     * The initial prompt the user sees to get started
     *
     * @param scanner   the Scanner instance to get user input
     */
    private static void initialPrompt(Scanner scanner) {
        System.out.println("Welcome to Typing Tester!  This program is a very rudimentary way to test typing speed\n" +
                "from a terminal.  This method should provide a rough estimate for this measurement.  To begin,\n" +
                "please type in a number (and ONLY a number)from the options listed to get started...");

        boolean quit = false;

        while (!quit) { // should just keep running until user quits
            System.out.println("\n\t1) Typing Test\n" +
                    "\t2) Practice Test\n" +
                    "\t3) Help\n" +
                    "\t4) Credits\n" +
                    "\t5) Quit");

            String strInput;
            char userInput;
            System.out.print("\t");
            // Keep cycling the prompt until a valid answer is given
            while ((strInput = scanner.next()).length() <= 0 || (userInput = strInput.charAt(0)) < '1' || userInput > '5') {
                System.out.println("\nINVALID RESPONSE.  PLEASE TYPE IN A NUMBER CORRESPONDING TO ONE OF THE OPTIONS LISTED...");
                System.out.println("\n\t1) Typing Test\n" +
                        "\t2) Practice Test\n" +
                        "\t3) Help\n" +
                        "\t4) Credits\n" +
                        "\t5) Quit");
            }

            switch (userInput) {
                case '1':
                    System.out.println("\nStarting typing test...");
                    new UserPrompt("src/Words.csv", "src/Paragraphs.txt").runTypingTest();
                    break;
                case '2':
                    System.out.println("\nStarting practice test...");
                    new UserPrompt("src/Words.csv", "src/Paragraphs.txt").runPracticeTest();
                    break;
                case '3':
                    System.out.println("\nPress ENTER after each of the following...\n" +
                            "\tType 1 to start a typing test.\n" +
                            "\tType 2 to start a practice test.\n" +
                            "\tType 4 to see the credits.\n" +
                            "\tType 5 and then Type 'Y' (without quotes) to exit.");
                    break;
                case '4':
                    System.out.println("\nMade by Justin with JDK11.");
                    break;
                case '5':
                    System.out.println("\nAre you sure?  Y/N");
                    char c = scanner.next().charAt(0);
                    if (c == 'Y' || c == 'y')
                        quit = true;
            }
        }
    }

    /**
     * Run the program
     *
     * @param args  command line arguments --> not used
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initialPrompt(scanner);
        scanner.close();
    }
}
