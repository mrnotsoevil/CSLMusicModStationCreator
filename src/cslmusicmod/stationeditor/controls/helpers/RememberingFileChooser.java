package cslmusicmod.stationeditor.controls.helpers;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.management.relation.RoleList;
import java.io.File;
import java.util.List;

public class RememberingFileChooser {

    private FileChooser wrapped;

    public RememberingFileChooser() {
        wrapped = new FileChooser();
    }

    public ObservableList<FileChooser.ExtensionFilter> getExtensionFilters() {
        return wrapped.getExtensionFilters();
    }

    public File showOpenDialog(Window window) {

        File opened = wrapped.showOpenDialog(window);
        if(opened != null) {
            wrapped.setInitialDirectory(opened.getParentFile());
        }
        return opened;
    }

    public List<File> showOpenMultipleDialog(Window window) {
        List<File> opened = wrapped.showOpenMultipleDialog(window);

        if(opened != null && !opened.isEmpty()) {
            wrapped.setInitialDirectory(opened.get(0).getParentFile());
        }

        return opened;
    }

    public void setInitialFileName(String s) {
        wrapped.setInitialFileName(s);
    }

    public File showSaveDialog(Window window) {

        File saved = wrapped.showSaveDialog(window);
        if(saved != null) {
            wrapped.setInitialDirectory(saved.getParentFile());
        }
        return saved;
    }
}
