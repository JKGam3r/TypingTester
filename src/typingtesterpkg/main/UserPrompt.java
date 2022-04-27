package typingtesterpkg.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class is responsible for interacting with the user and indicating what they should type
 *
 * @author Justin Kocur
 */
public class UserPrompt {
    // The number of correct words the user has achieved
    private int correctWords;

    // Paragraph that the user should type for the typing test
    private String paragraph;

    // Gets a whole paragraph from a .csv file for the typing test
    private Scanner paragraphScanner;

    // Used for parsing user-generated Strings and responding
    private final Scanner scanner;

    // The total number of words gone through
    private int totalWords;

    // List of all words for the practice test
    private final String[] words;

    // Gets words in a .csv file for the practice test
    private Scanner wordScanner;

    /**
     *  Initializes the scanners and word array
     *
     * @param wordPath          file path to reach the document containing the list of words
     *                          for the practice test
     * @param paragraphPath     file path to reach teh document containing the list of paragraphs
     *                          for the typing test
     */
    public UserPrompt(String wordPath, String paragraphPath)  {
        scanner = new Scanner(System.in);
        try {
            wordScanner = new Scanner(new File(wordPath));
            wordScanner.useDelimiter(",");

            paragraphScanner = new Scanner(new File(paragraphPath));
            paragraphScanner.useDelimiter(";");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        words   = new String[10_000];
    }

    /**
     * Runs a practice version of the (actual) typing test that the user can use to "warm-up"
     */
    public void runPracticeTest() {
        addWordsToPracticeTest();

        System.out.println("Directions: Type the word that appears.  After typing a word\n" +
                "press ENTER to get the next word to type.  The practice run ends when\n" +
                "you type 'quit1' (without the quotes).\n" +
                "\n" +
                "Press ENTER to start.");
        scanner.nextLine();

        String correctWord  = words[(int) (Math.random() * words.length)];

        String userWord = "";
        while(!userWord.toLowerCase().equals("quit1")) {
            System.out.println(correctWord);
            userWord = scanner.next();

            if(userWord.equals(correctWord))
                correctWords++;

            correctWord  = words[(int) (Math.random() * words.length)];

            if(!userWord.toLowerCase().equals("quit1"))
                totalWords++;
        }

        System.out.printf("Correct Words: %d%nTotal Words: %d%nAccuracy: %.2f%%%n",
                correctWords, totalWords, ((float) correctWords / totalWords) * 100);
    }

    /**
     * Allows the user to type a paragraph and receive feedback (WORDS PER MINUTE and ACCURACY)
     */
    public void runTypingTest() {
        addParagraphToTypingTest();

        System.out.print("Directions: You will receive a paragraph.  Type all of the words in the paragraph.\n" +
                "When you are finished or decide to quit, press ENTER to see your results.\n" +
                "\nPress ENTER to start.");
        scanner.nextLine();

        System.out.println(paragraph);
        // time the timer started
        final long START_TIME = System.currentTimeMillis();
        String results = scanner.nextLine();
        // amount of time in seconds
        final long TOTAL_TIME = (System.currentTimeMillis() - START_TIME) / 1_000;

        System.out.println("\nAnalyzing results...");
        // Find the number of correct words
        int[] a = accuracyCalculator(paragraph, results);

        // in case the user didn't type all of the words (should have no effect (value of 1) if all words typed)
        if(a[0] <= 0) {
            System.out.println("Words per Minute: N/A");
            return;
        }
        int multiple = a[1] / a[0];
        float accuracy = (float) a[0] / a[1];
        int wpm = (int) (((a[1] * accuracy) / (TOTAL_TIME * multiple)) * 60);
        System.out.println("Words per Minute: " + wpm + " WPM");
        System.out.printf("Accuracy: %.2f%%%n", accuracy * 100);
    }

    /**
     *  Computes the number of correct words the user got right, along with the total number of words
     *
     * @param blueprintString       the prompt paragraph that the user used for typing
     * @param userString            the user's version of the paragraph to check for typing speed and accuracy
     * @return                      an array with two elements, the first element being the number of
     *                              correct words, the second value being the total number of words
     */
    private int[] accuracyCalculator(String blueprintString, String userString) {
        int[] a = new int[2];

        // Words based PURELY on spaces...
        Scanner bpSc = new Scanner(blueprintString);
        bpSc.useDelimiter(" ");

        Scanner uSc = new Scanner(userString);
        uSc.useDelimiter(" ");

        // Compare each word
        while(bpSc.hasNext()) {
            // if uSc is ONLY A SPACE, keep advancing it, and it only until another word or the end is reached
            if(uSc.hasNext()) {
                String s2 = uSc.next().strip();
                if(s2.length() > 0 && !s2.equals(" ")) { // user might have multiple spaces between words
                    String s1 = bpSc.next().strip();
                    if (s1.equals(s2))
                        a[0]++;
                }
            } else
                bpSc.next(); // no more user words; simply advance next() to naturally stop loop and increment total words

            a[1]++;
        }

        return a; // [correct words, total words]
    }

    /**
     * Takes the list of paragraphs from the file and chooses a random one for the user to type
     */
    private void addParagraphToTypingTest() {
        // we will use a random text
        int randomParaNumber = (int) (Math.random() * 10);
        int iteration = 1;

        // Keep iterating through the paragraphs until we get to the desired one
        while(iteration++ < randomParaNumber)
            paragraphScanner.next();

        paragraph = paragraphScanner.next();
    }

    /**
     * Takes the list of words from the file and adds them to the word array
     */
    private void addWordsToPracticeTest() {
        int index = 0;
        while(wordScanner.hasNext() && index < words.length) {
            words[index] = wordScanner.next();
            index++;
        }
        wordScanner.close();
    }
}
