package cslmusicmod.stationeditor.model.adapters;

import com.google.gson.*;
import cslmusicmod.stationeditor.model.ContextCondition;
import cslmusicmod.stationeditor.model.ContextConditionDNF;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextConditionDNFAdapter implements JsonDeserializer<ContextConditionDNF>, JsonSerializer<ContextConditionDNF> {
    @Override
    public ContextConditionDNF deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<List<String>> dnf = new ArrayList<>();
        Map<String, ContextCondition> inlinedContextConditions = new HashMap<>();

        for(JsonElement l1 : jsonElement.getAsJsonArray()) {

            List<String> conj = new ArrayList<>();
            dnf.add(conj);

            for(JsonElement item : l1.getAsJsonArray()) {
                if(item.isJsonPrimitive()) {
                    conj.add(item.getAsString());
                }
                else {
                    ContextCondition inlined = jsonDeserializationContext.deserialize(item, ContextCondition.class);
                    String name = ContextCondition.generateUniqueName(inlined);
                    inlinedContextConditions.put(name, inlined);
                    conj.add(name);
                }
            }
        }

        ContextConditionDNF o = new ContextConditionDNF();
        o.setDnf(dnf);
        o.setInlinedContextConditions(inlinedContextConditions);

        return o;
    }

    @Override
    public JsonElement serialize(ContextConditionDNF contextConditionDNF, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(contextConditionDNF.getDnf());
    }
}
