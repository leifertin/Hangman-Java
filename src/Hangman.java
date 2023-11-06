import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.List;

public class Hangman {
    public static void main(String[] args) throws FileNotFoundException {
        // Get relative path
        String RELATIVE_PATH = new File(".").getAbsolutePath();
        RELATIVE_PATH = RELATIVE_PATH.substring(0, RELATIVE_PATH.length() - 1);

        // Init classes
        Responses respond = new Responses();
        Prompts prompt = new Prompts();

        // Init hangman
        int wrong_count = 0;
        List<String> hangman_body_full = List.of(" O", "\\ ", "/", " |", "/ ", "\\");
        int guesses_allowed = hangman_body_full.size();

        // Prepare for user input
        Scanner user_input = new Scanner(System.in);

        // Choose language
        String language = null;
        while (language == null) {
            language = chooseLanguage(user_input, language);
        }

        // Choose word
        String word = null;
        while (word == null) {
            word = choosePlayers(RELATIVE_PATH, user_input, language, word);
        }

        List<Character> player_guesses = new ArrayList<>();

        while (wrong_count < guesses_allowed + 1) {

            printHangman(wrong_count, hangman_body_full);

            if (wrong_count == 6) {
                System.out.println(respond.lose(word, language));
                break;
            }

            printWordState(word, player_guesses);
            String guess = getPlayerGuess(user_input, player_guesses, language);

            if (word.toLowerCase().contains(guess)) {
                System.out.println(respond.correctLetter(language));

            } else {
                wrong_count++;
                System.out.println(respond.wrong(language));
            }

            if (printWordState(word, player_guesses)) {
                System.out.println(respond.win(word, language));
                break;
            }

            System.out.println(prompt.guessWord(language));

            if (user_input.nextLine().equalsIgnoreCase(word)) {
                System.out.println(respond.win(word, language));
                break;
            } else {
                System.out.println(respond.wrong(language));
            }
        }
        user_input.close();
    }

    private static String chooseLanguage(Scanner user_input, String language) {
        System.out.println("Which language?\nType 1 for English or 2 for Polish");
        String language_input = user_input.nextLine();

        if (language_input.equals("1")) {
            language = "English";

        } else if (language_input.equals("2")) {
            language = "Polish";
        }

        return language;
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

    private static String choosePlayers(String RELATIVE_PATH, Scanner user_input, String language, String word) throws FileNotFoundException {
        Prompts prompt = new Prompts();

        System.out.println(prompt.howManyPlayers(language));
        String amount_of_players = user_input.nextLine();

        if (amount_of_players.equals("1")) {
            List<String> words = generateWordsList(RELATIVE_PATH, language.toLowerCase());
            Random random = new Random();
            word = words.get(random.nextInt(words.size()));

        } else if (amount_of_players.equals("2")) {
            System.out.println(prompt.enterWord(language));
            word = user_input.nextLine();
            System.out.println(new String(new char[50]).replace("\0", "\r\n"));
            System.out.println(prompt.playerTwoStart(language));
        }

        if (word != null) {
            word = getUTF8(word);
        }

        return word;
    }

    private static String getUTF8(String word) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(word);
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    private static String getPlayerGuess(Scanner user_input, List<Character> player_guesses, String language) {
        Prompts prompt = new Prompts();
        Responses respond = new Responses();

        System.out.println(prompt.guessLetter(language));
        String letter_guess_line = user_input.nextLine();

        Character letter_guess = letter_guess_line.toLowerCase().charAt(0);

        if (player_guesses.contains(letter_guess)) {
            System.out.println(respond.alreadyGuessed(letter_guess, language));
        } else {
            player_guesses.add(letter_guess_line.toLowerCase().charAt(0));
        }

        return letter_guess_line;
    }

    private static void printHangman(Integer wrong_count, List<String> hangman_body_full) {
        String hangman_header = " -------\n |     |";
        System.out.println(hangman_header);

        for (int i = 0; i < wrong_count; i++) {
            System.out.print(hangman_body_full.get(i));

            if (i == 0 || i == 2 || i == 3) {
                System.out.println();
            }
        }

        System.out.println();
    }

    private static boolean printWordState(String word, List<Character> player_guesses) {
        int correct_count = 0;

        for (int i = 0; i < word.length(); i++) {
            if (player_guesses.contains(word.toLowerCase().charAt(i))) {
                System.out.print(word.charAt(i));
                correct_count++;
            } else {
                System.out.print("-");
            }
        }

        System.out.println();

        return (word.length() == correct_count);
    }

}