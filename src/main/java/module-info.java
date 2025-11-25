module com.aristel {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.aristel to javafx.fxml;
    exports com.aristel;
}
