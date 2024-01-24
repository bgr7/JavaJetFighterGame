import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSystem {
	//it is created usersystem to manage user login , register operations.
    private Map<String, User> users;
    private static final String USER_DATA_FILE = "src/userdata.txt";
    private static final String SCOREBOARD_FILE = "src/scoreboard.txt";

    public UserSystem() {
        users = loadUserData();
    }
    // it is created map for each user , I prefer map because there cannot be same username.
    private Map<String, User> loadUserData() {
        Map<String, User> loadedUsers = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    String username = fields[0];
                    String password = fields[1];
                    String realName = fields[2];
                    loadedUsers.put(username, new User(username, password, realName));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedUsers;
    }
    
    //scoreboard takes users username and score type of a ScoreBoard class.
    public List<ScoreBoard> getScoreboard() {
        List<ScoreBoard> scoreboard = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SCOREBOARD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 2) {
                    String username = fields[0];
                    int score = Integer.parseInt(fields[1]);
                    scoreboard.add(new ScoreBoard(username, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scoreboard;
    }

    private void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRealName());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean registerUser(String username, String password, String realName) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password, realName));
            saveUserData();
            return true; 
        } else {
            return false; 
        }
    }
// I also asked user for his/her real name.
    public String getRealName(String username) {
        User user = users.get(username);
        return user != null ? user.getRealName() : null;
    }
    
    void updateScoreboard(String username, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCOREBOARD_FILE, true))) {
            writer.write(username + "," + score);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // I put all exceptions to avoid game crash.
}
