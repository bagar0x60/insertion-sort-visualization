import java.awt.*;
import javax.swing.*;

public class NumberBlock extends JPanel{

    private Color borderColor;
    private int number;
    private int sideSize;

    public NumberBlock(int num, int xCor, int yCor, int sideS){
        number = num;
        sideSize = sideS;
        JLabel numLabel = new JLabel(Integer.toString(number));

        this.setLayout(new GridBagLayout());
        this.add(numLabel);
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createLineBorder(Color.white));

        this.setLocation((int)Math.round(xCor), (int)Math.round(yCor));

        this.setSize(sideS, sideS);
    }

    public int getNumber(){
        return number;
    }

    public void setBorderColorRed(){
        this.setBorder(BorderFactory.createLineBorder(Color.red));
    }

    public void setBorderColorGreen(){
        this.setBorder(BorderFactory.createLineBorder(Color.green));
    }

    public void setBorderColorGBlack(){
        this.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void setBorderColorEmpty(){
        this.setBorder(BorderFactory.createLineBorder(Color.white));

    }

    public Color getColor(){
        return  borderColor;
    }

    public void setPosition(double xCor, double yCor){
        this.setLocation((int)Math.round(xCor), (int)Math.round(yCor));

    }

    public int getSideSize(){
        return sideSize;
    }

    public void setSideSize(int sSize){
        sideSize = sSize;
        this.setSize(sideSize, sideSize);
    }
}
