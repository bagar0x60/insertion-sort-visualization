import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class Form  extends JFrame {
    private static final int MAX_BLOCKS_NUMBER = 10;
    private static final int MAX_ALLOWED_ELEMENT = 100;
    private static final int MIN_ALLOWED_ELEMENT = -100;
    private static final int BLOCK_SIDE_SIZE = 70;

    private static final int BUTTON_WIDTH = 32;
    private static final int BUTTON_HEIGHT = 32;
    private static final int SPACE_BETWEEN_BUTTONS = 20;

    private static final float FONT_SIZE = 15;

    private static final String RESOURCES_PATH = "src/resources/";

    private static final int FPS = 60;

    private static final double SPEED_REGULATION_COEFFICIENT = 1.5;

    private double animationSpeedCoefficient = 1;


    private Vector<NumberBlock> blockVector;
    private Timer timer;

    private JPanel bottomPane;
    private JPanel inputPane;
    private JPanel resultPane;
    private JPanel animationPane;
    private JPanel animationPicturePane;
    private JTextField resultTextField;
    private JTextField inputTextField;
    private JTextArea animationTextArea;
    private JScrollPane animationTextScrollPane;
    private JLabel resultLabe;
    private JLabel inputLabe;
    private JPanel emptyPanel1, emptyPanel2, emptyPanel3, emptyPanel4, emptyPanel5;

    private JButton animationSpeedDownButton;
    private JButton animationSpeedUpButton;
    private JButton animationPausePlayButton;
    private InsertionSortAnimation animation;

    private Icon pauseIcon;
    private Icon playIcon;


    public Form() {
        super("Application");
        setSize(800, 600);
        setMinimumSize(new Dimension(800,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());
        blockVector = new Vector<NumberBlock>();

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

        inputLabe = new JLabel(" Input numbers: ");
        inputLabe.setFont(inputLabe.getFont().deriveFont(FONT_SIZE));
        inputTextField = new JTextField();
        inputTextField.addActionListener(new InputTFListener());
        inputTextField.setFont(inputTextField.getFont().deriveFont(FONT_SIZE));


        inputPane.add(inputLabe, BorderLayout.WEST);
        inputPane.add(inputTextField, BorderLayout.CENTER);
        inputPane.add(emptyPanel1, BorderLayout.NORTH);
        inputPane.add(emptyPanel2, BorderLayout.EAST);

        resultLabe = new JLabel("        Result:        ");
        resultLabe.setFont(resultLabe.getFont().deriveFont(FONT_SIZE));

        resultTextField = new JTextField();
        resultTextField.setHorizontalAlignment(JTextField.CENTER);
        resultTextField.setEditable(false);
        resultTextField.setFont(resultTextField.getFont().deriveFont(FONT_SIZE));

        resultPane.add(resultLabe, BorderLayout.WEST);
        resultPane.add(resultTextField, BorderLayout.CENTER);
        resultPane.add(emptyPanel3, BorderLayout.NORTH);
        resultPane.add(emptyPanel4, BorderLayout.SOUTH);
        resultPane.add(emptyPanel5, BorderLayout.EAST);



        animationPane = new JPanel(new BorderLayout());
        animationPicturePane = new JPanel();
        animationTextArea = new JTextArea();
        animationTextScrollPane = new JScrollPane(animationTextArea);


        cont.add(animationPane, BorderLayout.CENTER);
        animationPane.add(animationPicturePane, BorderLayout.CENTER);
        animationPane.add(animationTextScrollPane, BorderLayout.SOUTH);
        animationPane.setBorder(BorderFactory.createLineBorder(Color.black));

        pack();


        animationTextArea.setLayout(null);
        animationTextArea.setEditable(false);
        animationTextArea.setFont(animationTextArea.getFont().deriveFont(FONT_SIZE));

        animationTextScrollPane.setPreferredSize(new Dimension(animationPane.getWidth(), animationPane.getHeight()/2));


        //------------------------------------ Buttons -----------------

        Icon speedDownIcon = new ImageIcon(RESOURCES_PATH + "speed_down.png");

        animationSpeedDownButton = new JButton(speedDownIcon);
        animationSpeedDownButton.setBackground(Color.WHITE);

        animationSpeedDownButton.addActionListener(new animationSpeedDownButtonListener());
        animationSpeedDownButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        animationSpeedDownButton.setLocation(animationPicturePane.getWidth()/2 - (BUTTON_WIDTH/2)*3 - SPACE_BETWEEN_BUTTONS/2,
                (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));

        animationSpeedDownButton.setMargin(new Insets(0, 0, 0, 0));
        animationSpeedDownButton.setBorder(null);


        pauseIcon = new ImageIcon(RESOURCES_PATH + "pause.png");
        playIcon = new ImageIcon(RESOURCES_PATH + "play.png");
        animationPausePlayButton = new JButton(pauseIcon);
        animationPausePlayButton.setBackground(Color.WHITE);
        animationPausePlayButton.setBorder(null);
        animationPausePlayButton.addActionListener(new animationPausePlayButtonListener());
        animationPausePlayButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        animationPausePlayButton.setLocation(animationPicturePane.getWidth()/2 - BUTTON_WIDTH/2,
                (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));


        Icon speedUpIcon = new ImageIcon(RESOURCES_PATH + "speed_up.png");
        animationSpeedUpButton = new JButton(speedUpIcon);
        animationSpeedUpButton.setBackground(Color.WHITE);
        animationSpeedUpButton.setBorder(null);
        animationSpeedUpButton.addActionListener(new animationSpeedUpButtonListener());
        animationSpeedUpButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        animationSpeedUpButton.setLocation(animationPicturePane.getWidth()/2 + BUTTON_WIDTH/2 + SPACE_BETWEEN_BUTTONS/2 ,
                (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));

        //---------------------------------------------------------------------

        animationPicturePane.setLayout(null);
        animationPicturePane.addComponentListener(new ResizeListener());
        animationPicturePane.setBorder(BorderFactory.createLineBorder(Color.black));
        animationPicturePane.setBackground(Color.white);
        animationPicturePane.add(animationSpeedDownButton);
        animationPicturePane.add(animationSpeedUpButton);
        animationPicturePane.add(animationPausePlayButton);

        animation = new InsertionSortAnimation(blockVector, 0, 0);
        timer = new Timer(1000 / FPS, new AnimationListener());
    }

    private class InputTFListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            timer.stop();

            String inpStr;
            int size = 0;

            while(blockVector.size()!=0) {
                animationPicturePane.remove(blockVector.remove(blockVector.size()-1));
            }
            animationPicturePane.revalidate();
            animationPicturePane.repaint();

            for(;;) {
                inpStr = inputTextField.getText();
                Scanner strScanner = new Scanner(inpStr);
                boolean tooLongInt = false;

                while (strScanner.hasNextInt()) {
                    int z = strScanner.nextInt();
                    if(z >= MAX_ALLOWED_ELEMENT || z <= MIN_ALLOWED_ELEMENT){
                        tooLongInt = true;
                        break;
                    }
                    size++;
                }
                if (strScanner.hasNext() || tooLongInt || size > MAX_BLOCKS_NUMBER) {
                    if(strScanner.hasNext() || tooLongInt) {
                        resultTextField.setText("Incorrect data format");
                    }
                    else if(size > MAX_BLOCKS_NUMBER) {
                        resultTextField.setText("Too many numbers (max = 10)");
                    }

                    inputTextField.setText("");
                    size = 0;
                }
                else{
                    break;
                }
            }

            Scanner strScanner = new Scanner(inpStr);
            int Data[] = new int[size];


            int i = 0;

            int xShift = (animationPicturePane.getWidth() - MAX_BLOCKS_NUMBER * BLOCK_SIDE_SIZE) / 2
                    + BLOCK_SIDE_SIZE*(MAX_BLOCKS_NUMBER-size)/2;
            int yShift = (animationPicturePane.getHeight() - BLOCK_SIDE_SIZE - BUTTON_HEIGHT/2) / 2;

            animation.setOriginPosition(xShift, yShift);

            while(strScanner.hasNextInt()){
                Data[i] = strScanner.nextInt();

                blockVector.add(new NumberBlock(Data[i], xShift + i*BLOCK_SIDE_SIZE, yShift, BLOCK_SIDE_SIZE));
                animationPicturePane.add(blockVector.get(i));
                i++;
            }

            animation.setSortedBlocks(blockVector);

            animationPicturePane.revalidate();
            animationPicturePane.repaint();

            sort(Data, size);

            String resultStr = "";
            for(int j = 0; j < size; j++){
                resultStr += Data[j] + " ";
            }


            if(!resultStr.equals("")){
                inputTextField.setText("");
                resultTextField.setText(resultStr);
            }

            animationTextArea.setText("");

            timer.start();
            animationPausePlayButton.setIcon(pauseIcon);
        }
    }

    private class ResizeListener extends ComponentAdapter {
        private int oldHeight, oldWidth;

        ResizeListener() {
            oldWidth = animationPicturePane.getWidth();
            oldHeight = animationPicturePane.getHeight();

        }

        public void componentResized(ComponentEvent e) {
            for (NumberBlock block : blockVector){
                block.setLocation(  block.getX() + (-oldWidth  + animationPicturePane.getWidth())  / 2,
                        block.getY() + (-oldHeight + animationPicturePane.getHeight()) / 2);
            }


            animationSpeedDownButton.setLocation(animationSpeedDownButton.getX() + (-oldWidth + animationPicturePane.getWidth())/2,
                    (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));

            animationPausePlayButton.setLocation(animationPausePlayButton.getX() + (-oldWidth + animationPicturePane.getWidth())/2,
                    (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));

            animationSpeedUpButton.setLocation(animationSpeedUpButton.getX() + (-oldWidth + animationPicturePane.getWidth())/2,
                    (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));


            int originX = (animationPicturePane.getWidth() - MAX_BLOCKS_NUMBER * BLOCK_SIDE_SIZE) / 2
                    + BLOCK_SIDE_SIZE*(MAX_BLOCKS_NUMBER - blockVector.size())/2;
            int originY = (animationPicturePane.getHeight() - BLOCK_SIDE_SIZE - BUTTON_HEIGHT/2) / 2;

            animation.setOriginPosition(originX, originY);

            animationPicturePane.revalidate();
            animationPicturePane.repaint();

            oldWidth = animationPicturePane.getWidth();
            oldHeight = animationPicturePane.getHeight();
        }
    }

    public class AnimationListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            animation.tick((double)1000*animationSpeedCoefficient / (double)FPS);
            animationTextArea.append(animation.getLastMsg());
            animationPicturePane.revalidate();
            animationPicturePane.repaint();
        }
    }


    public class animationSpeedDownButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            animationSpeedCoefficient /= SPEED_REGULATION_COEFFICIENT;
        }
    }

    public class animationSpeedUpButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            animationSpeedCoefficient *= SPEED_REGULATION_COEFFICIENT;
        }
    }

    public class animationPausePlayButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (timer.isRunning()) {
                timer.stop();
                animationPausePlayButton.setIcon(playIcon);
            } else {
                timer.start();
                animationPausePlayButton.setIcon(pauseIcon);
            }
        }
    }

    private static void sort(int arr[], int s){

        for (int i = 0; i < s; i++)
        {
            int temp = arr[i];
            int j = i - 1;
            while(j >= 0 && arr[j] > temp)
            {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = temp;
        }
    }

    public Vector<NumberBlock> getBlockVector(){
        return blockVector;
    }

}