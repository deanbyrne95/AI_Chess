class Square
{
    private int xCoOrdinate;
    private int yCoOrdinate;
    private String pieceName;

    public Square(int x, int y)
    {
        xCoOrdinate = x;
        yCoOrdinate = y;
        pieceName = "";
    }

    public Square(int x, int y, String name)
    {
        xCoOrdinate = x;
        yCoOrdinate = y;
        pieceName = name;
    }

    int getXCoOrdinate()
    {
        return xCoOrdinate;
    }

    int getYCoOrdinate()
    {
        return yCoOrdinate;
    }

    String getPieceName()
    {
        return pieceName;
    }
}
