package Model;

public class BankAccount
{
    private String AccountName;
    private String AccountType;
    private String AccountId;
    private String Balance;

    public BankAccount(String accountName, String accountType, String balance, String accountId) {
        super();

        AccountType = accountType;
        AccountName = accountName;
        AccountId = accountId;
        Balance = balance;
    }

    public String toString()
    {
        return AccountName + "," + AccountType + "," + Balance + "," + AccountId;
    }
}