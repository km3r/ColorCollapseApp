package kyle.dynamicdata.io.compact.engine;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import kyle.dynamicdata.io.compact.R;

/**
 * Created by Kyle on 4/9/2015.
 */
public abstract class TouchInput implements View.OnTouchListener,Button.OnClickListener {

    int rows;
    int cols;
    SurfaceView surfaceView;

    public TouchInput(int rows, int cols, SurfaceView view) {
        surfaceView = view;

        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Action action = new Action((int) (motionEvent.getY()/surfaceView.getY()), (int) (motionEvent.getX()/surfaceView.getX()));
        Log.d("Coords",action.toString());
        callback(action);
        return false;
    }

    @Override
    public void onClick(View view) {
        Log.d("canvas", "button is pressed");
        switch (view.getId()){
            case R.id.button2: //New Game
                callback(new Action(Command.RESTART));
                break;
            case R.id.button3: //Difficulty
                callback(new Action(Command.DIFFICULT));
                break;
            case R.id.surfaceView: // on game
                Action action = new Action((int)view.getX(),(int) view.getY());
                callback(action);
                break;
            default:
                ((BoardView) surfaceView).draw();
                break;
        }

    }

    public abstract void callback(Action action);
}
