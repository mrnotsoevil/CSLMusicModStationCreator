package cslmusicmod.stationeditor.model.adapters;

import com.google.gson.*;
import cslmusicmod.stationeditor.model.SongCollection;

import java.lang.reflect.Type;

public class SongCollectionAdapter implements JsonDeserializer<SongCollection>, JsonSerializer<SongCollection> {
    @Override
    public SongCollection deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        SongCollection coll = new SongCollection();
        coll.setName(jsonElement.getAsString());
        return coll;
    }

    @Override
    public JsonElement serialize(SongCollection songCollection, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(songCollection.getName());
    }
}
