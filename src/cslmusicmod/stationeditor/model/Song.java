package cslmusicmod.stationeditor.model;


import com.google.common.io.Files;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Song {

    String file;

    public Song(String file) {
        this.file = file;
    }

    public String getSongType() {

        String baseName = Files.getNameWithoutExtension(file);

        if(baseName.endsWith("#blurb")) {
            return "blurb";
        }
        else if(baseName.endsWith("#commercial")) {
            return "commercial";
        }
        else if(baseName.endsWith("#talk")) {
            return "talk";
        }

        return "music";
    }

    public String getName() {
        String baseName = Files.getNameWithoutExtension(file);
        String type = getSongType();

        if(type.equals("music")) {
            return baseName;
        }
        else {
            return baseName.substring(0, baseName.length() - type.length() - 1);
        }
    }

    public void setSongType(String type) {
        if(!ScheduleEntry.ALLOWED_TYPES.contains(type))
            throw new IllegalArgumentException("Unknown song type " + type);
        if(type.equals(getSongType()))
            return;

        String oldname = file;
        String newname = Paths.get(file).getParent().resolve(getName()).toString();

        if(!type.equals("music")) {
            newname += "#" + type;
        }

        newname += ".ogg";

        if(oldname.equals(newname))
            return;

        try {
            Files.copy(new File(oldname), new File(newname));
            java.nio.file.Files.deleteIfExists(Paths.get(oldname));
            file = newname;
        } catch (IOException e) {
            DialogHelper.showExceptionError("Rename song", "Error while renaming the file!", e);
        }
    }

    public void delete() {
        try {
            java.nio.file.Files.deleteIfExists(Paths.get(file));
        } catch (IOException e) {
            DialogHelper.showExceptionError("Delete song", "Error while deleting the file!", e);
        }
    }

}
