module org.dev.ITBank {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;



    opens org.dev.ITBank to javafx.fxml;
    exports org.dev.ITBank;
    exports org.dev.ITBank.controllers;
    exports org.dev.ITBank.controllers.admin;
    exports org.dev.ITBank.controllers.client;
    exports org.dev.ITBank.dao;
    exports org.dev.ITBank.models;
    exports org.dev.ITBank.utilities;
    exports org.dev.ITBank.views;


}