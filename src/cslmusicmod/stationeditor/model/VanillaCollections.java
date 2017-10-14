package cslmusicmod.stationeditor.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class VanillaCollections {

    private static VanillaCollections instance;

    public static VanillaCollections getInstance() {
        if(instance == null) {
            instance = new VanillaCollections();
        }
        return instance;
    }

    private List<SongCollection> collections;

    private VanillaCollections() {
        Gson gson = new Gson();
        Type songCollectionListType = new TypeToken<List<SongCollection>>(){}.getType();

        try(InputStreamReader r = new InputStreamReader(getClass().getResourceAsStream("/cslmusicmod/stationeditor/VanillaCollections.json"))) {
            collections = gson.fromJson(r, songCollectionListType);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        System.out.println("Loaded vanilla collections.");
    }

    public List<SongCollection> getCollections() {
        return collections;
    }
}
