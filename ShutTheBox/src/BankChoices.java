import java.util.ArrayList;

public class BankChoices {
    // A clunky method of identifying all possible choices
    // This should be dynamic if it is meant to be used for more programs,
    // or if ShutTheBox is meant to have a flexible bank, but the code
    // is currently somewhat hard-code for exactly the number needed
    // for the current version, 1 through 9 in the bank with two dice.
    ArrayList<int[]> addChoices() {
        ArrayList<int[]> choices = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            int[] arr1 = new int[1];
            if (i > 1) {
                arr1[0] = i;
                choices.add(arr1);
            }
            for (int j = i + 1; j <= 9; j++) {
                if (i + j < 13) {
                    int[] arr2 = new int[2];
                    arr2[0] = i;
                    arr2[1] = j;
                    choices.add(arr2);
                } else {
                    j = 10;
                }
                for (int k = j + 1; k <= 9; k++) {
                    if (i + j + k < 13) {
                        int[] arr3 = new int[3];
                        arr3[0] = i;
                        arr3[1] = j;
                        arr3[2] = k;
                        choices.add(arr3);
                    } else {
                        k = 10;
                    }
                    for (int a = k + 1; a <= 9; a++) {
                        if (i + j + k + a < 13) {
                            int[] arr4 = new int[4];
                            arr4[0] = i;
                            arr4[1] = j;
                            arr4[2] = k;
                            arr4[3] = a;
                            choices.add(arr4);
                        } else {
                            a = 10;
                        }
                    }
                }
            }
        }
        return choices;
    }
}
