package org.dev.ITBank.dao;


import org.dev.ITBank.utilities.DatabaseConnect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataClientQuery {
    private static final Logger LOGGER = Logger.getLogger(DataClientQuery.class.getName());

    public ResultSet getClientData(String pAddress, String password) {
        String queryDate = "SELECT * FROM Clients WHERE PayeeAddress='" + pAddress + "' AND Password='" + password + "';";
        return DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect(queryDate);
    }

    public ResultSet getTransactions(String pAddress) {
        String queryDate = "SELECT * FROM Transactions WHERE Sender='" + pAddress + "' OR Receiver='" + pAddress + "';";
        return DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect(queryDate);
    }


    // Method returns savings account balance
    public double getSavingsAccountBalance(String pAddress) {
        String queryDate = "SELECT * FROM SavingsAccounts WHERE Owner='" + pAddress + "';";
        double balance = 0;
        try {
            balance = DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect(queryDate).getDouble("Balance");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to return from database balance savings account " + pAddress, e);
        }
        return balance;
    }
    public double getCheckingAccountBalance(String pAddress) {
        String queryDate = "SELECT * FROM CheckingAccounts WHERE Owner='" + pAddress + "';";
        double balance = 0;
        try {
            balance = DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect(queryDate).getDouble("Balance");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to return from database balance checking accounts " + pAddress, e);
        }
        return balance;
    }


    // Method to either add or subtract from balance given operation
    public void updateBalanceSavingsAccounts(String pAddress, double amount, String operation) {
        try {
            ResultSet resultSet = DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect("SELECT * FROM SavingsAccounts WHERE Owner='" + pAddress + "';");
            double newBalance;
            if (operation.equals("ADD")) {
                newBalance = resultSet.getDouble("Balance") + amount;
                DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate("UPDATE SavingsAccounts SET Balance='" + newBalance + "' WHERE Owner='" + pAddress + "';");
            } else  if (operation.equals("SUB")) {
                if (resultSet.getDouble("Balance") >= amount) {
                    newBalance = resultSet.getDouble("Balance") - amount;
                    DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate("UPDATE SavingsAccounts SET Balance='" + newBalance + "' WHERE Owner='" + pAddress + "';");
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update balance savings accounts " + pAddress, e);
        }
    }
    public void updatesBalanceCheckingAccount(String pAddress, double amount, String operation) {
        try {
            ResultSet resultSet = getCheckingAccountData(pAddress);
            double newBalance;
            if (operation.equals("ADD")) {
                newBalance = resultSet.getDouble("Balance") + amount;
                DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate("UPDATE CheckingAccounts SET Balance='" + newBalance + "' WHERE Owner='" + pAddress + "';");
            } else  if (operation.equals("SUB")) {
                if (resultSet.getDouble("Balance") >= amount) {
                    newBalance = resultSet.getDouble("Balance") - amount;
                    DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate("UPDATE CheckingAccounts SET Balance='" + newBalance + "' WHERE Owner='" + pAddress + "';");
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update balance checking accounts " + pAddress, e);
        }
    }

    // Creates and records new transaction
    public void newTransaction(String sender, String receiver, double amount, String message) {
        LocalDate date = LocalDate.now();
        DatabaseConnect.getInstance().getResultsExecuteQueryDataUpdate( "INSERT INTO " + "Transactions(Sender, Receiver, Amount, Date, Message) " +
                "VALUES ('" + sender    + "', " +
                        "'" + receiver  + "', " +
                        "'" + amount    + "', " +
                        "'" + date      + "', " +
                        "'" + message   + "');");
    }

    public ResultSet searchClient(String pAddress) {
        return DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect("SELECT * FROM Clients where PayeeAddress='" + pAddress + "'");
    }

    public ResultSet getCheckingAccountData(String pAddress) {
        return DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect("SELECT * FROM CheckingAccounts WHERE Owner='" + pAddress + "';");
    }

    public ResultSet getSavingsAccountData(String pAddress) {
        return DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect("SELECT * FROM SavingsAccounts WHERE Owner='" + pAddress + "';");
    }

    public int getLastClientsId() {
        int id = 0;
        var queryData = "SELECT * FROM sqlite_sequence WHERE name='Clients'";
        try {
            id = DatabaseConnect.getInstance().getResultsExecuteQueryDataSelect(queryData).getInt("seq");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to return id from lasted client " , e);
        }
        return id;
    }
}
