package tetris;

//CHOULOT Hugo

import tetris.Pieces.Tetrominoes;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Grid extends JPanel{

    private final int GRID_WIDTH = 10;
    private final int GRID_HEIGHT = 20;

    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private int scored;

    private JLabel lines;
    private JLabel paused;
    private JLabel score;

    private Timer timer;
    private Pieces curPiece;
    private Tetrominoes[] grid;

    public Grid(Game launch) {

        initGrid(launch);
    }

    private void initGrid(Game launch) {

        setFocusable(true);
        lines = launch.getLines();
        paused = launch.getPaused();
        score = launch.getScore();
        addKeyListener(new TAdapter());
    }

    private void newPiece() {

        curPiece.setRandomShapes();
        curX = GRID_WIDTH / 2 + 1;
        curY = GRID_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {

            curPiece.setPieces(Tetrominoes.NoShape);
            timer.stop();
            var msg = String.format("Game over. Score: %d ", scored);
            paused.setText(msg);
        }
    }
    void start(int period) {

        curPiece = new Pieces();
        grid = new Tetrominoes[GRID_WIDTH * GRID_HEIGHT];
        clearGrid();
        newPiece();
        timer = new Timer(period, new GameRound());
        timer.start();
    }

    public void pause() {

        isPaused = !isPaused;

        if (isPaused) {

            paused.setText("paused");

        } else {

            paused.setText("");
        }

        repaint();
    }

    private class GameRound implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            doGameRound();
        }
    }

    private void doGameRound() {

        update();
        repaint();
    }

    private void update() {

        if (isPaused) {

            return;
        }

        if (isFallingFinished) {

            isFallingFinished = false;
            newPiece();

        } else {

            speedUpDown();
        }
    }

    class TAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            if (curPiece.getPieces() == Tetrominoes.NoShape) {

                return;
            }

            int keycode = e.getKeyCode();

            switch (keycode) {

                case KeyEvent.VK_LEFT -> tryMove(curPiece, curX - 1, curY);
                case KeyEvent.VK_RIGHT -> tryMove(curPiece, curX + 1, curY);
                case KeyEvent.VK_DOWN -> tryMove(curPiece.rotateClockwise(), curX, curY);
                case KeyEvent.VK_UP -> tryMove(curPiece.rotateAntiClockwise(), curX, curY);
                case KeyEvent.VK_SPACE -> dropDown();
                case KeyEvent.VK_D -> speedUpDown();
                case KeyEvent.VK_P -> pause();
            }
        }

    }

    private void dropDown() {

        int newY = curY;

        while (newY > 0) {

            if (!tryMove(curPiece, curX, newY - 1)) {

                break;
            }

            newY--;
        }

        pieceDropped();
    }

    private void speedUpDown() {

        if (!tryMove(curPiece, curX, curY - 1)) {

            pieceDropped();
        }
    }

    private void clearGrid() {

        for (int i = 0; i < GRID_HEIGHT * GRID_WIDTH; i++) {

            grid[i] = Tetrominoes.NoShape;
        }
    }

    private void pieceDropped() {

        for (int i = 0; i < 4; i++) {

            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            grid[(y * GRID_WIDTH) + x] = curPiece.getPieces();
        }

        removeFullLines();

        if (!isFallingFinished) {

            newPiece();
        }
    }


    private boolean tryMove(Pieces newPiece, int newX, int newY) {

        for (int i = 0; i < 4; i++) {

            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {

                return false;
            }

            if (shapeAt(x, y) != Tetrominoes.NoShape) {

                return false;
            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void removeFullLines() {

        int numFullLines = 0;
        int scores = 0;

        //boolean oneTetris = false;

        for (int i = GRID_HEIGHT - 1; i >= 0; i--) {

            boolean lineIsFull = true;

            for (int j = 0; j < GRID_WIDTH; j++) {

                if (shapeAt(j, i) == Tetrominoes.NoShape) {

                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {

                numFullLines++;

                //if(numFullLines == 4 || oneTetris){
                //scores = scores + 400;
                //}

                if(numFullLines == 4){

                    //oneTetris = true;

                    scores = scores + 500;

                } else {

                    //oneTetris = false;

                    scores = scores + 100;
                }

                for (int k = i; k < GRID_HEIGHT - 1; k++) {

                    for (int j = 0; j < GRID_WIDTH; j++) {

                        grid[(k * GRID_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {

            numLinesRemoved += numFullLines;
            lines.setText(" Lines : "+String.valueOf(numLinesRemoved)+" ");
            scored += scores;
            score.setText(" Score = "+String.valueOf(scored)+" ");
            isFallingFinished = true;
            curPiece.setPieces(Tetrominoes.NoShape);
        }
    }

    //Graphic part
    private int squareWidth() {

        return (int) getSize().getWidth() / GRID_WIDTH;
    }

    private int squareHeight() {

        return (int) getSize().getHeight() / GRID_HEIGHT;
    }

    private Tetrominoes shapeAt(int x, int y) {

        return grid[(y * GRID_WIDTH) + x];
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.setColor(new Color(47,79,79));
        int uniteX = getWidth()/10;
        int uniteY = getHeight()/20;
        for(int i=0; i<20; i++){
            g.drawLine(uniteX*i, 0, uniteX*i, getHeight());
            g.drawLine(0, uniteY*i, getWidth(), uniteY*i);
        }
        drawingAll(g);
    }

    private void drawingAll(Graphics g) {

        var size = getSize();
        int gridTop = (int) size.getHeight() - GRID_HEIGHT * squareHeight();

        for (int i = 0; i < GRID_HEIGHT; i++) {

            for (int j = 0; j < GRID_WIDTH; j++) {

                Tetrominoes shape = shapeAt(j, GRID_HEIGHT - i - 1);

                if (shape != Tetrominoes.NoShape) {

                    drawShapes(g, j * squareWidth(), gridTop + i * squareHeight(), shape);
                }
            }
        }

        if (curPiece.getPieces() != Tetrominoes.NoShape) {

            for (int i = 0; i < 4; i++) {

                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);

                drawShapes(g, x * squareWidth(), gridTop + (GRID_HEIGHT - y - 1) * squareHeight(), curPiece.getPieces());
            }
        }
    }


    private void drawShapes(Graphics g, int x, int y, Tetrominoes shape) {

        Color colors[] = {new Color(0, 0, 0), Color.RED,
                Color.GREEN, Color.CYAN, Color.MAGENTA,
                Color.YELLOW, Color.ORANGE, Color.BLUE };

        var color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.darker());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
    }

}
