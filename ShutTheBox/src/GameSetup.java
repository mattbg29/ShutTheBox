import java.util.ArrayList;

public class GameSetup {
    private final ArrayList<int[]> choices;
    private final ArrayList<ArrayList<int[]>> wins = new ArrayList<>();
    private final ArrayList<ArrayList<Double>> winOdds = new ArrayList<>();
    private final double[] odds = {1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1};
    private final ArrayList<String> roll = new ArrayList<>();

    GameSetup() {
        BankChoices choices = new BankChoices();
        this.choices = choices.addChoices();
        modifyOdds();
        findWinners();
    }

    // The method is overloaded so that we don't need to call the method originally with
    // all the inputs.  Only 'choices' refers to anything of substance here.
    void findWinners() {
        ArrayList<int[]> winChoices = new ArrayList<>();
        ArrayList<Double> winOdds = new ArrayList<>();
        StringBuilder roll = new StringBuilder();
        findWinners(winChoices, winOdds, choices, roll);
    }

    void findWinners(ArrayList<int[]> winChoices, ArrayList<Double> winOdds,
                     ArrayList<int[]> choices, StringBuilder roll) {

        // Iterate through every possible set of bank choices
        for (int[] choice : choices) {

            // We need to create new arrays for all inputs for each new recursion call,
            // so that when we exit the recursion, the original array is intact
            // Choices2 is created later
            ArrayList<int[]> winChoices2 = new ArrayList<>(winChoices);
            ArrayList<Double> winOdds2 = new ArrayList<>(winOdds);
            StringBuilder roll2 = new StringBuilder(roll);

            winChoices2.add(choice);
            roll2.append(findSum(choice));
            double rollOdds = odds[findSum(choice) - 2];

            // If this is the first roll, add the odds of this roll, else
            // multiply the odds of the last set of rolls by the odds of this roll
            if (winOdds2.size() > 0) {
                double prevOdds = winOdds2.get(winOdds2.size() - 1);
                double newOdds = prevOdds * rollOdds;
                winOdds2.add(newOdds);
            } else {
                winOdds2.add(rollOdds);
            }

            // Add to the new choices2 array only those choices that do not contain
            // any individual choices from the choice made, ie if the choice is 1,2
            // then any choice with a 1 or a 2 gets omitted
            ArrayList<int[]> choices2 = new ArrayList<>();
            for (int[] choiceB : choices) {
                boolean skip = false;
                for (int a : choiceB) {
                    for (int b : choice) {
                        if (a == b) {
                            skip = true;
                            break;
                        }
                    }
                }
                if (!skip) {
                    choices2.add(choiceB);
                }
            }
            // Call this method with this winChoice added, this winOdds added,
            // the narrowed choices selection, and this roll added
            findWinners(winChoices2, winOdds2, choices2, roll2);
        }

        // We drop out of the choice loop once no more choices are left, at which
        // point we need to see how many bank elements we used
        int numUsed = 0;
        for (int[] winChoice : winChoices) {
            numUsed += winChoice.length;
        }

        // If we used 9, then hurray, this was a winning series of rolls.
        // We add this set of choices, this set of odds, and this string of rolls
        // to member variables that will store this info for use in the game
        if (numUsed == 9) {
            wins.add(winChoices);
            this.winOdds.add(winOdds);
            this.roll.add(roll.toString());
        }
    }

    // Because I was too lazy to input the actual fractions at the top
    void modifyOdds() {
        for (int i = 0; i < odds.length; i++) {
            odds[i] *= 1 / (double) 36;
        }
    }

    // To find the sum of the given choice
    int findSum(int[] arr) {
        int sum = 0;
        for (int j : arr) {
            sum += j;
        }
        return sum;
    }

    ArrayList<ArrayList<int[]>> getWins() {
        return wins;
    }

    ArrayList<ArrayList<Double>> getWinOdds() {
        return winOdds;
    }

    ArrayList<String> getRoll() {
        return roll;
    }
}
