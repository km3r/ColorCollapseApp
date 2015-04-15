package kyle.dynamicdata.io.compact.engine;

/**
 * Created by Kyle on 4/8/2015.
 */
public class Action {
    int row;
    int col;
    boolean isCommand;
    Command action;

    public Action(int row, int col){
        this.row = row;
        this.col = col;
        isCommand = false;
    }

    public Action(Command action){
        this.action = action;
        isCommand = true;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isCommand() {
        return isCommand;
    }

    public Command getAction() {
        return action;
    }

    @Override
    public String toString() {
        if (isCommand()){
            return action.toString();
        }
        return "row: " + row + "; col: " + col;
    }
}
