package cslmusicmod.stationeditor.helpers;

import javafx.concurrent.Task;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CopyToDirectoryTask extends Task<Void> {

    private List<File> source;

    private Path target;

    public CopyToDirectoryTask(List<File> source, Path target) {
        this.target = target;
        this.source = source;
    }

    @Override
    protected Void call() throws Exception {

        for(int i = 0; i < source.size(); ++i) {
            File file = source.get(i);
            Path dst = target.resolve(file.getName());
            updateProgress(i, source.size());
            updateMessage(file.getAbsolutePath());
            Files.copy(file.toPath(), dst, StandardCopyOption.REPLACE_EXISTING);
        }

        return null;
    }
}
