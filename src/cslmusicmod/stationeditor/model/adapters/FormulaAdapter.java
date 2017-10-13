package cslmusicmod.stationeditor.model.adapters;

import com.google.gson.*;
import cslmusicmod.stationeditor.model.Conjunction;
import cslmusicmod.stationeditor.model.ContextCondition;
import cslmusicmod.stationeditor.model.Formula;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormulaAdapter implements JsonDeserializer<Formula>, JsonSerializer<Formula> {
    @Override
    public Formula deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<Conjunction> dnf = new ArrayList<>();
        Map<String, ContextCondition> inlinedContextConditions = new HashMap<>();

        for(JsonElement l1 : jsonElement.getAsJsonArray()) {

            Conjunction conj = new Conjunction();
            dnf.add(conj);

            for(JsonElement item : l1.getAsJsonArray()) {
                if(item.isJsonPrimitive()) {
                    conj.getLiterals().add(item.getAsString());
                }
                else {
                    ContextCondition inlined = jsonDeserializationContext.deserialize(item, ContextCondition.class);
                    String name = ContextCondition.generateUniqueName(inlined);
                    inlinedContextConditions.put(name, inlined);
                    conj.getLiterals().add(name);
                }
            }
        }

        Formula o = new Formula();
        o.setDnf(dnf);
        o.setInlinedContextConditions(inlinedContextConditions);

        return o;
    }

    @Override
    public JsonElement serialize(Formula formula, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(formula.getDnf());
    }
}
