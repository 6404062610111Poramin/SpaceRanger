/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Guy
 */
public class StartMenu extends JPanel {

    //import wallpaper
    public ImageIcon wallpaper = new ImageIcon(this.getClass().getResource("StartWallpaper.gif"));

    //Start&Exit button
    private ImageIcon exits = new ImageIcon(this.getClass().getResource("exit-button.png"));
    public JButton Exit = new JButton(exits);
    
    private ImageIcon starts = new ImageIcon(this.getClass().getResource("start-button.png"));
    public JButton Start = new JButton(starts);

    StartMenu() {

        setLayout(null);

        //Set Button Location 
        this.add(Start);
        Start.setBounds(330, 250, 145,150);
        this.add(Exit);
        Exit.setBounds(620, 450, 145, 100);

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        //setColor
        g.setColor(Color.WHITE);

        //paint wallpaper
        g.drawImage(wallpaper.getImage(), 0, 0, 800, 600, this);

        //setFont
        g.setFont(new Font("2005_iannnnnTKO", Font.CENTER_BASELINE, 50));
        g.drawString("SpaceRanger", 240, 200);

    }

}
