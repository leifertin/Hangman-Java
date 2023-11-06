public class Prompts {
    private final String english = "English";

    public String enterWord(String language) {
        if (language.equals(english)) {
            return "Player 1, please enter your word or phrase:";
        } else {
            return "Graczu 1, wpisz swoje słowo lub zdanie:";
        }
    }

    public String guessLetter(String language) {
        if (language.equals(english)) {
            return "Guess a letter:";
        } else {
            return "Zgadnij list:";
        }
    }

    public String guessWord(String language) {
        if (language.equals(english)) {
            return "Guess the word or phrase:";
        } else {
            return "Zgadnij swoje słowo lub zdanie:";
        }
    }

    public String howManyPlayers(String language) {
        if (language.equals(english)) {
            return "1 or 2 players?";
        } else {
            return "1 czy 2 graczy?";
        }
    }

    public String playerTwoStart(String language) {
        if (language.equals(english)) {
            return "Player 2 - Your turn.";
        } else {
            return "Gracz 2, twoja kolej.";
        }
    }
}
