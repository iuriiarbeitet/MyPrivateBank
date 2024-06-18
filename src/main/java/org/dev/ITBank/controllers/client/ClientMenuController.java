package org.dev.ITBank.controllers.client;

import org.dev.ITBank.models.Model;
import org.dev.ITBank.utilities.ClientMenuOptions;
import org.dev.ITBank.views.ViewFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button accounts_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;
    private final ViewFactory viewFactory = Model.getInstance().getViewFactory();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        transaction_btn.setOnAction(event -> onTransactions());
        accounts_btn.setOnAction(event -> onAccounts());
        logout_btn.setOnAction(event -> onLogout());
    }

    private void onDashboard() {
        viewFactory.getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }
    private void onTransactions() {
        viewFactory.getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTIONS);
    }

    private void onAccounts() {
        viewFactory.getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }
    private void onLogout() {
        // Get Stage
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        // Close the client window
        viewFactory.closeStage(stage);
        // Show Login Window
        viewFactory.showLoginWindow();
        // Set Client Login Success Flag to false
        Model.getInstance().setClientLoginSuccessFlag(false);
    }
}
