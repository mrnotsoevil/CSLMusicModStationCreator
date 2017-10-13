package cslmusicmod.stationeditor.model.adapters;

import com.google.gson.*;
import cslmusicmod.stationeditor.model.Conjunction;

import java.lang.reflect.Type;

public class ConjunctionAdapter implements JsonSerializer<Conjunction>, JsonDeserializer<Conjunction> {
    @Override
    public Conjunction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Conjunction conj = new Conjunction();
        for(JsonElement e : jsonElement.getAsJsonArray()) {
            conj.getLiterals().add(e.getAsString());
        }
        return conj;
    }

    @Override
    public JsonElement serialize(Conjunction conjunction, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray arr = new JsonArray();
        conjunction.getLiterals().stream().forEach(x -> arr.add(x));
        return arr;
    }
}
