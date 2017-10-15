package cslmusicmod.stationeditor.helpers;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public final class DesktopHelper {

    private DesktopHelper() {

    }

    public static void open(File file) {
        SwingUtilities.invokeLater(() -> {
            if(Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void browser(URI uri) {
        SwingUtilities.invokeLater(() -> {
            if(Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
