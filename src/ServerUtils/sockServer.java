package ServerUtils;

import Model.BankAccount;
import UI.Main;

import java.io.IOException;
import java.io.BufferedReader;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class sockServer implements Runnable
{
    Socket csocket;
    String ipString;

    static Vector<String> vec = new Vector<String>(5);

    public static Hashtable<String, BankAccount> bankAccounts =
            new Hashtable<String, BankAccount>();

    static final String newline = "\n";
    static int first_time = 1;

    static int port_num = 3333;

    static int numOfConnections = 0;
    static int numOfMessages = 0;
    static int max_connections = 5;
    static int numOfTransactions = 0;

    sockServer(Socket csocket, String ip)
    {
        this.csocket  = csocket;
        this.ipString = ip;
    }

    public static void runSockServer()   // throws Exception
    {
        boolean sessionDone = false;

        ServerSocket ssock = null;

        try
        {
            ssock = new ServerSocket(port_num);
        }
        catch (BindException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // update the status text area to show progress of program
        try
        {
            InetAddress ipAddress = InetAddress.getLocalHost();
            Main.textArea.appendText("IP Address : " + ipAddress.getHostAddress() + newline);
        }
        catch (UnknownHostException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Main.textArea.appendText("Listening on port " + port_num + newline);

        //
        // initialize the bank accounts hash table with whats written in the file
        //
        bankAccounts = new fileIO().readBankAccountData();

        sessionDone = false;
        while (sessionDone == false)
        {
            Socket sock = null;
            try
            {
                // blocking system call
                sock = ssock.accept();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            // update the status text area to show progress of program
            Main.textArea.appendText("Client Connected : " + sock.getInetAddress() + newline);

            new Thread(new sockServer(sock, sock.getInetAddress().toString())).start();
        }

        try
        {
            ssock.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //
    // get all bank account data from the hash table keys
    //
    public static String getAllBankAccounts()
    {
        String rs="";

        List<String> v = new ArrayList<String>(bankAccounts.keySet());
        Collections.sort(v);

        for (String str : v)
            rs = rs + bankAccounts.get(str) + "\r\n\n";

        return rs;
    }

    // This is the thread code that ALL clients will run()
    public void run()
    {
        try
        {
            boolean session_done = false;
            long threadId;
            String clientString;
            String keyString = "";

            threadId = Thread.currentThread().getId();

            numOfConnections++;

            Main.textArea.appendText("Num of Connections = " + numOfConnections + newline);

            keyString = ipString + ":" + threadId;

            if (vec.contains(keyString) == false)
            {
                int counter = 0;
                vec.addElement(keyString);

                Main.textArea_2.setText("");
                Enumeration<String> en = vec.elements();
                while (en.hasMoreElements())
                {
                    Main.textArea_2.appendText(en.nextElement() + " || ");

                    if (++counter >= 6)
                    {
                        Main.textArea_2.appendText("\r\n");
                        counter = 0;
                    }
                }
            }

            PrintStream pstream = new PrintStream (csocket.getOutputStream());
            BufferedReader rstream = new BufferedReader(new InputStreamReader(csocket.getInputStream()));

            while (session_done == false)
            {
                if (rstream.ready())   // check for any data messages
                {
                    clientString = rstream.readLine();


                    //
                    // write to transaction log
                    //
                    fileIO transLog = new fileIO();
                    transLog.wrTransactionData(clientString);


                    // update the status text area to show progress of program
                    Main.textArea.appendText("RECV : " + clientString + newline);

                    // update the status text area to show progress of program
                    Main.textArea.appendText("RLEN : " + clientString.length() + newline);

                    if (clientString.length() > 128)
                    {
                        session_done = true;
                        continue;
                    }

                    if (clientString.contains("quit"))
                    {
                        session_done = true;
                    }
                    else if (clientString.contains("QUIT"))
                    {
                        session_done = true;
                    }
                    else if (clientString.contains("Quit"))
                    {
                        session_done = true;
                    }
                    else if (clientString.contains("BankAccountsQuery>"))
                    {
                        ArrayList<String> bankAccountsList = new ArrayList<>();

                        for (var bankAccount: bankAccounts.values()) {
                            bankAccountsList.add(bankAccount.toString());
                        }

                        pstream.println(String.join(">", bankAccountsList));
                    }
                    else if (clientString.contains("NewBankAccount>")) {
                        String tokens[] = clientString.split("\\>");
                        String args[] = tokens[2].split("\\,");

                        if (!bankAccounts.containsKey(args[2])) {
                            transLog.wrBankAccountData(String.join(",", args));
                            bankAccounts.put(args[2], new BankAccount(args[0], args[1], args[2]));
                        }
                    }
                    else if (clientString.contains("BankTransaction>")) {
                        String tokens[] = clientString.split("\\>");
                        String args[] = tokens[2].split("\\,");

                        if (bankAccounts.containsKey(args[3])) {
                            transLog.wrBankTransactionData(String.join(",", args));
                        }
                    }
                    else if (clientString.contains("Date>"))
                    {
                        numOfMessages++;

                        // Create an instance of SimpleDateFormat used for formatting
                        // the string representation of date (month/day/year)
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                        // Get the date today using Calendar object.
                        Date today = Calendar.getInstance().getTime();

                        // Using DateFormat format method we can create a string
                        // representation of a date with the defined format.
                        String reportDate = df.format(today);

                        // Print what date is today!
                        pstream.println("Num Of Messages : " + numOfMessages + "   Simple Date: " + reportDate);
                    }
                    else
                    {
                        pstream.println("NACK : ERROR : No such command!");
                    }
                }

                Thread.sleep(500);

            }    // end while loop

            keyString = ipString + ":" + threadId;

            if (vec.contains(keyString) == true)
            {
                int counter = 0;
                vec.removeElement(keyString);

                Main.textArea_2.setText("");
                Enumeration<String> en = vec.elements();
                while (en.hasMoreElements())
                {
                    Main.textArea_2.appendText(en.nextElement() + " || ");

                    if (++counter >= 6)
                    {
                        Main.textArea_2.appendText("\r\n");
                        counter = 0;
                    }
                }

                //sss5.textArea_2.repaint();
            }

            numOfConnections--;

            // close client socket
            csocket.close();

            // update the status text area to show progress of program
            Main.textArea.appendText("Child Thread : " + threadId + " : is Exiting!!!" + newline);
            Main.textArea.appendText("Num of Connections = " + numOfConnections);

        } // end try

        catch (SocketException e)
        {
            // update the status text area to show progress of program
            Main.textArea.appendText("ERROR : Socket Exception!" + newline);
        }
        catch (InterruptedException e)
        {
            // update the status text area to show progress of program
            Main.textArea.appendText("ERROR : Interrupted Exception!" + newline);
        }
        catch (UnknownHostException e)
        {
            // update the status text area to show progress of program
            Main.textArea.appendText("ERROR : Unkonw Host Exception" + newline);
        }
        catch (IOException e)
        {
            // update the status text area to show progress of program
            Main.textArea.appendText("ERROR : IO Exception!" + newline);
        }
        catch (Exception e)
        {
            numOfConnections--;

            // update the status text area to show progress of program
            Main.textArea.appendText("ERROR : Generic Exception!" + newline + e.toString());
        }

    }  // end run() thread method
}