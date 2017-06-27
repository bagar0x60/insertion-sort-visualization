import java.util.LinkedList;
import java.util.Vector;

public class InsertionSortAnimation {

    private LinkedList <Command> queue;
    enum Colours { WHITE, BLACK, GREEN, RED }
    enum CompletedActions{ MAIN_CHOOSE, MOVE_RIGHT, COMPARE_ELEMENTS, ELEMENT_SORTED, END_SORT}
    private static final double PAUSE_TIME = 500;
    private static final double MOVEMENT_TIME = 500;
    private String lastMsg;

    public InsertionSortAnimation (Vector<NumberBlock> arr){
        queue = new LinkedList <Command>();
        sort(arr);
    }

    public void sort(Vector<NumberBlock> arr){

        String arrStr = new String();
        for(int i = 0; i < arr.size(); i++){
            arrStr += arr.get(i).getNumber() + " ";
        }

        for (int i = 0; i < arr.size(); i++) {
            NumberBlock temp = arr.get(i);

            // --------------- Blinking red with the active block ---------------
            queue.add(new ActionComplited(CompletedActions.MAIN_CHOOSE, temp));
            queue.add(new Colour(Colours.RED, temp));
            queue.add(new Pause(PAUSE_TIME));
            queue.add(new Colour(Colours.WHITE, temp));
            queue.add(new Pause(PAUSE_TIME));
            queue.add(new Colour(Colours.RED, temp));
            queue.add(new Pause(PAUSE_TIME));
            // ------------------------------------------------------------------

            int j = i - 1;

            if (j >= 0){
                if(arr.get(j).getNumber() > temp.getNumber()){
                    boolean compareMsgPrinted = false;
                    // raise up
                    queue.add(new ActionComplited(CompletedActions.COMPARE_ELEMENTS, temp, arr.get(j)));
                    queue.add(new Colour(Colours.GREEN, arr.get(j)));
                    queue.add(new Pause(PAUSE_TIME));
                    queue.add(new Move(new Cor(i, 0), new Cor(i, 2), temp, MOVEMENT_TIME));
                    queue.add(new Pause(PAUSE_TIME/2));
                    compareMsgPrinted = true;
                    // --------

                    while(j >= 0 && arr.get(j).getNumber() > temp.getNumber()) {

                        // --------------- Blinking green with the active block ---------------

                        if(!compareMsgPrinted)queue.add(new ActionComplited(CompletedActions.COMPARE_ELEMENTS, temp, arr.get(j)));
                        compareMsgPrinted = false;
                        queue.add(new Colour(Colours.GREEN, arr.get(j)));
                        queue.add(new Pause(PAUSE_TIME));
                        // ------------------------------------------------------------------


                        // move block left
                        queue.add(new ActionComplited(CompletedActions.MOVE_RIGHT, arr.get(j)));
                        queue.add(new Move(new Cor(j, 0), new Cor(j + 1, 0), arr.get(j), MOVEMENT_TIME));
                        queue.add(new Colour(Colours.BLACK, arr.get(j)));
                        queue.add(new Pause(PAUSE_TIME));
                        // --------------

                        arr.set(j + 1, arr.get(j));
                        j--;
                    }

                    // move active block in empty space
                    queue.add(new Move(new Cor(i, 2), new Cor(j + 1, 0), temp, MOVEMENT_TIME));
                    queue.add(new Pause(PAUSE_TIME/2));
                    // -------------------------------

                    arr.set(j + 1, temp);
                }
                else{
                    queue.add(new ActionComplited(CompletedActions.COMPARE_ELEMENTS, temp, arr.get(j)));
                    queue.add(new Colour(Colours.GREEN, arr.get(j)));
                    queue.add(new Pause(PAUSE_TIME*2));
                    queue.add(new Colour(Colours.BLACK, arr.get(j)));
                }
            }

            queue.add(new ActionComplited(CompletedActions.ELEMENT_SORTED, temp));
            queue.add(new Colour(Colours.BLACK, temp));
            queue.add(new Pause(PAUSE_TIME));
        }
        queue.add(new ActionComplited(CompletedActions.END_SORT, arrStr));
    }

    public void setQueue (Vector<NumberBlock> arr){
        queue.clear();
        sort (arr);
    }


    public String getLastMsg(){
        return lastMsg;
    }

    public void tick(double tickTime){
        lastMsg = "";
        while (!queue.isEmpty()){
            tickTime = queue.getFirst().play(tickTime);

            if (tickTime >= 0) {
                String tempStr = queue.getFirst().getInfo();
                if(!tempStr.equals("")) lastMsg = tempStr;
                queue.removeFirst();
            }
            else {
                break;
            }
        }
    }


    private class ActionComplited implements Command{

        private CompletedActions compActs;
        private String message;
        private NumberBlock numBlockMain;
        private NumberBlock numBlockCompared;

        ActionComplited(CompletedActions Act, NumberBlock block){
            compActs = Act;
            numBlockMain = block;
        }

        ActionComplited(CompletedActions Act, NumberBlock mainBlock, NumberBlock comparedBlock){
            compActs = Act;
            numBlockMain = mainBlock;
            numBlockCompared = comparedBlock;
        }

        ActionComplited(CompletedActions Act, String vecToSort){
            compActs = Act;
            message = "\n numbers: \"" + vecToSort;
        }

        @Override
        public double play(double tickTime) {
            switch (compActs) {
                case MAIN_CHOOSE:
                    message = "\n" + "Element (" + numBlockMain.getNumber() + ") choosed.";
                    break;
                case MOVE_RIGHT:
                    message = "\n" + "Element (" + numBlockMain.getNumber() + ") moves right.";
                    break;
                case COMPARE_ELEMENTS:
                    message = "\n" + "Element (" + numBlockMain.getNumber() + ") compares with (" + numBlockCompared.getNumber() + ")";
                    break;
                case ELEMENT_SORTED:
                    message = "\n" + "Element (" + numBlockMain.getNumber() + ") sorted.\n";
                    break;
                case END_SORT:
                    message += "\" sorted successefully .\n";
                    break;
                default:
            }
            return tickTime;
        }

        @Override
        public String getInfo() {
            return message;
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
            return "";
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
            return "";
        }
    }
}
