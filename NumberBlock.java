
import java.awt.*;
import javax.swing.*;

public class NumberBlock extends JPanel{

    private Color borderColor;
    private int number;
    private int xCordinate;
    private int yCordinate;
    private int sideSize;

    public NumberBlock(int num, int xCor, int yCor, int sideS){

        number = num;
        xCordinate = xCor;
        yCordinate = yCor;
        sideSize = sideS;
        JLabel numLabe = new JLabel(Integer.toString(number));
        numLabe.setSize(10, 10);
        numLabe.setAlignmentX(CENTER_ALIGNMENT);
        numLabe.setAlignmentY(CENTER_ALIGNMENT);
        this.add(numLabe);
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setLocation(xCordinate, yCordinate);
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

    public Color getColor(){
        return  borderColor;
    }

    public void setPosition(int xCor, int yCor){
        xCordinate = xCor;
        yCordinate = yCor;
        this.setLocation(xCor, yCor);
    }

    public int getXCordinate(){
        return xCordinate;
    }

    public int getYCordinate(){
        return yCordinate;
    }

    public int getSideSize(){
        return sideSize;
    }

    public void setSideSize(int sSize){

        sideSize = sSize;
        this.setSize(sideSize, sideSize);
    }

}