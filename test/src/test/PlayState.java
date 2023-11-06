package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayState extends JPanel {

    private ImageIcon player = new ImageIcon(this.getClass().getResource("Player.png"));
    private ImageIcon enemy = new ImageIcon(this.getClass().getResource("Enemy.png"));
    public ImageIcon background = new ImageIcon(this.getClass().getResource("wallpaper.jpg"));
    private int x = 300;
    private int playerY = 400;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private Random random = new Random();
    private int score = 0;
    private int playerLives = 3; // Number of lives the player starts with

    private boolean isGameOver = false;
    private JButton restartButton;
    private JButton exitButton;

    public PlayState() {
        
        restartButton = new JButton("Restart");
        restartButton.setBounds(10, 10, 100, 30);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        
        restartButton.setVisible(false); // Initially, hide the restart button
        add(restartButton);

        exitButton = new JButton("Exit");
        exitButton.setBounds(120, 10, 100, 30);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitButton.setVisible(false); // Initially, hide the exit button
        add(exitButton);
    
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isGameOver) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        x -= 5; // Move left when the left arrow key is pressed
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        x += 5; // Move right when the right arrow key is pressed
                    } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        // Shoot when the spacebar key is pressed
                        bullets.add(new Bullet(x + 50, playerY));
                    }
                    repaint(); // Trigger a repaint after changing the position
                }
            }
        });

        setFocusable(true);
        requestFocus();

        Timer timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    // Generate new enemies periodically
                    if (Math.random() < 0.02) {
                        int enemyX = random.nextInt(getWidth());
                        int enemySpeed = random.nextInt(4) + 1;
                        enemies.add(new Enemy(enemyX, 0, enemySpeed));
                    }

                    // Update the enemy positions
                    for (Enemy enemy : enemies) {
                        enemy.move();
                    }

                    // Update bullet positions
                    for (Bullet bullet : bullets) {
                        bullet.move();
                    }

                    // Remove bullets and enemies that are out of bounds
                    bullets.removeIf(bullet -> bullet.getY() < 0);
                    enemies.removeIf(enemy -> enemy.getY() > getHeight());

                    // Check for collisions
                    checkCollisions();

                    repaint();
                }
            }
        });
        timer.start();

        Thread gameThread = new Thread(new GameLoop());
        gameThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        g.drawImage(player.getImage(), x, playerY, 150, 150, this);

        // Draw the enemy photos
        for (Enemy enemy : enemies) {
            g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), 150, 150, this);
        }

        // Draw the bullets
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        // Draw the player's score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        // Draw player lives
        g.setColor(Color.WHITE);
        g.drawString("Lives: " + playerLives, 10, 40);

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2);

            restartButton.setVisible(true); // Show the restart button
            exitButton.setVisible(true); // Show the exit button
            
        }
    }
    
    private void restartGame() {
        // Reset game variables and start a new game
        score = 0;
        playerLives = 3;
        isGameOver = false;
        x = 300;
        playerY = 400;
        enemies.clear();
        bullets.clear();

        // Request focus for the game to accept keyboard input
        setFocusable(true);
        requestFocus();

        restartButton.setVisible(false); // Hide the restart button
        exitButton.setVisible(false); // Hide the exit button

        repaint();
    }


    private class GameLoop implements Runnable {
        @Override
        public void run() {
            while (playerLives > 0) {
                // Implement game logic here

                repaint();

                try {
                    Thread.sleep(1000 / 60); // Sleep for 16.67 milliseconds (approximately 60 FPS)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            isGameOver = true;
            repaint();
        }
    }

    private class Bullet {
        private int x;
        private int y;

        public Bullet(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            y -= 5; // Move the bullet up
        }

        public int getY() {
            return y;
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(x, y, 5, 10); // Draw the bullet
        }
    }

    private class Enemy {
        private int x;
        private int y;
        private int speed;

        public Enemy(int x, int y, int speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        public void move() {
            y += speed; // Move the enemy down
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Image getImage() {
            return enemy.getImage();
        }
    }

    private void checkCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (bullet.x + 5 > enemy.x && bullet.x < enemy.x + 150
                        && bullet.y + 10 > enemy.y && bullet.y < enemy.y + 150) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                    score += 10; // Increase the player's score
                }
            }
        }

        // Remove bullets and enemies after collision checks
        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);

        // Check collisions between player and enemies
        for (Enemy enemy : enemies) {
            if (x + 150 > enemy.x && x < enemy.x + 150
                    && playerY + 150 > enemy.y && playerY < enemy.y + 150) {
                // Decrease player lives and remove the enemy
                playerLives--;
                enemiesToRemove.add(enemy);
            }
        }

        // Remove enemies after collision checks
        enemies.removeAll(enemiesToRemove);

        // Check if player has no lives left
        if (playerLives <= 0) {
            isGameOver = true;
        }
    }
}
