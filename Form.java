package com.company;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Максим on 23.06.2017.
 */
public class Form  extends JFrame{
    private JPanel bottomPane;
    private JPanel inputPane;
    private JPanel resultPane;
    private JPanel animationPane;
    private JPanel animationPicturePane;
    private JPanel animationTextPane;
    private JTextField resultTextField;
    private JLabel resultLabe;
    private JTextField inputTextField;
    private JLabel inputLabe;
    private JPanel emptyPanel1, emptyPanel2, emptyPanel3, emptyPanel4, emptyPanel5;


    private Form() {
        super("Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cont = getContentPane();
        cont.setLayout(new BorderLayout(5,5));

        emptyPanel1 = new JPanel();
        emptyPanel2 = new JPanel();
        emptyPanel3 = new JPanel();
        emptyPanel4 = new JPanel();
        emptyPanel5 = new JPanel();

        emptyPanel1.setBackground(Color.lightGray);
        emptyPanel2.setBackground(Color.lightGray);
        emptyPanel3.setBackground(Color.lightGray);
        emptyPanel4.setBackground(Color.lightGray);
        emptyPanel5.setBackground(Color.lightGray);


        bottomPane = new JPanel(new BorderLayout());
        inputPane = new JPanel(new BorderLayout());
        resultPane = new JPanel(new BorderLayout());

        bottomPane.setBorder(BorderFactory.createLineBorder(Color.black));
        inputPane.setBackground(Color.lightGray);
        resultPane.setBackground(Color.lightGray);

        cont.add(bottomPane, BorderLayout.SOUTH);
        bottomPane.add(inputPane, BorderLayout.CENTER);
        bottomPane.add(resultPane, BorderLayout.SOUTH);

        inputLabe = new JLabel("      Input numbers:      ");
        inputTextField = new JTextField();

        inputPane.add(inputLabe, BorderLayout.WEST);
        inputPane.add(inputTextField, BorderLayout.CENTER);
        inputPane.add(emptyPanel1, BorderLayout.NORTH);
        inputPane.add(emptyPanel2, BorderLayout.EAST);

        resultLabe = new JLabel("             Result:              ");
        resultTextField = new JTextField();

        resultPane.add(resultLabe, BorderLayout.WEST);
        resultPane.add(resultTextField, BorderLayout.CENTER);
        resultPane.add(emptyPanel3, BorderLayout.NORTH);
        resultPane.add(emptyPanel4, BorderLayout.SOUTH);
        resultPane.add(emptyPanel5, BorderLayout.EAST);


        animationPane = new JPanel(new BorderLayout());
        animationPicturePane = new JPanel(new FlowLayout());
        animationTextPane = new JPanel(new FlowLayout());

        cont.add(animationPane, BorderLayout.CENTER);
        animationPane.add(animationPicturePane, BorderLayout.CENTER);
        animationPane.add(animationTextPane, BorderLayout.SOUTH);

        //==============
        animationPicturePane.setLayout(null);

        Dimension animationPicturePaneSize = animationPicturePane.getSize();  // return 0, 0 because fuck you

        JButton button1 = new JButton("1");
        button1.setSize(100, 100);
        button1.setLocation(200, 200);
        JButton button2 = new JButton("2");
        button2.setSize(100, 100);
        button2.setLocation(300, 200);

        animationPicturePane.add(button1);
        animationPicturePane.add(button2);
        //==============

        animationTextPane.setBorder(BorderFactory.createLineBorder(Color.black));
        animationPicturePane.setBorder(BorderFactory.createLineBorder(Color.black));
        animationPane.setBorder(BorderFactory.createLineBorder(Color.black));

        animationPicturePane.add(new JLabel("Animaion"));
        animationTextPane.add(new JLabel("Comments"));
        animationPicturePane.setBackground(Color.white);
        animationTextPane.setBackground(Color.orange);


    }

    public static void main(String[] args) {
        Form f = new Form();
        f.setVisible(true);
    }
}