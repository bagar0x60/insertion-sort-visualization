import java.util.LinkedList;
import java.util.Vector;

public class InsertionSortAnimation {

    private LinkedList <Command> scriptQueue;

    enum Colours { WHITE, BLACK, GREEN, RED }
    private static final double PAUSE_TIME = 500;
    private static final double MOVEMENT_TIME = 500;

    public InsertionSortAnimation (Vector<NumberBlock> arr){
        scriptQueue = new LinkedList <Command>();
        sort(arr);
    }

    public void sort(Vector<NumberBlock> arr){
        for (int i = 0; i < arr.size(); i++) {
            NumberBlock temp = arr.get(i);

            // Blinking red with the active block
            scriptQueue.add(new Colour(Colours.RED, temp));
            scriptQueue.add(new Pause(PAUSE_TIME));
            scriptQueue.add(new Colour(Colours.WHITE, temp));
            scriptQueue.add(new Pause(PAUSE_TIME));
            scriptQueue.add(new Colour(Colours.RED, temp));
            scriptQueue.add(new Pause(PAUSE_TIME));
            // ----------------------------------

            int j = i - 1;

            if (j >= 0 && arr.get(j).getNumber() > temp.getNumber()){
                // Lift red block up
                scriptQueue.add(new Move(new Cor(i, 0), new Cor(i, 2), temp, MOVEMENT_TIME));
                scriptQueue.add(new Pause(PAUSE_TIME));
                // -----------------

                while(j >= 0 && arr.get(j).getNumber() > temp.getNumber()) {
                    // Set compared block green
                    scriptQueue.add(new Colour(Colours.GREEN, arr.get(j)));
                    scriptQueue.add(new Pause(2*PAUSE_TIME));
                    // ------------------------

                    // move compared block to the right
                    scriptQueue.add(new Move(new Cor(j, 0), new Cor(j + 1, 0), arr.get(j), MOVEMENT_TIME));
                    scriptQueue.add(new Pause(PAUSE_TIME));
                    // -------------------------------

                    // Set compared block black
                    scriptQueue.add(new Colour(Colours.BLACK, arr.get(j)));
                    scriptQueue.add(new Pause(PAUSE_TIME));
                    // ------------------------

                    arr.set(j + 1, arr.get(j));
                    j--;
                }

                if (j >= 0) {
                    // Set compared block green
                    scriptQueue.add(new Colour(Colours.GREEN, arr.get(j)));
                    scriptQueue.add(new Pause(2*PAUSE_TIME));
                    // ------------------------

                    // Set compared block black
                    scriptQueue.add(new Colour(Colours.BLACK, arr.get(j)));
                    scriptQueue.add(new Pause(PAUSE_TIME));
                    // ------------------------
                }

                // Move red block to the empty space
                scriptQueue.add(new Move(new Cor(i, 2), new Cor(j + 1, 0), temp, MOVEMENT_TIME));
                scriptQueue.add(new Pause(PAUSE_TIME));
                // ---------------------------------

                arr.set(j + 1, temp);
            }

            // Set active block black
            scriptQueue.add(new Colour(Colours.BLACK, temp));
            scriptQueue.add(new Pause(PAUSE_TIME));
            // ----------------------
        }
    }

    public void setQueue (Vector<NumberBlock> arr){
        scriptQueue.clear();
        sort (arr);
    }

    public String getStatus() {
        if(!scriptQueue.isEmpty()) {
            return scriptQueue.getFirst().getInfo();
        }
        else return "";
    }

    public void tick(double tickTime){
        while (!scriptQueue.isEmpty()){
            tickTime = scriptQueue.getFirst().play(tickTime);

            if (tickTime >= 0) {
                scriptQueue.removeFirst();
            }
            else {
                break;
            }
        }
    }

    private class Move implements Command {
        private double movementTime;
        private double passedTime;
        private Cor startPoint;
        private Cor endPoint;
        private NumberBlock element;
        private double speedX;
        private double speedY;

        Move(Cor start, Cor end, NumberBlock bl, double t){
            movementTime = t;
            startPoint = start;
            endPoint = end;
            element = bl;
            passedTime = 0;
            speedX = (endPoint.xCor - startPoint.xCor) * element.getSideSize() / t;
            speedY = -(endPoint.yCor - startPoint.yCor) * element.getSideSize() / t;
        }

        public double play(double tickTime){
            passedTime += tickTime;

            if (passedTime >= movementTime){
                double lengthX = (tickTime + passedTime - movementTime) * speedX;
                double lengthY = (tickTime + passedTime - movementTime) * speedY;
                element.setLocation(element.getX() + (int)lengthX, element.getY() + (int)lengthY);

                return passedTime - movementTime;
            }
            else {
                double lengthX = tickTime * speedX;
                double lengthY = tickTime * speedY;
                element.setLocation(element.getX() + (int)lengthX, element.getY() + (int)lengthY);

                return -1;
            }
        }

        public String getInfo() {
            if (startPoint.yCor - endPoint.yCor < 0) {
                return "Sort the block from position " + startPoint.xCor;
            }
            else if (startPoint.yCor - endPoint.yCor > 0) {
                return "Moves the sorted block to position " + endPoint.xCor;
            }
            else {
                return "Moves the block from position " + startPoint.xCor + " to endPoint.xCor";
            }
        }
    }

    private class Cor {
        public int xCor;
        public int yCor;

        Cor (int x, int y) {
            xCor = x;
            yCor = y;
        }
    }

    private class Pause implements Command {
        private double workingTime;
        private double passedTime;

        Pause(double t) {
            workingTime = t;
            passedTime = 0;
        }

        public double play(double tickTime){
            passedTime += tickTime;
            if (passedTime >= workingTime){
                return passedTime - workingTime;
            }
            else return -1;
        }

        public String getInfo() { return ""; }
    }

    private class Colour implements Command {
        private NumberBlock element;
        private Colours colour;

        Colour(Colours c, NumberBlock bl){
            colour = c;
            element = bl;
        }

        public double play(double tickTime){
            switch (colour) {
                case WHITE:
                    element.setBorderColorEmpty();
                    break;
                case BLACK:
                    element.setBorderColorGBlack();
                    break;
                case RED:
                    element.setBorderColorRed();
                    break;
                case GREEN:
                    element.setBorderColorGreen();
                    break;
            }

            return tickTime;
        }

        public String getInfo() {
            if (colour == Colours.GREEN) {
                return "Comparison with the block in position " + (int)(element.getXCordinate() / element.getSideSize());
            }
            else return "";
        }
    }
}