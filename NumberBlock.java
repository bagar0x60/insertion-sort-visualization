import java.awt.*;
import javax.swing.*;

public class NumberBlock extends JPanel{
    private static final int BORDER_WIDTH = 5;
    private static final float FONT_SIZE = 32.0f;

    private Color borderColor;
    private int number;
    private int sideSize;

    public NumberBlock(int num, double x, double y, int sideS){
        number = num;
        sideSize = sideS;
        JLabel numLabel = new JLabel(Integer.toString(number));
        numLabel.setFont(numLabel.getFont().deriveFont(FONT_SIZE));

        setLayout(new GridBagLayout());
        add(numLabel);
        setBackground(Color.white);
        setLocation((int)Math.round(x), (int)Math.round(y));

        setSideSize(sideS);
        setBorderColorEmpty();
    }

    public int getNumber(){
        return number;
    }

    public void setBorderColorRed(){
        borderColor = Color.red;
        this.setBorder(BorderFactory.createLineBorder(borderColor, BORDER_WIDTH));
    }

    public void setBorderColorGreen(){
        borderColor = Color.green;
        this.setBorder(BorderFactory.createLineBorder(borderColor, BORDER_WIDTH));;
    }

    public void setBorderColorGBlack(){
        borderColor = Color.BLACK;
        this.setBorder(BorderFactory.createLineBorder(borderColor, BORDER_WIDTH));
    }

    public void setBorderColorEmpty(){
        borderColor = Color.WHITE;
        this.setBorder(BorderFactory.createLineBorder(borderColor, BORDER_WIDTH));
    }

    public Color getColor(){
        return  borderColor;
    }

    public int getSideSize(){
        return sideSize;
    }

    public void setSideSize(int sSize){
        sideSize = sSize;
        this.setSize(sideSize, sideSize);
    }

}
