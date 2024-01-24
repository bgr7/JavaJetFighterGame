import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.Iterator;

// I created maingame class which includes Jframe properties and also it includes listeners etc.
public class MainGame extends JFrame implements ActionListener, KeyListener,Runnable {
	private UserSystem userSystem = new UserSystem();
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private int score = 0;
    private int health = 4;
    private int expX;
    private int expY;
    private Thread maingameThread;
    private boolean isGameRunning = false;
    private boolean showIntroImage = true;
    

    private List<Enemy> Enemies;
    private List<Bullet> bullets;
   
    
    private ImageIcon playerImage;
    private Image backgroundImage;
    private ImageIcon explosionImage;
    
    private int playerX, playerY;
    private boolean explosionstate=false;

    private RenderingPart renders;

    private String currentUser;
    private Timer timer;

    public MainGame() {
        setTitle("BGR's Jet Fighter Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        maingameThread = new Thread(this);
        initializeMenu();

        Enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        playerImage = new ImageIcon(getClass().getResource("p_ani.gif"));
        backgroundImage = new ImageIcon(getClass().getResource("arkaplanson.gif")).getImage();
        explosionImage = new ImageIcon(getClass().getResource("PLANE-EXPLOSION-EXP-PSD.gif"));
        playerX = WIDTH / 2 - 25;
        playerY = HEIGHT - 150;
        renders = new RenderingPart();
        add(renders);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        requestFocus();
    }
    // Start method is created to start game and its thread after reset the game.

    private void startGame() {
        resetGame();
        if (!maingameThread.isAlive()) {
            isGameRunning = true;
            maingameThread.start();
        }
    }

// I created that method to make enemy shoot less frequently.
    
    private void enemyShooting() {
        for (Enemy enemy : Enemies) {
            if (Math.random() < 0.001) {  
                enemy.shootBullet();
            }
        }
    }
    
// all menu items initialized here.
      private void initializeMenu() {
          JMenuBar menuBar = new JMenuBar();

          
          JMenu fileMenu = new JMenu("File");
          JMenuItem registerItem = new JMenuItem("Register");
          JMenuItem loginItem = new JMenuItem("Login");
          JMenuItem playGameItem = new JMenuItem("Play Game");
          fileMenu.add(playGameItem);
          JMenuItem scoreTableItem = new JMenuItem("Score Table");
          JMenuItem exitItem = new JMenuItem("Exit");

          
          JMenu helpMenu = new JMenu("Help");
          JMenuItem aboutItem = new JMenuItem("About");

          
          fileMenu.add(registerItem);
          fileMenu.add(loginItem);
          fileMenu.add(playGameItem);
          fileMenu.add(scoreTableItem);
          fileMenu.add(exitItem);

          
          helpMenu.add(aboutItem);

          
          menuBar.add(fileMenu);
          menuBar.add(helpMenu);

         
          setJMenuBar(menuBar);
          
          // listeners starts here . All the user interactions understood here.

          playGameItem.addActionListener(new ActionListener() {
        	    @Override
        	    public void actionPerformed(ActionEvent e) {
        	    	showIntroImage=false;
        	    	
        	    	if (currentUser == null) {
        	            LoginMenu();
        	            
        	        } else {
        	            timer.start(); 
        	            startGame(); 
        	        }
        	    }
        	});


          
          registerItem.addActionListener(new ActionListener() {
        	    @Override
        	    public void actionPerformed(ActionEvent e) {
        	        if (currentUser == null) {
        	            RegisterMenu();
        	        } else {
        	            JOptionPane.showMessageDialog(MainGame.this, "You are already logged in as " + currentUser);
        	        }
        	    }
        	});

          loginItem.addActionListener(new ActionListener() {
        	    @Override
        	    public void actionPerformed(ActionEvent e) {
        	        if (currentUser == null) {
        	            LoginMenu();
        	            
        	        } else {
        	            JOptionPane.showMessageDialog(MainGame.this, "You are already logged in as " + currentUser);
        	        }
        	    }
        	});

         

          scoreTableItem.addActionListener(new ActionListener() {
        	  @Override
              public void actionPerformed(ActionEvent e) {
                  showScoreTable();
              }
          });

          exitItem.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  System.exit(0);
              }
          });

          aboutItem.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  JOptionPane.showMessageDialog(MainGame.this, "Buğra Baygül");
              }
          });
      }
      private void showScoreTable() {
    	    List<ScoreBoard> scoreboard = userSystem.getScoreboard();
    	    //sort method is used here to compare high scores and display just top 10.
    	    
    	    scoreboard.sort(Comparator.comparingInt(ScoreBoard::getScore).reversed());
    	    

    	    int numScoresToDisplay = Math.min(scoreboard.size(), 10);
    	    StringBuilder scoreboardText = new StringBuilder("Top 10 High Scores:\n");

    	    for (int i = 0; i < numScoresToDisplay; i++) {
    	        ScoreBoard entry = scoreboard.get(i);
    	        scoreboardText.append(i + 1).append(". ").append(entry.getUsername()).append(": ").append(entry.getScore()).append("\n");
    	    }

    	    JOptionPane.showMessageDialog(this, scoreboardText.toString(), "Score Table", JOptionPane.INFORMATION_MESSAGE);
    	}

      
      private void RegisterMenu() {
    	    JFrame registerFrame = new JFrame("Register");

    	    JPanel panel = new JPanel();
    	    panel.setLayout(new GridLayout(3, 2));

    	    JTextField usernameField = new JTextField();
    	    JPasswordField passwordField = new JPasswordField();
    	    JTextField realNameField = new JTextField();

    	    panel.add(new JLabel("Username:"));
    	    panel.add(usernameField);
    	    panel.add(new JLabel("Password:"));
    	    panel.add(passwordField);
    	    panel.add(new JLabel("Real Name:"));
    	    panel.add(realNameField);

    	    int result = JOptionPane.showConfirmDialog(
    	            registerFrame, panel, "Register", JOptionPane.OK_CANCEL_OPTION);

    	    if (result == JOptionPane.OK_OPTION) {
    	        String username = usernameField.getText();
    	        String password = new String(passwordField.getPassword());
    	        String realName = realNameField.getText();

    	        if (userSystem.registerUser(username, password, realName)) {
    	            currentUser = username;
    	            JOptionPane.showMessageDialog(registerFrame, "Registration successful. You are now logged in as " + currentUser);
    	            resetGame(); 
    	        } else {
    	        	JOptionPane.showMessageDialog(registerFrame, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
    	        }
    	    }
    	}

// Login menu is created here. While I'm debugging the game I thought that it was good if I have an administor account so I created that Admin login and its own properties.
      private void LoginMenu() {
    	    JFrame loginFrame = new JFrame("Login");

    	    JPanel panel = new JPanel();
    	    panel.setLayout(new GridLayout(3, 2));

    	    JTextField usernameField = new JTextField();
    	    JPasswordField passwordField = new JPasswordField();

    	    panel.add(new JLabel("Username:"));
    	    panel.add(usernameField);
    	    panel.add(new JLabel("Password:"));
    	    panel.add(passwordField);

    	    int result = JOptionPane.showConfirmDialog(
    	            loginFrame, panel, "Login", JOptionPane.OK_CANCEL_OPTION);

    	    if (result == JOptionPane.OK_OPTION) {
    	        String username = usernameField.getText();
    	        String password = new String(passwordField.getPassword());

    	        if (userSystem.loginUser(username, password)) {
    	            currentUser = username;
    	            if (username.equals("admin")) {
    	                JOptionPane.showMessageDialog(loginFrame, "Our great developer has landed! Welcome, [DEV]BUGRA!");
    	                showIntroImage=false;

    	                // when the admin user login different message is displayed. Also the health , size of the plane is differnt. Moreover, Admin uses helicopter with its own name logo.
    	                resetGame();
    	                health = 99999;

    	                
    	                int adminPlayerWidth = 120;  
    	                int adminPlayerHeight = 150; 
    	                playerImage = new ImageIcon(getClass().getResource("admin.gif")); 

    	                
    	                renders.setAdminPlayerSize(adminPlayerWidth, adminPlayerHeight);

    	                
    	            }
    	            
    	            else {
    	            JOptionPane.showMessageDialog(loginFrame, "Succesfully logged in as " + currentUser);
    	            resetGame();
    	            showIntroImage=false;
    	            }
    	            
    	        } else {
    	            JOptionPane.showMessageDialog(loginFrame, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
    	        }
    	    }
    	}

    //reset game method is called when new game starts. It reset timer and clear the screen.
      private void resetGame() {
    	    score = 0;
    	    health = 4;
    	    Enemies.clear();
    	    bullets.clear();

    	    if (timer == null) {
    	        timer = new Timer(15, this);
    	    }

    	    if (timer != null) {
    	        timer.restart();
    	    }

    	    if (currentUser != null) {
    	        timer.start();
    	    }
    	}

  // threads run method created here I tried to handle all exceptions but still sometimes moveenemies and bulletremove methods throws exceptions.
   
      public void run() {
    	    try {
    	        while (isGameRunning && !Thread.interrupted()) {
    	            spawnEnemy();
    	            moveEnemies();
    	            moveBullets();
    	            moveEnemyBullets();
    	            enemyShooting();

    	            for (Enemy enemy : Enemies) {
    	                enemy.setPlayerPosition(playerX, playerY);
    	            }

    	            checkCollisions();
    	            renders.repaint();

    	            try {
    	                Thread.sleep(15);
    	            } catch (InterruptedException e) {
    	                Thread.currentThread().interrupt();
    	            }
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}




     public void actionPerformed(ActionEvent e) {
    	    if (e.getSource() instanceof Timer) {
    	        spawnEnemy();
    	        moveEnemies();
    	        moveBullets();
    	        moveEnemyBullets();
    	        enemyShooting();

    	        
    	        for (Enemy enemy : Enemies) {
    	            enemy.setPlayerPosition(playerX, playerY);
    	        }

    	        checkCollisions();
    	        renders.repaint();
    	    }
    	}
  // actionperformed method is called for timer actions such as updates game state checks collisions etc. after that renderpart repainted.

     private void moveEnemyBullets() {
    	    Iterator<Enemy> iterator = Enemies.iterator();
    	    while (iterator.hasNext()) {
    	        Enemy enemy = iterator.next();
    	        for (EnemyBullet bullet : enemy.getEnemyBullets()) {
    	            bullet.move();
    	        }

    	        if (!enemy.isVisible) {
    	            for (EnemyBullet bullet : enemy.getEnemyBullets()) {
    	                bullet.isVisible = false;
    	            }
    	            iterator.remove();
    	        }
    	    }
    	}

    // moveenemybullet method acts enemies bullets according to its features.


    private void spawnEnemy() {
        Random random = new Random();
        if (random.nextInt(200) < 5) {
            int randomX = random.nextInt(WIDTH - 30);
            int randomY = 0;
            
           
            while (enemyOverlaps(randomX, randomY)) {
                randomX = random.nextInt(WIDTH - 30);
            }

            Enemy enemy = new Enemy(randomX, randomY, 50,50, playerX, playerY);
            Enemies.add(enemy);
        }
    }
    //that method spawns enemy in random x places at the top of the screen.

    private boolean enemyOverlaps(int x, int y) {
        for (Enemy existingEnemy : Enemies) {
            int distance = (int) Math.sqrt(Math.pow(x - existingEnemy.x, 2) + Math.pow(y - existingEnemy.y, 2));
            if (distance < 50) { 
                return true;
            }
        }
        return false;
    }
// I create that method to prevent enemy overlaps when spawning and following the player's plane.

    private void moveEnemies() {
        for (Enemy enemy : Enemies) {
            if (enemy.getEnemyThread() == null || !enemy.getEnemyThread().isAlive()) {
                Thread newEnemyThread = new Thread(enemy);
                enemy.setEnemyThread(newEnemyThread);
                newEnemyThread.start();
               // enemy.move();
            }
        }
    }
    

    private void moveBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.move();
            if (!bullet.isVisible) {
                iterator.remove();
            }
        }
    }
    
   // The most important function is check collision function since it handle all the player hits , enemy hits and collision between enenmy plane and player's plane.

    private void checkCollisions() {
        Rectangle playerRect = new Rectangle(playerX, playerY, 50, 80);
// first of all I tried to handle collision by just checking x and y coordinates after that I realized drawing a boundary is more optimized solution for that method.
        
        Iterator<Enemy> enemyIterator = Enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            Rectangle enemyRect = new Rectangle(enemy.x, enemy.y, enemy.width, enemy.height);

            if (playerRect.intersects(enemyRect) && enemy.isVisible) {
                handlePlayerHit();
                handlePlayerHit();
                enemy.isVisible = false;
                enemyIterator.remove(); 
                expX = enemy.x;
                expY = enemy.y;
                explosionstate = true;
            }
            
            //intersects method direct do the work that I desire.

            
            Iterator<EnemyBullet> bulletIterator = enemy.getEnemyBullets().iterator();
            while (bulletIterator.hasNext()) {
                EnemyBullet bullet = bulletIterator.next();
                Rectangle bulletRect = new Rectangle(bullet.x, bullet.y, bullet.width, bullet.height);

                if (bulletRect.intersects(playerRect) && bullet.isVisible) {
                    handlePlayerHit();
                    bullet.isVisible = false;
                    bulletIterator.remove(); 
                    expX = playerX;
                    expY = playerY;
                    explosionstate = true;
                }
            }
        }
        
        // Although all the code is seems correct sometimes game doesn't recognize enemy bullet hits the player. Probably that problem causes on threads.

        
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Rectangle bulletRect = new Rectangle(bullet.x, bullet.y, bullet.width, bullet.height);

            
            Iterator<Enemy> enemyIterator2 = Enemies.iterator();
            while (enemyIterator2.hasNext()) {
                Enemy enemy = enemyIterator2.next();
                Rectangle enemyRect = new Rectangle(enemy.x, enemy.y, enemy.width, enemy.height);

                if (bulletRect.intersects(enemyRect) && bullet.isVisible && enemy.isVisible) {
                    handleBulletHit(bullet, enemy);
                    bulletIterator.remove(); 
                    enemy.isVisible = false;
                    expX = enemy.x;
                    expY = enemy.y;
                    explosionstate = true;
                }
            }
        }
    }




    private void handlePlayerHit() {
        health--;  
        if (health == 0) {
        	
            endGame(); 
           
           // that method is called when the collision occurs on player's plane. It just decreases player's health. 
        }
        
    }

    private void handleBulletHit(Bullet bullet, Enemy Enemy) {
        score++;  
        
    }
    
    //when player hit the enemy, score is increased.

    private void endGame() {
        JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + score, "Game Over", JOptionPane.ERROR_MESSAGE);

        if (currentUser != null) {
            userSystem.updateScoreboard(currentUser, score);
        }
        isGameRunning = false;
        timer.stop();
        maingameThread.interrupt();
        
        //Enemies.clear();
        //bullets.clear();
       
// endgame method is called when the game ends ( health =  0) and it stop timer so no more enemy spawns and no more score can be obtained.            
    }

 
//Custom JPanel class is created to render the game graphics. It cover some ui elements such as background and explosion gif etc.
    private class RenderingPart extends JPanel {

        private static final long serialVersionUID = 1L;

        private Timer explosionTimer;
        private static final int DELAY_AFTER_EXPLOSION = 1000;
        

        private void drawEnemyBullets(Graphics g) {
            for (Enemy enemy : Enemies) {
                for (EnemyBullet bullet : enemy.getEnemyBullets()) {
                    bullet.draw(g);
                }
            }
        }

        int playerWidth = 80;
        int playerHeight = 100;

        public void setAdminPlayerSize(int adminPlayerWidth, int adminPlayerHeight) {
            playerWidth = adminPlayerWidth;
            playerHeight = adminPlayerHeight;
        }
        
//that method handles painting components such as enemy bullets, player explosion,health and score etc.
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (showIntroImage) {
                ImageIcon introImage = new ImageIcon(getClass().getResource("kapakson.jpg"));
                g.drawImage(introImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                return;
            }

            drawEnemyBullets(g);
            explosionTimer = new Timer(DELAY_AFTER_EXPLOSION, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    explosionstate = false;
                    explosionTimer.stop();
                }
            });
            explosionTimer.setRepeats(false);

            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g.drawImage(playerImage.getImage(), playerX, playerY, playerWidth, playerHeight, this);

            for (Enemy Enemy : Enemies) {
                Enemy.draw(g);
            }

            for (Bullet bullet : bullets) {
                bullet.draw(g);
            }

            drawHealthBar(g);
            drawScore(g);
            setDoubleBuffered(true);

            if (explosionstate) {
                g.drawImage(explosionImage.getImage(), expX, expY, this);
                explosionTimer.start();
            }
        }
        

        private void drawHealthBar(Graphics g) {
            g.setColor(Color.RED);
            int healthBarWidth = health * 30;
            g.fillRect(10, 10, healthBarWidth, 20);
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 120, 20);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("HEALTH ", 25, 28);
        }
        

        private void drawScore(Graphics g) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Score: " + score, getWidth() - 100, 30);
        }
    }



    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int playerSpeed = 40; 

        switch (key) {
            case KeyEvent.VK_LEFT:
                playerX = Math.max(playerX - playerSpeed, 0);
                break;
            case KeyEvent.VK_RIGHT:
                playerX = Math.min(playerX + playerSpeed, WIDTH - 80);
                break;
            case KeyEvent.VK_UP:
                playerY = Math.max(playerY - playerSpeed, 200);
                break;
            case KeyEvent.VK_DOWN:
                playerY = Math.min(playerY + playerSpeed, HEIGHT - 120); 
                break;
            case KeyEvent.VK_SPACE:
                shootBullet();
                break;
        }
        renders.repaint();
    }

    private void shootBullet() {
        Bullet bullet = new Bullet(playerX+40 , playerY, 10, 20); 
        bullets.add(bullet);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGame game = new MainGame();
            game.setVisible(true);
            
        });
    }

// I created that methods automatically because unless I didn't Eclipse give me warnings.
	@Override
	public void keyTyped(KeyEvent e) {		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}
}
