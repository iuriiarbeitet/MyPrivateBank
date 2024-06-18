package org.dev.ITBank.controllers.client;

import org.dev.ITBank.dao.DataClientQuery;
import org.dev.ITBank.models.Client;
import org.dev.ITBank.models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit; // TODO
    public Label ch_acc_date; // TODO
    public Label ch_acc_bal;
    public Label sv_acc_num;
    public Label withdrawal_limit; // TODO
    public Label sv_acc_date; // TODO
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public Button trans_to_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_ch_btn;

    private final Client client = Model.getInstance().getClient();
    private final DataClientQuery dataClientQuery = Model.getInstance().getDataClientQuery();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindDate();
        trans_to_sv_btn.setOnAction(actionEvent -> onSendMoneyToSavingsAccount());
        trans_to_ch_btn.setOnAction(actionEvent -> onSendMoneyToCheckingAccount());
    }

    private void bindDate() {
        ch_acc_num.textProperty().bind(client.checkingAccountProperty().get().accountNumberProperty());
        ch_acc_bal.textProperty().bind(client.checkingAccountProperty().get().balanceProperty().asString());
        sv_acc_num.textProperty().bind(client.savingsAccountProperty().get().accountNumberProperty());
        sv_acc_bal.textProperty().bind(client.savingsAccountProperty().get().balanceProperty().asString());
    }

    private void onSendMoneyToSavingsAccount(){
        updateBalanceAccounts(amount_to_sv, "ADD", "SUB");
    }
    private void onSendMoneyToCheckingAccount(){
        updateBalanceAccounts(amount_to_ch, "SUB", "ADD");
    }
    private void updateBalanceAccounts(TextField amount, String svADD, String chADD){
        String pAddress = client.getPayeeAddress();
        double amounts = Double.parseDouble(amount.getText());
        dataClientQuery.updateBalanceSavingsAccounts(pAddress, amounts, svADD);
        dataClientQuery.updatesBalanceCheckingAccount(pAddress, amounts, chADD);
        amount.setText("");
        client.savingsAccountProperty().get().setBalance(dataClientQuery.getSavingsAccountBalance(pAddress));
        client.checkingAccountProperty().get().setBalance(dataClientQuery.getCheckingAccountBalance(pAddress));
    }
}
