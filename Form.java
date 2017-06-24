package com.company;

import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.StringJoiner;

/**
 * Created by Максим on 23.06.2017.
 */
public class Form  extends JFrame implements ActionListener {
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
        setMinimumSize(new Dimension(800,600));
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
        inputTextField.addActionListener(this);

        inputPane.add(inputLabe, BorderLayout.WEST);
        inputPane.add(inputTextField, BorderLayout.CENTER);
        inputPane.add(emptyPanel1, BorderLayout.NORTH);
        inputPane.add(emptyPanel2, BorderLayout.EAST);

        resultLabe = new JLabel("             Result:              ");
        resultTextField = new JTextField();
        resultTextField.setHorizontalAlignment(JTextField.CENTER);

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
        button1.setLocation(cont.getWidth()/10+50, 200);
        JButton button2 = new JButton("2");
        button2.setSize(100, 100);
        button2.setLocation(cont.getWidth()/10+150, 200);

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

    public void actionPerformed(ActionEvent e){
        String inpStr;
        int size = 0;

        // Проверка корректности входных данных
        for(;;) {
            inpStr = inputTextField.getText();
            Scanner strScanner = new Scanner(inpStr);
            boolean tooLongInt = false;

            while (strScanner.hasNextInt()) {
                int z = strScanner.nextInt();
                if(z > 99 || z < -99){
                    tooLongInt = true;
                    break;
                }
                size++;
            }
            if (strScanner.hasNext() || tooLongInt || size > 10) {
                if(strScanner.hasNext())
                    resultTextField.setText("Данные введены в неверном формате");
                else if(size > 10)
                    resultTextField.setText("Слишком длинная последовательность чисел (max = 10)");

                inputTextField.setText("");
                inputTextField.addActionListener(this);
                //inpScanner.reset();
                size = 0;
                continue;
            }
            else{
                break;
            }
        }

        Scanner strScanner = new Scanner(inpStr);
        int inpData[] = new int[size];

        int i = 0;
        while(strScanner.hasNextInt()){
            inpData[i] = strScanner.nextInt();
            i++;
        }

        sort(inpData, size);

        String resultStr = "";
        for(int j = 0; j < size; j++){
            resultStr += inpData[j] + " ";
        }

        resultTextField.setText(resultStr);

    }

    public static void main(String[] args) {
        Form f = new Form();
        f.setVisible(true);
    }

    public static void sort(int arr[], int s){

        for (int i = 0; i < s; i++)
        {
            int temp = arr[i];
            int j =i-1;
            while(j >= 0 && arr[j] > temp)
            {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = temp;
        }
    }

}

