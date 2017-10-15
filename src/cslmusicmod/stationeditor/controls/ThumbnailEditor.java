package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.model.Station;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ThumbnailEditor extends HBox {

    private Station station;

    private Image defaultImage;

    @FXML
    private ImageView thumbnailImage;

    @FXML
    private FilePicker thumbnailFile;

    public ThumbnailEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        defaultImage = new Image("/cslmusicmod/stationeditor/defaultThumbnail.png");

        thumbnailFile.textProperty().addListener(change -> {

            if(station != null) {
                station.setThumbnail(thumbnailFile.textProperty().getValue());
                updateImage();
            }
            else if(!thumbnailFile.textProperty().getValue().isEmpty()) {
                thumbnailFile.textProperty().setValue("");
            }

        });
    }

    private void setDefaultImage() {
        thumbnailImage.setImage(defaultImage);
    }

    private void updateImage() {

        String thumbnail = station.getThumbnail();

        if(thumbnail.isEmpty()) {
            setDefaultImage();
            return;
        }

        String path;

        if(Paths.get(thumbnail).isAbsolute()) {
            path = thumbnail;
        }
        else if(station.hasSaveLocation()) {
            path = Paths.get(station.getDirectory(), thumbnail).toString();
        }
        else {
            setDefaultImage();
            return;
        }

        if(path.isEmpty() || !Files.exists(Paths.get(path)) || Files.isDirectory(Paths.get(path))) {
            setDefaultImage();
            return;
        }

        try{
            thumbnailImage.setImage(new Image("file:" + path));
        }
        catch(Exception ex) {
            setDefaultImage();
        }
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
        this.thumbnailFile.textProperty().setValue(station.getThumbnail());
        updateImage();
    }
}
