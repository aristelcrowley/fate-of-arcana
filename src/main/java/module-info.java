module com.aristel {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics; 
    opens com.aristel.controllers to javafx.fxml;
    exports com.aristel;
}