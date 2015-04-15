package kyle.dynamicdata.io.compact.engine;

import android.graphics.Color;

/**
 * Created by Kyle on 4/9/2015.
 */
public enum BoardSquare {
    WHITE(Color.WHITE),
    RED(Color.RED),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE),
    YELLOW(Color.YELLOW);

    public int color;

    BoardSquare(int color)
    {
        this.color = color;
    }
}
