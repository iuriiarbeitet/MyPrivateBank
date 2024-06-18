package org.dev.ITBank.controllers;

import org.dev.ITBank.models.Model;
import org.dev.ITBank.utilities.AccountType;
import org.dev.ITBank.views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Label payee_address_lbl;
    public TextField payee_address_fld;
    public PasswordField password_fld;
    public Button login_btn;
    public Label error_lbl;
    public ChoiceBox<AccountType> acc_selector;
    private final ViewFactory viewFactory = Model.getInstance().getViewFactory();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        acc_selector.setValue(viewFactory.getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> setAcc_selector());
        login_btn.setOnAction(actionEvent -> onLogin());
    }

    private void onLogin() {
        Stage stage = (Stage) error_lbl.getScene().getWindow();

        if(viewFactory.getLoginAccountType() == AccountType.CLIENT){
            // Evaluate Client Login Credentials
            Model.getInstance().evaluateClientCred(payee_address_fld.getText(), password_fld.getText());
            if(Model.getInstance().getClientLoginSuccessFlag()) {
                viewFactory.showClientWindow();
                // Close the login stage
                viewFactory.closeStage(stage);
            } else {
                payee_address_fld.setText("");
                payee_address_fld.setText("");
                error_lbl.setText("No Such Login Credentials.");
            }
        } else {
            // Evaluate Admin Login Credentials
            Model.getInstance().evaluateAdminCred(payee_address_fld.getText(), password_fld.getText());
            if(Model.getInstance().getAdminLoginSuccessFlag()) {
                viewFactory.showAdminWindow();
                // Close the login stage
                viewFactory.closeStage(stage);
            } else {
                payee_address_fld.setText("");
                payee_address_fld.setText("");
                error_lbl.setText("No Such Login Credentials.");
            }
        }
    }

    private void setAcc_selector() {
        viewFactory.setLoginAccountType(acc_selector.getValue());
        //Change Payee Address label accordingly
        if(acc_selector.getValue() == AccountType.ADMIN) {
            payee_address_lbl.setText("Username:");
        } else {
            payee_address_lbl.setText("Payee Address:");
        }
    }
}
