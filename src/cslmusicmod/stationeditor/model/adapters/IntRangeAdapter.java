package cslmusicmod.stationeditor.model.adapters;

import com.google.gson.*;
import cslmusicmod.stationeditor.model.IntRange;

import java.lang.reflect.Type;

public class IntRangeAdapter implements JsonDeserializer<IntRange>, JsonSerializer<IntRange> {
    @Override
    public IntRange deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new IntRange(jsonElement.getAsJsonArray().get(0).getAsInt(), jsonElement.getAsJsonArray().get(1).getAsInt());
    }

    @Override
    public JsonElement serialize(IntRange range, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonArray arr = new JsonArray(2);
        arr.add(range.getFrom());
        arr.add(range.getTo());

        return arr;
    }
}
