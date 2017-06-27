import java.awt.font.FontRenderContext;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.Timer;


public class Form  extends JFrame {
    private static final int MAX_BLOCKS_NUMBER = 10;
    private static final int MAX_ALLOWED_ELEMENT = 100;
    private static final int MIN_ALLOWED_ELEMENT = -100;
    private static final int BLOCK_SIDE_SIZE = 70;

    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 50;
    private static final int SPACE_BETWEEN_BUTTONS = 20;

    private static final int FPS = 20;


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

    private JButton stopAnimationButt;
    private JButton continueAnimationButt;
    private JButton clearAnimationButt;
    private InsertionSortAnimation animation;


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

        inputLabe = new JLabel("      Input numbers:      ");
        inputTextField = new JTextField();
        inputTextField.addActionListener(new InputTFListener());

        inputPane.add(inputLabe, BorderLayout.WEST);
        inputPane.add(inputTextField, BorderLayout.CENTER);
        inputPane.add(emptyPanel1, BorderLayout.NORTH);
        inputPane.add(emptyPanel2, BorderLayout.EAST);

        resultLabe = new JLabel("             Result:              ");
        resultTextField = new JTextField();
        resultTextField.setHorizontalAlignment(JTextField.CENTER);
        resultTextField.setEditable(false);

        resultPane.add(resultLabe, BorderLayout.WEST);
        resultPane.add(resultTextField, BorderLayout.CENTER);
        resultPane.add(emptyPanel3, BorderLayout.NORTH);
        resultPane.add(emptyPanel4, BorderLayout.SOUTH);
        resultPane.add(emptyPanel5, BorderLayout.EAST);



        animationPane = new JPanel(new BorderLayout());
        animationPicturePane = new JPanel();
        animationTextArea = new JTextArea("\n\n\t\t\t\t  Comments");
        animationTextScrollPane = new JScrollPane(animationTextArea);


        cont.add(animationPane, BorderLayout.CENTER);
        animationPane.add(animationPicturePane, BorderLayout.CENTER);
        animationPane.add(animationTextScrollPane, BorderLayout.SOUTH);
        animationPane.setBorder(BorderFactory.createLineBorder(Color.black));





        pack();
        animationTextArea.setBackground(Color.orange);
        animationTextArea.setLayout(null);
        animationTextArea.setEditable(false);

        animationTextScrollPane.setPreferredSize(new Dimension(animationPane.getWidth(), (animationPane.getHeight()/16)*3));


        stopAnimationButt = new JButton("Pause");
        stopAnimationButt.setBackground(Color.lightGray);
        stopAnimationButt.setBorder(BorderFactory.createLineBorder(Color.red));
        stopAnimationButt.addActionListener(new stopAnimationButtonListener());
        stopAnimationButt.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        stopAnimationButt.setLocation(animationPicturePane.getWidth()/2 - (BUTTON_WIDTH/2)*3 - SPACE_BETWEEN_BUTTONS/2,
                (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));


        clearAnimationButt = new JButton("Clear");
        clearAnimationButt.setBackground(Color.lightGray);
        clearAnimationButt.setBorder(BorderFactory.createLineBorder(Color.cyan));
        clearAnimationButt.addActionListener(new clearAnimationButtonListener());
        clearAnimationButt.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        clearAnimationButt.setLocation(animationPicturePane.getWidth()/2 - BUTTON_WIDTH/2,
                (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));


        continueAnimationButt = new JButton("Play");
        continueAnimationButt.setBackground(Color.lightGray);
        continueAnimationButt.setBorder(BorderFactory.createLineBorder(Color.blue));
        continueAnimationButt.addActionListener(new continueAnimationButtonListener());
        continueAnimationButt.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        continueAnimationButt.setLocation(animationPicturePane.getWidth()/2 + BUTTON_WIDTH/2 + SPACE_BETWEEN_BUTTONS/2 ,
                (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));



        animationPicturePane.setLayout(null);
        animationPicturePane.addComponentListener(new ResizeListener());
        animationPicturePane.setBorder(BorderFactory.createLineBorder(Color.black));
        animationPicturePane.setBackground(Color.white);
        animationPicturePane.add(stopAnimationButt);
        animationPicturePane.add(continueAnimationButt);
        animationPicturePane.add(clearAnimationButt);

        animation = new InsertionSortAnimation(blockVector);
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
                        resultTextField.setText("Данные введены в неверном формате");
                    }
                    else if(size > MAX_BLOCKS_NUMBER) {
                        resultTextField.setText("Слишком длинная последовательность чисел (max = 10)");
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

            while(strScanner.hasNextInt()){
                Data[i] = strScanner.nextInt();

                blockVector.add(new NumberBlock(Data[i], xShift + i* BLOCK_SIDE_SIZE, yShift, BLOCK_SIDE_SIZE));
                animationPicturePane.add(blockVector.get(i));
                i++;
            }

            animation.setQueue(blockVector);

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


            stopAnimationButt.setLocation(stopAnimationButt.getX() + (-oldWidth + animationPicturePane.getWidth())/2,
                    (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));

            clearAnimationButt.setLocation(clearAnimationButt.getX() + (-oldWidth + animationPicturePane.getWidth())/2,
                    (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));

            continueAnimationButt.setLocation(continueAnimationButt.getX() + (-oldWidth + animationPicturePane.getWidth())/2,
                    (animationPicturePane.getHeight() - BUTTON_HEIGHT - 10));

            animationPicturePane.revalidate();
            animationPicturePane.repaint();

            oldWidth = animationPicturePane.getWidth();
            oldHeight = animationPicturePane.getHeight();
        }
    }

    public class AnimationListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            animation.tick(1000 / FPS);
            animationTextArea.append(animation.getLastMsg());
            animationPicturePane.revalidate();
            animationPicturePane.repaint();
        }
    }


    public class stopAnimationButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            timer.stop();
        }
    }

    public class continueAnimationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer.start();
        }
    }

    public class clearAnimationButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            for(ActionListener listener : inputTextField.getActionListeners()){
                listener.actionPerformed(e);
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
