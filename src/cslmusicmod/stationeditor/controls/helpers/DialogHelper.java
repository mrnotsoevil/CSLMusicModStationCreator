package cslmusicmod.stationeditor.controls.helpers;

import javafx.scene.control.Alert;

public final class DialogHelper {

    private DialogHelper() {

    }

    public static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
