class Square
{
    public int xCoOrdinate;
    public int yCoOrdinate;
    public String pieceName;

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

    public int getXCoOrdinate()
    {
        return xCoOrdinate;
    }

    public int getYCoOrdinate()
    {
        return yCoOrdinate;
    }

    public String getPieceName()
    {
        return pieceName;
    }
}
