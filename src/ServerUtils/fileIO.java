package ServerUtils;

import Model.BankAccount;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    
    public void wrBankTransactionData(String dataStr) {
        FileWriter fwg = null;
        try {
            fwg = new FileWriter("bankTransactions.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter bwg = new BufferedWriter(fwg);
        PrintWriter outg = new PrintWriter(bwg);

        outg.println(dataStr);

        outg.close();
    }

    public void updateBankAccounts(Hashtable<String, BankAccount> bankAccountHashtable) {
        FileWriter fwg = null;
        try {
            fwg = new FileWriter("bankAccounts.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter bwg = new BufferedWriter(fwg);
        PrintWriter outg = new PrintWriter(bwg);

        ArrayList<String> bankAccountsList = new ArrayList<>();

        for (var bankAccount: bankAccountHashtable.values()) {
            bankAccountsList.add(bankAccount.toString());
        }

        outg.println(String.join("\n", bankAccountsList));

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

                bankAccountsHash.put(args[3], new BankAccount(args[0], args[1], args[2], args[3]));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return new Hashtable<String, BankAccount>();
        } catch (IOException ex) {
            ex.printStackTrace();
            return new Hashtable<String, BankAccount>();
        }

        return bankAccountsHash;
    }
}