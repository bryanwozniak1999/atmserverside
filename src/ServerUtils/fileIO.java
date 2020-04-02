package ServerUtils;

import Model.BankAccount;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class fileIO
{
    public void wrTransactionData(String dataStr)
    {
        FileWriter fwg = null;
        try
        {
            // open the file in append write mode
            fwg = new FileWriter("transactionLog.txt", true);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BufferedWriter bwg = new BufferedWriter(fwg);
        PrintWriter outg   = new PrintWriter(bwg);

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy HH.mm.ss").format(new Date());

        outg.println(timeStamp + " : " + dataStr);

        outg.close();
    }

    public void wrBankAccountData(String dataStr) {
        FileWriter fwg = null;
        try {
            fwg = new FileWriter("bankAccounts.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter bwg = new BufferedWriter(fwg);
        PrintWriter outg = new PrintWriter(bwg);

        outg.println(dataStr);

        outg.close();
    }

    public Hashtable<String, BankAccount> readBankAccountData() {
        Hashtable<String, BankAccount> bankAccountsHash =
                new Hashtable<String, BankAccount>();

        try {
            File bankAccounts = new File("bankAccounts.txt");

            BufferedReader br = new BufferedReader(new FileReader(bankAccounts));
            String line;

            while ((line = br.readLine()) != null) {
                String args[] = line.split("\\,");

                bankAccountsHash.put(args[2], new BankAccount(args[0], args[1], args[2]));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return bankAccountsHash;
    }
}