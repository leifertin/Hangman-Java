public class Responses {
    private final String english = "English";
    private final String english_correct = "Correct!";
    private final String english_wrong = "Wrong!";
    private final String english_the_word_was = " The word was ";

    private final String polish_correct = "Prawidłowy!";
    private final String polish_wrong = "Zło!";
    private final String polish_the_word_was = " Słowo było ";


    public String alreadyGuessed(Character letter, String language) {
        if (language.equals(english)) {
            return "You already guessed "
                    .concat(String.valueOf(letter))
                    .concat(".");
        } else {
            return "Już zgadłeś "
                    .concat(String.valueOf(letter))
                    .concat(".");
        }
    }

    public String correctLetter(String language) {
        if (language.equals(english)) {
            return english_correct;
        } else {
            return polish_correct;
        }
    }

    public String wrong(String language) {
        if (language.equals(english)) {
            return english_wrong;
        } else {
            return polish_wrong;
        }
    }

    public String lose(String word, String language) {
        if (language.equals(english)) {
            String english_lose = "You lose!";
            return english_wrong
                    .concat(english_the_word_was)
                    .concat(word)
                    .concat(".\n")
                    .concat(english_lose);
        } else {
            String polish_lose = "Przegrałeś!";
            return polish_wrong
                    .concat(polish_the_word_was)
                    .concat(word)
                    .concat(".\n")
                    .concat(polish_lose);
        }
    }

    public String win(String word, String language) {
        if (language.equals(english)) {
            String english_win = "You win!";
            return english_correct
                    .concat(english_the_word_was)
                    .concat(word)
                    .concat(".\n")
                    .concat(english_win);
        } else {
            String polish_win = "Wygrałeś!";
            return polish_correct
                    .concat(polish_the_word_was)
                    .concat(word)
                    .concat(".\n")
                    .concat(polish_win);
        }
    }
}
