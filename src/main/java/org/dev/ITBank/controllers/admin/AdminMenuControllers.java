package org.dev.ITBank.controllers.admin;

import org.dev.ITBank.models.Model;
import org.dev.ITBank.utilities.AdminMenuOptions;
import org.dev.ITBank.views.ViewFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuControllers implements Initializable {
    public Button create_client_btn;
    public Button clients_btn;
    public Button deposit_btn;
    public Button logout_btn;
    private final ViewFactory viewFactory = Model.getInstance().getViewFactory();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners(){
        create_client_btn.setOnAction(actionEvent -> onCreateClient());
        clients_btn.setOnAction(actionEvent -> onClients());
        deposit_btn.setOnAction(actionEvent -> onDeposit());
        logout_btn.setOnAction(actionEvent -> onLogout());
    }

    private void onCreateClient() {
        viewFactory.getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENT);
    }

    private void onClients() {
        viewFactory.getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
    }

    private void onDeposit() {
        viewFactory.getAdminSelectedMenuItem().set(AdminMenuOptions.DEPOSIT);
    }

    private void onLogout() {
        // Get Stage
        Stage stage = (Stage) clients_btn.getScene().getWindow();
        // Close the client window
        viewFactory.closeStage(stage);
        // Show Login Window
        viewFactory.showLoginWindow();
        // Set Admin Login Success Flag to false
        Model.getInstance().setAdminLoginSuccessFlag(false);
    }
}
