package org.dev.ITBank.controllers.client;

import org.dev.ITBank.models.Model;
import org.dev.ITBank.views.ViewFactory;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public BorderPane client_parent;
    private final ViewFactory viewFactory = Model.getInstance().getViewFactory();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       viewFactory.getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case TRANSACTIONS -> client_parent.setCenter(viewFactory.getTransactionsView());
                case ACCOUNTS -> client_parent.setCenter(viewFactory.getAccountsView());
                            default -> client_parent.setCenter(viewFactory.getDashboardView());
            }
        });
    }
}
