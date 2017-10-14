package cslmusicmod.stationeditor.helpers;

public final class FileHelper {
    private FileHelper() {

    }

    public static String sanatizeFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
