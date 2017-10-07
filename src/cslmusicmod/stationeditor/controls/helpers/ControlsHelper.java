package cslmusicmod.stationeditor.controls.helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public final class ControlsHelper {

    private ControlsHelper() {

    }

    public static void initControl(Node node) {
        initControl(node, node.getClass().getSimpleName() + ".fxml");
    }

    public static void initControl(Node node, String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(node.getClass().getResource(fxml));
        fxmlLoader.setRoot(node);
        fxmlLoader.setController(node);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
