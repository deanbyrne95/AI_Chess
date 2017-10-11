class Move
{
    private Square start, landing;

    Move(Square x, Square y)
    {
        start = x;
        landing = y;
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
