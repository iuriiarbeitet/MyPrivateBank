package org.dev.ITBank.controllers.admin;

import org.dev.ITBank.models.Client;
import org.dev.ITBank.models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsCellController implements Initializable {

    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label sv_acc_lbl;
    public Label data_lbl;
    public Button delete_btn;

    private final Client client;

    public ClientsCellController(Client client) {
        this.client = client;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fName_lbl.textProperty().bind(client.firstNameProperty());
        lName_lbl.textProperty().bind(client.lastNameProperty());
        pAddress_lbl.textProperty().bind(client.pAddressProperty());
        ch_acc_lbl.textProperty().bind(client.checkingAccountProperty().asString());
        sv_acc_lbl.textProperty().bind(client.savingsAccountProperty().asString());
        data_lbl.textProperty().bind(client.dateProperty().asString());
        delete_btn.setOnAction(event -> onDelete());
    }

    private void onDelete() {
        Model.getInstance().getDataAdminQuery().deleteClient(pAddress_lbl.getText());
        Model.getInstance().deleteClientObsListView(getClient());
    }

    public Client getClient() {
        return this.client;
    }
}
