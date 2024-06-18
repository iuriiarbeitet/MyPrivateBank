package org.dev.ITBank.dao;

import org.dev.ITBank.utilities.DatabaseConnect;

import java.sql.ResultSet;
import java.time.LocalDate;

public class DataAdminQuery {

    public ResultSet getAdminData(String username, String password) {
        String queryDate = "SELECT * FROM Admins WHERE Username='" + username + "'AND Password='" + password + "';";
        return DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect(queryDate);
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date) {
        String queryDate = "INSERT INTO " + "Clients(FirstName, LastName, PayeeAddress, Password, Date)" +
                "VALUES ('" + fName + "', " +
                "'" + lName + "', " +
                "'" + pAddress + "', " +
                "'" + password + "', " +
                "'" + date.toString() + "')";
        DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate(queryDate);
    }
    public void deleteClient(String pAddress){
        DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate("DELETE FROM Clients WHERE PayeeAddress='" +pAddress+ "';");
        DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate("DELETE FROM CheckingAccounts WHERE Owner='" +pAddress+ "';");
        DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate("DELETE FROM SavingsAccounts WHERE Owner='" +pAddress+ "';");

    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance) {
        String queryDate = "INSERT INTO " + "CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance)" +
                "VALUES ('" + owner + "', " +
                "'" + number + "', " +
                "'" + tLimit + "', " +
                "'" + balance + "')";
        DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate(queryDate);
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance) {
        String queryDate = "INSERT INTO " + "SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance)" +
                "VALUES ('" + owner + "', " +
                "'" + number + "', " +
                "'" + wLimit + "', " +
                "'" + balance + "')";
        DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate(queryDate);
    }

    public ResultSet getAllClientsData() {
        return DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect("SELECT * FROM  Clients;");
    }

    public void depositSavings(String pAddress, double amount) {
        DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate("UPDATE SavingsAccounts SET Balance='" + amount + "' WHERE Owner='" + pAddress + "'");
    }


}
