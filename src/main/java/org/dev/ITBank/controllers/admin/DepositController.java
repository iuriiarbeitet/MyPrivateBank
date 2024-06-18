package org.dev.ITBank.controllers.admin;

import org.dev.ITBank.models.Client;
import org.dev.ITBank.models.Model;
import org.dev.ITBank.views.ClientCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepositController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(DepositController.class.getName());

    public TextField pAddress_fld;
    public Button search_btn;
    public ListView<Client> result_listview;
    public TextField amount_fld;
    public Button deposit_btn;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(actionEvent -> onClientSearch());
        deposit_btn.setOnAction(e -> onDeposit());
    }

    private void onClientSearch() {
        ObservableList<Client> searchResult = Model.getInstance().getResultSearchClient(pAddress_fld.getText());
        if (!searchResult.isEmpty()) {
            result_listview.setItems(searchResult);
            result_listview.setCellFactory(e -> new ClientCellFactory());
            client = searchResult.get(0);
        } else {
            LOGGER.log(Level.SEVERE, "Failed to search result is empty: " + pAddress_fld.getText());
            searchResult = FXCollections.emptyObservableList();
            result_listview.setItems(searchResult);
            result_listview.setCellFactory(e -> new ClientCellFactory());
            client = null;
        }
    }

    private void onDeposit() {
        if (client != null) {
            double amount = Double.parseDouble(amount_fld.getText());
            if (amount_fld.getText() != null) {
                Model.getInstance().getDataClientQuery().updateBalanceSavingsAccounts(client.getPayeeAddress(), amount, "ADD");
                client.savingsAccountProperty().get().setBalance(Model.getInstance().getDataClientQuery().getSavingsAccountBalance(client.getPayeeAddress()));
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed: client is null: " + pAddress_fld.getText());
        }
        emptyFields();
    }

    private void emptyFields() {
        pAddress_fld.setText("");
        amount_fld.setText("");
    }

}
