public class Main {
    public static void main(String[] args) {
        GameSetup game = new GameSetup();
        Play playGame = new Play(game);
        playGame.playGame();
    }
}
