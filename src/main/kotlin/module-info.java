module com.example.photoedit {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.photoedit to javafx.fxml;
    exports com.example.photoedit;
}