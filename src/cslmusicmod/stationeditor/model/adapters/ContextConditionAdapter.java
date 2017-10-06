package cslmusicmod.stationeditor.model.adapters;

import com.google.gson.*;
import cslmusicmod.stationeditor.model.*;

import java.lang.reflect.Type;

public class ContextConditionAdapter implements JsonSerializer<ContextCondition>, JsonDeserializer<ContextCondition> {


    @Override
    public ContextCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String contextType = jsonElement.getAsJsonObject().get("type").getAsString();

        if(contextType.equals("time")) {
            return jsonDeserializationContext.deserialize(jsonElement, TimeContextCondition.class);
        }
        else if(contextType.equals("disaster")) {
            return jsonDeserializationContext.deserialize(jsonElement, DisasterContextCondition.class);
        }
        else if(contextType.equals("mood")) {
            return jsonDeserializationContext.deserialize(jsonElement, MoodContextCondition.class);
        }
        else if(contextType.equals("weather")) {
            return jsonDeserializationContext.deserialize(jsonElement, WeatherContextCondition.class);
        }
        else {
            throw new JsonParseException("Unknown type " + contextType);
        }
    }

    @Override
    public JsonElement serialize(ContextCondition contextCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement elem = jsonSerializationContext.serialize(contextCondition, contextCondition.getClass());
        elem.getAsJsonObject().add("type", new JsonPrimitive(contextCondition.getType()));
        return elem;
    }
}
