package org.dev.ITBank.controllers.admin;

import org.dev.ITBank.models.Model;
import org.dev.ITBank.views.ViewFactory;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;
    private final ViewFactory viewFactory = Model.getInstance().getViewFactory();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       viewFactory.getAdminSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
           switch (newVal) {
               case CLIENTS -> admin_parent.setCenter(viewFactory.getClientsView());
               case DEPOSIT -> admin_parent.setCenter(viewFactory.getDepositView());
               default -> admin_parent.setCenter(viewFactory.getCreateClientView());
           }
        });

    }
}
