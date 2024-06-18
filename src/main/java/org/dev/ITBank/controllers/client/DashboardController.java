package org.dev.ITBank.controllers.client;

import org.dev.ITBank.dao.DataClientQuery;
import org.dev.ITBank.models.Client;
import org.dev.ITBank.models.Transaction;
import org.dev.ITBank.models.Model;
import org.dev.ITBank.views.TransactionCellFactory;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(DataClientQuery.class.getName());

    public Text user_name;
    public Label login_date;
    public Label checking_bal;
    public Label checking_acc_num;
    public Label savings_bal;
    public Label savings_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView<Transaction> transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public TextArea message_fld;
    public Button send_money_btn;

    private final Client client = Model.getInstance().getClient();
    private final DataClientQuery dataClientQuery = Model.getInstance().getDataClientQuery();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindDate();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
        send_money_btn.setOnAction(actionEvent -> onSendMoney());
        accountSummary();
    }

    private void bindDate() {
        user_name.textProperty().bind(Bindings.concat("Hi ").concat(client.firstNameProperty()));
        login_date.setText("Today, " + LocalDate.now());
        checking_bal.textProperty().bind(client.checkingAccountProperty().get().balanceProperty().asString());
        checking_acc_num.textProperty().bind(client.checkingAccountProperty().get().accountNumberProperty());
        savings_bal.textProperty().bind(client.savingsAccountProperty().get().balanceProperty().asString());
        savings_acc_num.textProperty().bind(client.savingsAccountProperty().get().accountNumberProperty());
    }

    private void onSendMoney() {
        String receiver = payee_fld.getText();
        double amount = Double.parseDouble(amount_fld.getText());
        String message = message_fld.getText();
        String sender = client.getPayeeAddress();
        ResultSet resultSet = dataClientQuery.searchClient(receiver);
        try {
            if (resultSet.isBeforeFirst()) {
                dataClientQuery.updateBalanceSavingsAccounts(receiver, amount, "ADD");
                // Subtract from sender's savings account
                dataClientQuery.updateBalanceSavingsAccounts(sender, amount, "SUB");
                // Update the savings account balance in the client object
                client.savingsAccountProperty().get().setBalance(dataClientQuery.getSavingsAccountBalance(sender));
                // Record new transaction
                dataClientQuery.newTransaction(sender, receiver, amount, message);
                Model.getInstance().updateListTransactions();
                accountSummary();
            } else {
                System.out.println("This " + receiver + " does not exist");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to send money : " + receiver  , e);
        }

        // Clear the fields
        payee_fld.setText("");
        amount_fld.setText("");
        message_fld.setText("");
    }

     private void accountSummary() {
        income_lbl.setText("+ $" + Model.getInstance().getIncomeBalanceTransactions());
        expense_lbl.setText("- $" + Model.getInstance().getExpensesBalanceTransactions());
    }
}
