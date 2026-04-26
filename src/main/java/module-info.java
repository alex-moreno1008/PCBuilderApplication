module org.example.pcbuilderapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pcbuilderapplication to javafx.fxml;
    exports org.example.pcbuilderapplication;
    exports org.example.pcbuilderapplication.controllers;
    opens org.example.pcbuilderapplication.controllers to javafx.fxml;
}