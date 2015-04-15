package kyle.dynamicdata.io.compact.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * Created by Kyle on 4/9/2015.
 */
public class BoardView extends SurfaceView {
    BoardSquare[][] board;
    float x,y;
    boolean win = false;

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context) {
        super(context);
    }

    void pushUpdate(BoardSquare[][] board, int numberRemaining, int turns)
    {
        this.board = board;
        win = numberRemaining == 0;
        draw();
    }

    void pushError(String error) {

    }


    public synchronized void draw(){
        Canvas canvas = getHolder().lockCanvas();
        if (board == null || canvas == null){
            Log.d("canvas", "canvas is null");
            return;
        }

        Log.d("canvas","canvas is not null");
        x = canvas.getWidth()/(float)board[0].length;
        y = canvas.getHeight()/(float)board.length;
        for (int i = 0; i < board[0].length; i++){
            for (int j = 0; j < board.length; j++){
                canvas.drawRect(x * i, j * y, x * i + x, j * y + y, getPaint(j, i));

            }
        }
        if (win){
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(64);
            canvas.drawText("You win!", canvas.getWidth() / 2, canvas.getHeight() / 2, paint);
        }
        getHolder().unlockCanvasAndPost(canvas);
    }

    public Paint getPaint(int row, int col){
        Paint p = new Paint();
        p.setColor(board[row][col].color);
        return p;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
