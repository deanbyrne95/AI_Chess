public class Move
{
    Square start, landing;

    public Move(Square x, Square y)
    {
        start = x;
        landing = y;
    }

    public Move()
    {

    }

    Square getStart()
    {
        return start;
    }

    Square getLanding()
    {
        return landing;
    }
}
