import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String realName;

    public User(String username, String password, String realName) {
        this.username = username;
        this.password = password;
        this.realName = realName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRealName() {
        return realName;
    }
}
// user class is created to use in scoreboard , login and register sections.
