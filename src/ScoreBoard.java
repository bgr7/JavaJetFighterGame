public class ScoreBoard {
    private String username;
    private int score;

    public ScoreBoard(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return username + "," + score;
    }
}
// scoreboard class is created to store username and score for each user.