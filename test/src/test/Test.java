package test;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test extends JFrame {
    StartMenu startMenu = new StartMenu();
    PlayState playState = new PlayState();

    Test() {
        
        this.add(startMenu);

        // Add an ActionListener to the "Start" button
        startMenu.Start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // When the "Start" button is clicked, switch to PlayState
                changeToPlayState();
            }
        });

        // Add an ActionListener to the "Exit" button in PlayState
        startMenu.Exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // When the "Exit" button in PlayState is clicked, exit the game
                System.exit(0);
            }
        });
    }

    public void changeToPlayState() {
        // Remove the StartMenu and add the PlayState
        this.remove(startMenu);
        this.add(playState);
        // Revalidate and repaint to update the frame
        this.revalidate();
        this.repaint();
        // Set the focus to the PlayState so that it can receive keyboard input
        playState.requestFocusInWindow();
    }

    public static void main(String[] args) {
        JFrame j = new Test();
        j.setTitle("SpaceRanger");
        j.setSize(800, 600);
        j.setDefaultCloseOperation(EXIT_ON_CLOSE);
        j.setLocationRelativeTo(null);
        j.setVisible(true);
    }
}
