package cslmusicmod.stationeditor.model.adapters;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import cslmusicmod.stationeditor.model.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationAdapter implements JsonSerializer<Station>, JsonDeserializer<Station> {

    @Override
    public Station deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Station station = new Station();
        JsonObject obj = jsonElement.getAsJsonObject();

        Type stringListType = new TypeToken<List<String>>(){}.getType();
        Type scheduleEntryListType = new TypeToken<List<ScheduleEntry>>(){}.getType();
        Type namedContextConditionsType = new TypeToken<Map<String, ContextCondition>>(){}.getType();
        Type contextListType = new TypeToken<List<ContextEntry>>(){}.getType();

        Map<String, ContextCondition> namedContextConditions = new HashMap<>();

        station.setName(obj.get("name").getAsString());
        if(obj.has("thumbnail"))
            station.setThumbnail(obj.get("thumbnail").getAsString());
        if(obj.has("collections"))
            station.setCollections(jsonDeserializationContext.deserialize(obj.get("collections"), stringListType));
        if(obj.has("schedule"))
            station.setSchedule(jsonDeserializationContext.deserialize(obj.get("schedule"), scheduleEntryListType));
        if(obj.has("filters"))
            namedContextConditions = jsonDeserializationContext.deserialize(obj.get("filters"), namedContextConditionsType);
        if(obj.has("contexts"))
            station.setContexts(jsonDeserializationContext.deserialize(obj.get("contexts"), contextListType));

        for(ContextEntry cont : station.getContexts()) {
            namedContextConditions.putAll(cont.getConditions().getInlinedContextConditions());
        }

        station.setFilters(namedContextConditions);

        // Set all necessary parent references
        station.getFilters().values().stream().forEach(x -> x.setStation(station));
        station.getSchedule().stream().forEach(x -> x.setStation(station));
        station.getContexts().stream().forEach(x -> {
            x.setStation(station);
            x.getConditions().setContext(x);
            x.getConditions().getDnf().stream().forEach(y -> y.setDnf(x.getConditions()));
        });

        return station;
    }

    @Override
    public JsonElement serialize(Station station, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject elem = new JsonObject();

        elem.add("name", new JsonPrimitive(station.getName()));
        if(station.getThumbnail() != null && !station.getThumbnail().isEmpty())
            elem.add("thumbnail", new JsonPrimitive(station.getThumbnail()));
        elem.add("collections", jsonSerializationContext.serialize(station.getCollections()));
        elem.add("schedule", jsonSerializationContext.serialize(station.getSchedule()));
        elem.add("filters", jsonSerializationContext.serialize(station.getFilters()));
        elem.add("contexts", jsonSerializationContext.serialize(station.getContexts()));

        return elem;
    }
}
