package ServerUtils;

public class kiosk
{
    String name;
    int transactionCount;
    int totalTickets;
    double totalDollars;

    public kiosk (String n, int tr, int ticks, double val)
    {
        name             = n;
        transactionCount = tr;
        totalTickets     = ticks;
        totalDollars     = val;
    }

    public String toString()
    {
        return name + " : " + "TransCount = " + transactionCount + "  Tickets = " + totalTickets +
                "  Dollars = " + totalDollars;
    }

    public void incrementTrans()
    {
        transactionCount++;
    }

    public void addTickets(int c)
    {
        totalTickets = totalTickets + c;
    }

    public void addDollars(double d)
    {
        totalDollars = totalDollars + d;
    }
}