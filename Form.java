import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class Form  extends JFrame {
    private Vector<NumberBlock> blockVector;
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
    private int oldX = 0;
    private int oldY = 0;

    private static final int MAX_BLOCKS_NUMBER = 10;
    private static final int MAX_ALLOWED_ELEMENT = 100;
    private static final int MIN_ALLOWED_ELEMENT = -100;
    private static final int BLOCK_SIDE_SIZE = 70;

    public Form() {
        super("Application");
        setSize(800, 600);
        setMinimumSize(new Dimension(800,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cont = getContentPane();
        cont.setLayout(new BorderLayout(5,5));
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
        animationTextPane = new JPanel(new FlowLayout());

        cont.add(animationPane, BorderLayout.CENTER);
        animationPane.add(animationPicturePane, BorderLayout.CENTER);
        animationPane.add(animationTextPane, BorderLayout.SOUTH);

        animationPicturePane.setLayout(null);
        animationPicturePane.addComponentListener(new ResizeListener());

        animationTextPane.setBorder(BorderFactory.createLineBorder(Color.black));
        animationPicturePane.setBorder(BorderFactory.createLineBorder(Color.black));
        animationPane.setBorder(BorderFactory.createLineBorder(Color.black));

        animationPicturePane.add(new JLabel("Animaion"));
        animationTextPane.add(new JLabel("Comments"));
        animationPicturePane.setBackground(Color.white);
        animationTextPane.setBackground(Color.orange);

    }

    private class InputTFListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
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
            int xShift = (animationPane.getWidth() - MAX_BLOCKS_NUMBER * BLOCK_SIDE_SIZE) / 2 ;
            int yShift = (animationPane.getHeight() - BLOCK_SIDE_SIZE) / 2;
            while(strScanner.hasNextInt()){
                Data[i] = strScanner.nextInt();

                blockVector.add(new NumberBlock(Data[i], xShift + i* BLOCK_SIDE_SIZE, yShift, BLOCK_SIDE_SIZE));
                animationPicturePane.add(blockVector.get(i));
                i++;
            }

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

        }
    }

    private class ResizeListener extends ComponentAdapter {
        private int oldHeight, oldWidth;

        ResizeListener() {
            oldWidth = animationPane.getWidth();
            oldHeight = animationPane.getHeight();
        }

        public void componentResized(ComponentEvent e) {
            for (NumberBlock block: blockVector){
                block.setPosition(  block.getXCordinate() + (-oldWidth  + animationPane.getWidth())  / 2,
                                    block.getYCordinate() + (-oldHeight + animationPane.getHeight()) / 2);
            }

            animationPicturePane.revalidate();
            animationPicturePane.repaint();

            oldWidth = animationPane.getWidth();
            oldHeight = animationPane.getHeight();
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