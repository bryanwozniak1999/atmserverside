package Model;

public class BankAccount
{
    private String AccountName;
    private String AccountType;
    private String AccountId;
    private String Balance;
    private String UserId;

    public BankAccount(String accountName, String accountType, String balance, String accountId, String userId) {
        super();

        AccountType = accountType;
        AccountName = accountName;
        AccountId = accountId;
        Balance = balance;
        UserId = userId;
    }

    public void SetBalance(String balance) {
        this.Balance = balance;
    }

    public String GetUserId() {
        return this.UserId;
    }

    public String toString()
    {
        return AccountName + "," + AccountType + "," + Balance + "," + AccountId + "," + UserId;
    }
}