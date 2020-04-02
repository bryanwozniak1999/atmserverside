package Model;

public class BankAccount
{
    private String AccountName;
    private String AccountType;
    private String AccountId;

    public BankAccount(String accountName, String accountType, String accountId) {
        super();

        AccountType = accountType;
        AccountName = accountName;
        AccountId = accountId;
    }

    public String toString()
    {
        return AccountId + ": " + "AccountName = " + AccountName + "," + "AccountType = " + AccountType.toString();
    }
}