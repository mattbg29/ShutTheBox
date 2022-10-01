import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Play {
    private final ArrayList<ArrayList<int[]>> wins;
    private final ArrayList<ArrayList<Double>> winOdds;
    private final ArrayList<String> roll;

    Play(GameSetup game) {
        this.wins = game.getWins();
        this.winOdds = game.getWinOdds();
        this.roll = game.getRoll();
    }

    private void printGameOdds() {
        double odds = 0.0;
        HashMap<String, Integer> wins2 = new HashMap<>();
        for (int i = 0; i < wins.size(); i++) {
            int sizeBefore = wins2.size();
            wins2.put(roll.get(i), 1);
            int sizeAfter = wins2.size();
            if (sizeBefore != sizeAfter) {
                odds += winOdds.get(i).get(winOdds.get(i).size() - 1);
            }
        }
        System.out.println("Overall odds of winning: " + odds);
    }

    void playGame() {
        printGameOdds();
        playGame(wins, winOdds, roll);
    }

    void playGame(ArrayList<ArrayList<int[]>> wins,
                  ArrayList<ArrayList<Double>> winOdds, ArrayList<String> rolls) {

        ArrayList<ArrayList<int[]>> winsB = new ArrayList<>();
        ArrayList<ArrayList<Double>> winsOddsB = new ArrayList<>();
        ArrayList<int[]> choices = new ArrayList<>();
        ArrayList<Double> odds = new ArrayList<>();
        ArrayList<String> rollsB = new ArrayList<>();

        Scanner inputReader2 = new Scanner(System.in);
        System.out.println("Please enter a roll 2-12");
        int roll = Integer.parseInt(inputReader2.nextLine());

        HashMap<String, Integer> rollChoices = new HashMap<>();

        // The game works by iterating through every possible winning set of rolls,
        // which gets narrower with each subsequent roll as only those sequences
        // that match the actual rolls are called in subsequent recursions
        for (int i = 0; i < wins.size(); i++) {
            int[] finalChoice = wins.get(i).get(wins.get(i).size() - 1);
            int rollChoice = findSum(finalChoice);

            // ChoiceUnique is necessary to account for all the possible unique sets
            // of rolls given a specific choice.  i.e. if I roll a 3 and can choose
            // (3) or (2,1), there may duplicate winning roll possibilities  for
            // (3) which we want to remove for calculating odds of winning, but
            // we don't want to remove duplicates between (3) and (2,1)
            String choiceUnique = rolls.get(i) + findUnique(finalChoice);

            if (rollChoice == roll) {
                if (wins.get(i).size() > 1) {

                    if (!choices.contains(finalChoice)) {
                        choices.add(finalChoice);
                    }

                    int prevSize = rollChoices.size();
                    rollChoices.put(choiceUnique, 1);
                    int postSize = rollChoices.size();

                    // If rollChoices size has changed, that means we have not seen this
                    // unique string of roll + choice combo, so we want to add the
                    // corresponding odds to the the chance of winning for this choice
                    if (postSize != prevSize) {

                        // The odds associated with a given choice is actually the odds
                        // of rolling that choice, despite the fact that, by the time
                        // we are looking at the choice, it has already been rolled.
                        // This is useful by introducing odds before any roll has been made,
                        // but does mean I need to check for one index beyond the given choice
                        // to get the odds of winning after choosing it, and it means I need
                        // to manually input the true odds when there is only one choice left
                        // and the requisite roll has happened, which are 100%
                        double finalChoiceOdds = winOdds.get(i).get(winOdds.get(i).size() - 2);
                        int index = choices.indexOf(finalChoice);

                        // if there are more choices than odds, then this must be the
                        // first time we have seen this choice, so we can simple add
                        // the corresponding odds.  Else, we add the odds to the existing
                        // odds at said choice index level
                        if (odds.size() < choices.size()) {
                            odds.add(finalChoiceOdds);
                        } else {
                            double oldOdds = odds.get(index);
                            double newOdds = oldOdds + finalChoiceOdds;
                            odds.set(index, newOdds);
                        }
                    }
                } else {
                    // See explanation above
                    choices.add(finalChoice);
                    odds.add(1.0);
                }

                // Here we add to the new arrays only the elements of the initial
                // arrays that are associated with the given roll
                winsB.add(wins.get(i));
                winsOddsB.add(winOdds.get(i));
                rollsB.add(rolls.get(i));
            }
        }

        // If at this point there are no winning combinations of rolls left, you have lost
        if (winsB.size() == 0) {
            System.out.println("Sorry you lost");
        } else {
            System.out.println("Here are your choices / odds of winning: \n");
            for (int i = 0; i < choices.size(); i++) {
                for (int j = 0; j < choices.get(i).length; j++) {
                    System.out.print(choices.get(i)[j]);
                }
                System.out.println(" : " + odds.get(i));
            }
            System.out.println("\nPlease enter your choice");
            Scanner inputReader = new Scanner(System.in);
            String userChoice = inputReader.nextLine();

            // We now create a new set of arrays for recursion that will capture
            // only those outcomes that capture the bank choice made by the user
            ArrayList<ArrayList<int[]>> winsC = new ArrayList<>();
            ArrayList<ArrayList<Double>> winsOddsC = new ArrayList<>();
            ArrayList<String> rollsC = new ArrayList<>();

            boolean stillOptions = false;
            for (int i = 0; i < winsB.size(); i++) {
                int[] finalChoice = winsB.get(i).get(winsB.get(i).size() - 1);
                StringBuilder finalChoiceString = new StringBuilder();
                String[] userChoiceInt = userChoice.split("");

                int userChoiceLength = findSumString(userChoiceInt);

                for (int value : finalChoice) {
                    finalChoiceString.append(value);
                }

                if (finalChoiceString.toString().equals(userChoice)) {
                    ArrayList<int[]> winsToAdd = new ArrayList<>();
                    ArrayList<Double> oddsToAdd = new ArrayList<>();

                    for (int k = 0; k < winsB.get(i).size() - 1; k++) {
                        winsToAdd.add(winsB.get(i).get(k));
                        oddsToAdd.add(winsOddsB.get(i).get(k));
                        stillOptions = true;
                    }

                    String rollsToAdd = "";

                    // The new rolls are simply the old rolls with the last number cut off
                    rollsToAdd = rollsB.get(i).substring(0, rollsB.get(i).length() - userChoiceLength);

                    winsC.add(winsToAdd);
                    winsOddsC.add(oddsToAdd);
                    rollsC.add(rollsToAdd);
                }
            }

            // If after making the choice, there are no more roll combinations available,
            // the user has won
            if (!stillOptions) {
                System.out.println("Congrats, you won");
            } else {
                playGame(winsC, winsOddsC, rollsC);
            }
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

    // To find the length of a given choice
    int findSumString(String[] arr) {
        int sum = 0;
        for (String j : arr) {
            sum += Integer.parseInt(j);
        }
        String sumString = String.valueOf(sum);
        return sumString.length();
    }

    // For identifying unique rolls split up by choice, as explained in the PlayGame method
    String findUnique(int[] arr) {
        StringBuilder sum = new StringBuilder();
        for (int j : arr) {
            sum.append(j);
        }
        return sum.toString();
    }
}
