import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.List;

public class Hangman {
    // Init hangman
    public static int wrong_count = 0;
    public static List<String> hangman_body_full = List.of(" O", "\\ ", "/", " |", "/ ", "\\");
    public static int guesses_allowed = hangman_body_full.size();

    // Init classes
    public static Prompts prompt = new Prompts();
    public static Responses respond = new Responses();

    public static void main(String[] args) throws FileNotFoundException {
        // Get relative path
        String RELATIVE_PATH = new File(".").getAbsolutePath();
        RELATIVE_PATH = RELATIVE_PATH.substring(0, RELATIVE_PATH.length() - 1);

        // Prepare for user input
        Scanner user_input = new Scanner(System.in);

        // Choose language
        String language = null;
        while (language == null) {
            language = chooseLanguage(user_input, language);
        }

        // Choose players
        int amount_of_players = 0;
        while (amount_of_players == 0) {
            amount_of_players = choosePlayers(user_input, language);
        }

        bottomPad(50);

        // Choose word
        String word = null;
        while (word == null) {
            word = chooseWord(RELATIVE_PATH, user_input, amount_of_players, language, word);
        }

        List<Character> player_guesses = new ArrayList<>();

        while (wrong_count < guesses_allowed + 1) {
            if (wrong_count == 6) {
                System.out.println(respond.lose(word, language));
                break;
            }

            // Print current progress
            bottomPad(1);
            printWordState(word, player_guesses, language);

            // Guess letter
            wrong_count = playerGuessLetter(user_input, word, player_guesses, wrong_count, language);

            printHangman(wrong_count, hangman_body_full);
            bottomPad(1);

            if (printWordState(word, player_guesses, language)) {
                System.out.println(respond.win(word, language));
                break;
            }

            // Guess word
            System.out.println(prompt.guessWord(language));

            if (user_input.nextLine().equalsIgnoreCase(word)) {
                System.out.println(respond.win(word, language));
                break;
            } else {
                bottomPad(50);
                System.out.println(respond.wrong(language));
                printHangman(wrong_count, hangman_body_full);

            }

        }
        user_input.close();
    }

    private static void bottomPad(int lines) {
        System.out.println(new String(new char[lines]).replace("\0", "\r\n"));
    }

    private static String chooseLanguage(Scanner user_input, String language) {

        System.out.println("Which language?\nType 1 for English or 2 for Polish");
        String language_input = user_input.nextLine();

        bottomPad(1);

        if (language_input.equals("1")) {
            language = "English";
        } else if (language_input.equals("2")) {
            language = "Polish";
        }

        return language;
    }

    private static int choosePlayers(Scanner user_input, String language) {

        System.out.println(prompt.howManyPlayers(language));
        String amount_of_players = user_input.nextLine();

        int amount_of_players_int = 0;

        if (amount_of_players.equals("1")) {
            amount_of_players_int = 1;
        } else if (amount_of_players.equals("2")) {
            amount_of_players_int = 2;
        }

        return amount_of_players_int;
    }

    private static String chooseWord(
            String RELATIVE_PATH,
            Scanner user_input,
            int amount_of_players,
            String language,
            String word)
            throws FileNotFoundException {

        if (amount_of_players == 1) {
            List<String> words = generateWordsList(RELATIVE_PATH, language.toLowerCase());
            Random random = new Random();
            word = words.get(random.nextInt(words.size()));

        } else if (amount_of_players == 2) {
            System.out.println(prompt.enterWord(language));
            word = user_input.nextLine();

            // Prevent player 2 from cheating :)
            bottomPad(50);
            System.out.println(prompt.playerTwoStart(language));
        }

        if (word != null) {
            word = getUTF8(word);
        }

        return word;
    }

    private static List<String> generateWordsList(String RELATIVE_PATH, String language) throws FileNotFoundException {
        String word_list_path = RELATIVE_PATH
                .concat("words_")
                .concat(language)
                .concat(".txt");

        Scanner file_input = new Scanner(new File(word_list_path));
        List<String> words = new ArrayList<>();
        while (file_input.hasNext()) {
            words.add(file_input.nextLine());
        }

        file_input.close();
        return words;
    }

    private static String getUTF8(String word) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(word);
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    private static int playerGuessLetter(
            Scanner user_input,
            String word,
            List<Character> player_guesses,
            int wrong_count,
            String language) {

        System.out.println(prompt.guessLetter(language));
        String letter_guess_line = user_input.nextLine();

        Character letter_guess = letter_guess_line.toLowerCase().charAt(0);

        bottomPad(50);

        if (player_guesses.contains(letter_guess)) {
            System.out.println(respond.alreadyGuessed(letter_guess, language));
        } else {

            if (word.toLowerCase().contains(letter_guess_line)) {
                System.out.println(respond.correctLetter(language));
            } else {
                wrong_count++;
                System.out.println(respond.wrong(language));
            }

            player_guesses.add(letter_guess_line.toLowerCase().charAt(0));
        }

        return wrong_count;
    }

    private static void printHangman(Integer wrong_count, List<String> hangman_body_full) {
        bottomPad(0);
        String hangman_header = " -------\n |     |";
        System.out.println(hangman_header);

        for (int i = 0; i < wrong_count; i++) {
            System.out.print(hangman_body_full.get(i));

            if (i == 0 || i == 2 || i == 3) {
                bottomPad(0);
            }
        }

    }

    private static boolean printWordState(String word, List<Character> player_guesses, String language) {
        int correct_count = 0;

        System.out.print(prompt.word(language));
        for (int i = 0; i < word.length(); i++) {
            if (player_guesses.contains(word.toLowerCase().charAt(i))) {
                System.out.print(word.charAt(i));
                correct_count++;
            } else {
                System.out.print("-");
            }
        }

        bottomPad(1);

        return (word.length() == correct_count);
    }

}