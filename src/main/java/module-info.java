module com.soccer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.soccer to javafx.fxml;
    exports com.soccer;
}
