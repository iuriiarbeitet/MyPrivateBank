package org.dev.ITBank.models;

import org.dev.ITBank.dao.DataAdminQuery;
import org.dev.ITBank.dao.DataClientQuery;
import org.dev.ITBank.views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class Model {
    private static final Logger LOGGER = Logger.getLogger(Model.class.getName());
    private static Model model;
    private final ViewFactory viewFactory;
    private final DataAdminQuery dataAdminQuery;
    private final DataClientQuery dataClientQuery;

    private boolean adminLoginSuccessFlag;

    private final ObservableList<Client> clients;
    private ObservableList<Client> resultSearchClient;


    // Client Data
    private final Client client;
    private boolean clientLoginSuccessFlag;
    private final ObservableList<Transaction> latestTransactions;
    private final ObservableList<Transaction> allTransactions;

    public Model() {
        this.viewFactory = new ViewFactory();
        this.dataAdminQuery = new DataAdminQuery();
        this.dataClientQuery = new DataClientQuery();
        this.adminLoginSuccessFlag = false;
        this.clients = FXCollections.observableArrayList();
        this.resultSearchClient = FXCollections.observableArrayList();
        setClients();
        sortByNameCollections(clients);

        this.clientLoginSuccessFlag = false;
        this.client = new Client("", "", "", null, null, null);
        this.latestTransactions = FXCollections.observableArrayList();
        this.allTransactions = FXCollections.observableArrayList();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public void evaluateAdminCred(String username, String password) {
        ResultSet resultSet = dataAdminQuery.getAdminData(username, password);
        try {
            if (resultSet.isBeforeFirst()) {
                this.adminLoginSuccessFlag = true;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to evaluate admin credential: " + username, e);
        }
    }

    public void setClients() {
        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;
        ResultSet resultSet = dataAdminQuery.getAllClientsData();
        try {
            while (resultSet.next()) {
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String pAddress = resultSet.getString("PayeeAddress");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                checkingAccount = getCheckingAccount(pAddress);
                savingsAccount = getSavingAccount(pAddress);
                clients.add(new Client(fName, lName, pAddress, checkingAccount, savingsAccount, date));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to setClients from database ", e);
        }
    }

    public ObservableList<Client> searchClient(String pAddress) {
        ObservableList<Client> searchResult = FXCollections.observableArrayList();
        ResultSet resultSet = dataClientQuery.searchClient(pAddress);
        try {
            CheckingAccount checkingAccount = getCheckingAccount(pAddress);
            SavingsAccount savingsAccount = getSavingAccount(pAddress);
            String fName = resultSet.getString("FirstName");
            String lName = resultSet.getString("LastName");
            String[] dateParts = resultSet.getString("Date").split("-");
            LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
            searchResult.add(new Client(fName, lName, pAddress, checkingAccount, savingsAccount, date));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to search client: " + pAddress, e);
        }
        return searchResult;
    }

    public ObservableList<Client> getResultSearchClient(String pAddress) {
        searchClientObsListView(pAddress);
        return resultSearchClient;
    }

    public void searchClientObsListView(String pAddress) {
        resultSearchClient = FXCollections.observableList(
                clients.stream()
                        .filter(cl -> cl.pAddressProperty().get().equals(pAddress))
                        .collect(Collectors.toList())
        );
    }

    public void addClientObsListView(String payeeAddress) {
        clients.add(searchClient(payeeAddress).get(0));
        sortByNameCollections(clients);
    }

    public void deleteClientObsListView(Client client) {
        clients.remove(client);
        resultSearchClient.remove(client);
    }

    public boolean getAdminLoginSuccessFlag() {
        return this.adminLoginSuccessFlag;
    }

    /*
     * Utility Methods Section
     */
    public void sortByNameCollections(ObservableList<Client> clients) {
        Comparator<Client> nameComparator = Comparator.comparing(Client::getFirstName);
        FXCollections.sort(clients, nameComparator);
    }

    public CheckingAccount getCheckingAccount(String pAddress) {
        CheckingAccount account = null;
        ResultSet resultSet = dataClientQuery.getCheckingAccountData(pAddress);
        try {
            String num = resultSet.getString("AccountNumber");
            int tLimit = (int) resultSet.getDouble("TransactionLimit");
            double balance = resultSet.getDouble("Balance");
            account = new CheckingAccount(pAddress, num, balance, tLimit);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to return checking account: " + pAddress, e);
        }
        return account;
    }

    public SavingsAccount getSavingAccount(String pAddress) {
        SavingsAccount account = null;
        ResultSet resultSet = dataClientQuery.getSavingsAccountData(pAddress);
        try {
            String num = resultSet.getString("AccountNumber");
            int wLimit = (int) resultSet.getDouble("WithdrawalLimit");
            double balance = resultSet.getDouble("Balance");
            account = new SavingsAccount(pAddress, num, balance, wLimit);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to return savings account: " + pAddress, e);
        }
        return account;
    }

    // Client Data
    public void evaluateClientCred(String pAddress, String password) {
        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;
        ResultSet resultSet = Model.getInstance().getDataClientQuery().getClientData(pAddress, password);
        try {
            if (resultSet.isBeforeFirst()) {
                this.client.firstNameProperty().set(resultSet.getString("FirstName"));
                this.client.lastNameProperty().set(resultSet.getString("LastName"));
                this.client.pAddressProperty().set(resultSet.getString("PayeeAddress"));
                String[] dataParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dataParts[0]), Integer.parseInt(dataParts[1]), Integer.parseInt(dataParts[2]));
                this.client.dateProperty().set(date);
                checkingAccount = Model.getInstance().getCheckingAccount(pAddress);
                savingsAccount = Model.getInstance().getSavingAccount(pAddress);
                this.client.checkingAccountProperty().set(checkingAccount);
                this.client.savingsAccountProperty().set(savingsAccount);
                this.clientLoginSuccessFlag = true;
                setAllTransactions();
                setLatestTransactions();
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to evaluate client credentials: " + pAddress, e);
        }
    }

    private void prepareTransactions(ObservableList<Transaction> transactions) {
        ResultSet resultSet = Model.getInstance().getDataClientQuery().getTransactions(this.client.pAddressProperty().get());
        try {
            while (resultSet.next()) {
                String sender = resultSet.getString("Sender");
                String receiver = resultSet.getString("Receiver");
                double amount = resultSet.getDouble("Amount");
                String[] dataParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dataParts[0]), Integer.parseInt(dataParts[1]), Integer.parseInt(dataParts[2]));
                String message = resultSet.getString("Message");
                transactions.add(new Transaction(sender, receiver, amount, date, message));
            }
            sortByDateCollections(allTransactions);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to prepare transactions from database ", e);
        }
    }

    public void setAllTransactions() {
        prepareTransactions(this.allTransactions);
    }

    public void setLatestTransactions() {
        for (int i = 0; i < 4; i++) {
            if (!allTransactions.isEmpty()) {
                latestTransactions.add(allTransactions.get(i));
            }
        }
    }

    public void updateListTransactions() {
        allTransactions.clear();
        setAllTransactions();
        latestTransactions.clear();
        setLatestTransactions();
    }

    public void sortByDateCollections(ObservableList<Transaction> transactions) {
        FXCollections.reverse(transactions);
    }

    public double getIncomeBalanceTransactions() {
        return allTransactions.stream()
                .filter(t -> !(t.senderProperty().get().equals(getClient().pAddressProperty().get()))
                ).mapToDouble(t -> t.amountProperty().get()).sum();
    }

    public double getExpensesBalanceTransactions() {
        return allTransactions.stream()
                .filter(t -> t.senderProperty().get().equals(getClient().pAddressProperty().get())
                ).mapToDouble(t -> t.amountProperty().get()).sum();
    }

    public boolean getClientLoginSuccessFlag() {
        return this.clientLoginSuccessFlag;
    }

    public ViewFactory getViewFactory() {
        return this.viewFactory;
    }

    public DataAdminQuery getDataAdminQuery() {
        return this.dataAdminQuery;
    }

    public DataClientQuery getDataClientQuery() {
        return this.dataClientQuery;
    }

    public ObservableList<Client> getClients() {
        return this.clients;
    }

    public Client getClient() {
        return this.client;
    }

    public ObservableList<Transaction> getLatestTransactions() {
        return this.latestTransactions;
    }

    public ObservableList<Transaction> getAllTransactions() {
        return this.allTransactions;
    }

    public void setAdminLoginSuccessFlag(boolean adminLoginSuccessFlag) {
        this.adminLoginSuccessFlag = adminLoginSuccessFlag;
    }

    public void setClientLoginSuccessFlag(boolean clientLoginSuccessFlag) {
        this.clientLoginSuccessFlag = clientLoginSuccessFlag;
    }
}
