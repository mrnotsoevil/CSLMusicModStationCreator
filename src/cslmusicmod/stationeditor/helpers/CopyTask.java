package cslmusicmod.stationeditor.helpers;

import javafx.concurrent.Task;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CopyTask extends Task<Void> {

    private List<Entry> entries;

    public CopyTask(List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    protected Void call() throws Exception {

        for(int i = 0; i < entries.size(); ++i) {
            Path src = entries.get(i).getSource().toPath();
            Path dst = entries.get(i).getTarget().toPath();

            if(!Files.exists(dst.getParent())) {
                Files.createDirectories(dst.getParent());
            }

            updateProgress(i, entries.size());
            updateMessage(src.toString());
            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
        }

        return null;
    }

    public static class Entry {
        private File source;
        private File target;

        public Entry(File source, File target) {
            this.source = source;
            this.target = target;
        }

        public File getSource() {
            return source;
        }

        public File getTarget() {
            return target;
        }
    }

}
