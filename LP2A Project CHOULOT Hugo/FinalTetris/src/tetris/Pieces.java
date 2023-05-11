package tetris;

//CHOULOT Hugo

import java.util.Random;

public class Pieces {

    protected enum Tetrominoes { NoShape, ZShape, SShape, LineShape, TShape,
        SquareShape, LShape, MirroredLShape }

    private Tetrominoes pieceShape;
    private int[][] coords;
    private int[][][] coordsTable;

    public Pieces() {

        initPieces();
    }

    private void initPieces() {

        coords = new int[4][2];

        coordsTable = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        setPieces(Tetrominoes.NoShape);
    }

    protected void setPieces(Tetrominoes shape) {

        for (int i = 0; i < 4 ; i++) {

            for (int j = 0; j < 2; ++j) {

                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }

        pieceShape = shape;
    }

    public Tetrominoes getPieces()  {

        return pieceShape;
    }

    private void setX(int index, int x) {

        coords[index][0] = x;
    }
    private void setY(int index, int y) {

        coords[index][1] = y;
    }
    public int x(int index) {

        return coords[index][0];
    }
    public int y(int index) {

        return coords[index][1];
    }

    public int minY() {

        int m = coords[0][1];

        for (int i=0; i < 4; i++) {

            m = Math.min(m, coords[i][1]);
        }

        return m;
    }
    public void setRandomShapes() {

        var r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;

        Tetrominoes[] values = Tetrominoes.values();
        setPieces(values[x]);
    }

    public Pieces rotateAntiClockwise() {

        if (pieceShape == Tetrominoes.SquareShape) {

            return this;
        }

        var result = new Pieces();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {

            result.setX(i, y(i));
            result.setY(i, -x(i));
        }

        return result;
    }

    public Pieces rotateClockwise() {

        if (pieceShape == Tetrominoes.SquareShape) {

            return this;
        }

        var result = new Pieces();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {

            result.setX(i, -y(i));
            result.setY(i, x(i));
        }

        return result;
    }

}
