module com.example.photoedit {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.kordamp.bootstrapfx.core;
    requires opencv;
    requires java.desktop;
    requires javafx.swing;
    requires com.google.gson;

    opens com.example.photoedit to javafx.fxml, com.google.gson;
    exports com.example.photoedit;
}