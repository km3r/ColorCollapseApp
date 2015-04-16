package kyle.dynamicdata.io.compact.engine;

import android.view.View;
import android.widget.Button;

import kyle.dynamicdata.io.compact.R;

/**
 * Controls the board for the Collapse game.
 *
 * @author Kyle Rosenthal
 * @version 4/6/2015
 */

public class CollapseBoard
{
    private BoardView output;
    private BoardSquare[][] boardData;
    private BoardSquare[][] startBoard;
    private int numberRemaining;
    private int kMaxData;
    private int turns;

    /**
     * Initialize a board with an
     * output stream, a size, and the number of colors.
     * @param output output stream
     * @param size size of board
     * @param kMaxData number of types of tiles
     */
    public CollapseBoard(BoardView output, int size, int kMaxData, Button[] buttons)
    {
        TouchInput input = new TouchInput(size,size,output) {
            @Override
            public void callback(Action action) {
                update(action);
            }
        };
        this.output = output;

        //output.setOnClickListener(input);
        output.setOnTouchListener(input);

        buttons[0].findViewById(R.id.button).setOnClickListener(input);
        buttons[1].findViewById(R.id.button2).setOnClickListener(input);
        buttons[2].findViewById(R.id.button3).setOnClickListener(input);

        numberRemaining = size * size;
        this.kMaxData = kMaxData;
        turns = 0;
        boardData = new BoardSquare[size][size];
        startBoard = new BoardSquare[size][size];
        reset();
        output.pushUpdate(boardData, numberRemaining, turns);
    }

    /**
     * Update the board with an action.
     * @param nextAction the action to perform
     */
    public void update(Action nextAction)
    {
        if (nextAction.isCommand()) {
            switch (nextAction.getAction()) {
                case RESET:
                    restart();
                    break;
                case RESTART:
                    reset();
                    break;
                case DIFFICULT:
                    if (kMaxData == 3){
                        kMaxData = 4;
                    } else {
                        kMaxData = 3;
                    }
                    break;
                default:
                    output.pushError("Bad input.");
                    break;
            }
            output.pushUpdate(boardData,numberRemaining,turns);
        }
        else {
            press(nextAction.row, nextAction.col);
            output.pushUpdate(boardData, numberRemaining, turns);
        }
    }

    /**
     * restart the game with the same board.
     */
    private void restart()
    {
        for (int row = 0; row < startBoard.length; row++)
        {
            boardData[row] = startBoard[row].clone();
        }
        numberRemaining = boardData.length * boardData[0].length;
        turns = 0;
    }

    /**
     * Remove this tile and all adjacent tiles.
     * if the size of the group is 2 or more
     * @param row row of tile
     * @param col column of tile
     */
    private void press(int row, int col)
    {
        if (row > boardData.length || col > boardData[0].length){
            return;
        }
        if (boardData[row][col] != BoardSquare.WHITE)
        {
            if ((row - 1 >= 0
                        && boardData[row][col] == boardData[row - 1][col])
                    || (row + 1 < boardData.length
                        && boardData[row][col] == boardData[row + 1][col])
                    || (col - 1 >= 0
                        && boardData[row][col] == boardData[row][col - 1])
                    || (col + 1 < boardData[0].length
                        && boardData[row][col] == boardData[row][col + 1]))
            {
                pressHelper(row, col, boardData[row][col]);
                shift();
            }
            else
            {
                turns--;
                output.pushError("Only one tile there.");
            }
        }
        else
        {
            turns--;
            output.pushError("Empty location.");
        }
    }

    /**
     * Recursively assist with finding adjecent tiles and removing them.
     * @param row row of tile
     * @param col column of tile
     * @param data the color of tile
     */
    private void pressHelper(int row, int col, BoardSquare data)
    {
        if (row < boardData.length
                && col < boardData[0].length
                && row >= 0
                && col >= 0
                && data != BoardSquare.WHITE)
        {
            if (boardData[row][col] == data)
            {
                boardData[row][col] = BoardSquare.WHITE;
                numberRemaining--;
                pressHelper(row - 1, col, data);
                pressHelper(row, col - 1, data);
                pressHelper(row + 1, col, data);
                pressHelper(row, col + 1, data);
            }
        }
    }

    /**
     * shift the board to the center.
     */
    private void shift()
    {
        for (int col = 0; col < boardData[0].length; col++)
        {
            drop(boardData.length - 1, col);
        }
        shiftL(boardData[0].length / 2 - 1);
        shiftR(boardData[0].length / 2);
        output.pushUpdate(boardData, numberRemaining, turns);
    }

    /**
     * Shift the right side of the board.
     * @param col column being checked
     */
    private void shiftR(int col)
    {
        if (col < boardData[0].length)
        {
            if (boardData[boardData.length - 1][col] == BoardSquare.WHITE)
            {
                int space = 1;
                while (col + space < boardData[0].length
                        && boardData[boardData[0].length - 1][col + space] == BoardSquare.WHITE)
                {
                    space++;
                }
                if (col + space != boardData[0].length)
                {
                    for (int row = 0; row < boardData.length; row++)
                    {
                        boardData[row][col] = boardData[row][col + space];
                        boardData[row][col + space] = BoardSquare.WHITE;
                    }
                }
            }
            shiftR(col + 1);
        }
    }

    /**
     * Shift the left side of the board.
     * @param col column being checked
     */
    private void shiftL(int col)
    {
        if (col >= 0)
        {
            if (boardData[boardData.length - 1][col] == BoardSquare.WHITE)
            {
                int space = 1;
                while (col - space >= 0
                        && boardData[boardData[0].length - 1][col - space] == BoardSquare.WHITE)
                {
                    space++;
                }
                if (col - space != -1)
                {
                    for (int row = 0; row < boardData.length; row++)
                    {
                        boardData[row][col] = boardData[row][col - space];
                        boardData[row][col - space] = BoardSquare.WHITE;
                    }

                }
            }
            shiftL(col - 1);
        }
    }

    /**
     * Drop all the pieces down to the bottom.
     * @param row row of tile
     * @param col column of tile
     */
    private void drop(int row, int col)
    {
        if (row >= 0)
        {
            if (boardData[row][col] == BoardSquare.WHITE)
            {
                int space = 1;
                while (row - space >= 0 && boardData[row - space][col] == BoardSquare.WHITE)
                {
                    space++;
                }
                if (row - space != -1)
                {
                    boardData[row][col] = boardData[row - space][col];
                    boardData[row - space][col] = BoardSquare.WHITE;
                }
            }
            drop(row - 1, col);
        }
    }

    /**
     * Cheat and make it one move away from winning.
     */
    private void cheat()
    {
        for (int row = 0; row < boardData.length; row++)
        {
            for (int col = 0; col < boardData[0].length; col++)
            {
                boardData[row][col] = BoardSquare.WHITE;
            }
        }
        boardData[0][0] = BoardSquare.GREEN;
        boardData[0][1] = BoardSquare.GREEN;
        numberRemaining = 2;
        output.pushUpdate(boardData, numberRemaining, turns);
    }

    /**
     * Reset the board with random values.
     */
    private void reset()
    {
        for (int row = 0; row < boardData.length; row++)
        {
            for (int col = 0; col < boardData[0].length; col++)
            {
                //0 is empty, add one to ensure > 0,
                // subtract one to prevent going over max,
                boardData[row][col] =
                        BoardSquare.values()[(int)(1 + Math.round(Math.random() * (kMaxData - 1)))];
            }
        }
        //store the new starting board
        for (int row = 0; row < boardData.length; row++)
        {
            startBoard[row] = boardData[row].clone();
        }
        numberRemaining = boardData.length * boardData[0].length;
        turns = 0;
    }
}
