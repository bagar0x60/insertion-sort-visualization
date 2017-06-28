import java.util.LinkedList;
import java.util.Vector;

public class InsertionSortAnimation {

    private LinkedList <Command> scriptQueue;
    enum Colours { WHITE, BLACK, GREEN, RED }
    enum CompletedActions{ MAIN_CHOOSE, MOVE_RIGHT, COMPARE_ELEMENTS, ELEMENT_SORTED, END_SORT}
    private static final double PAUSE_TIME = 500;
    private static final double MOVEMENT_TIME = 500;
    private String lastMsg;
    private Cor originPosition = new Cor(0, 0);

    public InsertionSortAnimation(Vector<NumberBlock> arr, int xOrigin, int yOrigin) {
        scriptQueue = new LinkedList <Command>();
        buildScriptQueue(arr);
        setOriginPosition(xOrigin, yOrigin);
    }

    public void buildScriptQueue(Vector<NumberBlock> arr) {
        if (arr.size() == 0) return;

        String arrStr = new String();
        for(int i = 0; i < arr.size(); i++){
            arrStr += arr.get(i).getNumber() + " ";
        }

        for (int i = 0; i < arr.size(); i++) {
            NumberBlock temp = arr.get(i);

            // Blinking red with the active block
            scriptQueue.add(new ActionCompleted(CompletedActions.MAIN_CHOOSE, temp));

            scriptQueue.add(new Colour(Colours.RED, temp));
            scriptQueue.add(new Pause(PAUSE_TIME));
            scriptQueue.add(new Colour(Colours.WHITE, temp));
            scriptQueue.add(new Pause(PAUSE_TIME));
            scriptQueue.add(new Colour(Colours.RED, temp));
            scriptQueue.add(new Pause(PAUSE_TIME));
            // ----------------------------------

            int j = i - 1;

            if (j >= 0 && arr.get(j).getNumber() <= temp.getNumber()) {
                // Set compared block green
                scriptQueue.add(new ActionCompleted(CompletedActions.COMPARE_ELEMENTS, temp, arr.get(j)));

                scriptQueue.add(new Colour(Colours.GREEN, arr.get(j)));
                scriptQueue.add(new Pause(2*PAUSE_TIME));
                // ------------------------

                // Set compared block black
                scriptQueue.add(new Colour(Colours.BLACK, arr.get(j)));
                scriptQueue.add(new Pause(PAUSE_TIME));
                // ------------------------

            }


            if (j >= 0 && arr.get(j).getNumber() > temp.getNumber()) {
                // Lift red block up
                scriptQueue.add(new ActionCompleted(CompletedActions.COMPARE_ELEMENTS, temp, arr.get(j)));

                scriptQueue.add(new Move(new Cor(i, 0), new Cor(i, -2), temp, MOVEMENT_TIME));
                scriptQueue.add(new Pause(PAUSE_TIME));
                // ----------------

                while (j >= 0 && arr.get(j).getNumber() > temp.getNumber()) {
                    // Set compared block green
                    scriptQueue.add(new ActionCompleted(CompletedActions.COMPARE_ELEMENTS, temp, arr.get(j)));

                    scriptQueue.add(new Colour(Colours.GREEN, arr.get(j)));
                    scriptQueue.add(new Pause(2*PAUSE_TIME));
                    // ------------------------

                    // move compared block to the right
                    scriptQueue.add(new ActionCompleted(CompletedActions.MOVE_RIGHT, arr.get(j)));

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
                    scriptQueue.add(new ActionCompleted(CompletedActions.COMPARE_ELEMENTS, temp, arr.get(j)));

                    scriptQueue.add(new Colour(Colours.GREEN, arr.get(j)));
                    scriptQueue.add(new Pause(2*PAUSE_TIME));
                    // ------------------------

                    // Set compared block black
                    scriptQueue.add(new Colour(Colours.BLACK, arr.get(j)));
                    scriptQueue.add(new Pause(PAUSE_TIME));
                    // ------------------------
                }

                // Move red block to the empty space
                scriptQueue.add(new Move(new Cor(i, -2), new Cor(j + 1, 0), temp, MOVEMENT_TIME));
                scriptQueue.add(new Pause(PAUSE_TIME));
                // ---------------------------------

                arr.set(j + 1, temp);
            }

            // Set active block black
            scriptQueue.add(new ActionCompleted(CompletedActions.ELEMENT_SORTED, temp));
            scriptQueue.add(new Colour(Colours.BLACK, temp));
            scriptQueue.add(new Pause(PAUSE_TIME));
            // ----------------------
        }

        scriptQueue.add(new ActionCompleted(CompletedActions.END_SORT, arrStr));
    }


    public void setSortedBlocks(Vector<NumberBlock> arr){
        scriptQueue.clear();
        buildScriptQueue(arr);
    }

    public void setOriginPosition(int x, int y) {
        originPosition = new Cor(x, y);
    }

    public String getLastMsg(){

        return lastMsg;
    }

    public void tick(double tickTime){
        lastMsg = "";
        while (!scriptQueue.isEmpty()){
            tickTime = scriptQueue.getFirst().play(tickTime);

            if (tickTime >= 0) {
                String tempStr = scriptQueue.getFirst().getInfo();
                if(!tempStr.equals("")) lastMsg = tempStr;
                scriptQueue.removeFirst();
            }
            else {
                break;
            }
        }
    }


    private class ActionCompleted implements Command {

        private CompletedActions compActs;
        private String message;
        private NumberBlock numBlockMain;
        private NumberBlock numBlockCompared;

        ActionCompleted(CompletedActions Act, NumberBlock block){
            compActs = Act;
            numBlockMain = block;
        }

        ActionCompleted(CompletedActions Act, NumberBlock mainBlock, NumberBlock comparedBlock){
            compActs = Act;
            numBlockMain = mainBlock;
            numBlockCompared = comparedBlock;
        }

        ActionCompleted(CompletedActions Act, String vecToSort){
            compActs = Act;
            message = "\n numbers: \"" + vecToSort;
        }

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
        private double reminderX = 0;
        private double reminderY = 0;

        Move(Cor start, Cor end, NumberBlock bl, double t){
            movementTime = t;
            startPoint = start;
            endPoint = end;
            element = bl;
            passedTime = 0;
            speedX = (endPoint.xCor - startPoint.xCor) * element.getSideSize() / t;
            speedY = (endPoint.yCor - startPoint.yCor) * element.getSideSize() / t;
        }

        public double play(double tickTime){
            passedTime += tickTime;

            double positionX, positionY;
            if (passedTime >= movementTime){
                positionX = endPoint.xCor*element.getSideSize() + originPosition.xCor;
                positionY = endPoint.yCor*element.getSideSize() + originPosition.yCor;
            }
            else {
                positionX = passedTime * speedX + startPoint.xCor*element.getSideSize() + originPosition.xCor;
                positionY = passedTime * speedY + startPoint.yCor*element.getSideSize() + originPosition.yCor;
            }

            element.setLocation((int)positionX, (int)positionY);
            return passedTime - movementTime;
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
