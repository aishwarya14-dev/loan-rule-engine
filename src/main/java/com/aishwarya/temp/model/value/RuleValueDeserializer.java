package com.aishwarya.Finbank.model.value;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class RuleValueDeserializer extends StdDeserializer<RuleValue> {

    public RuleValueDeserializer() {
        super(RuleValue.class);
    }

    @Override
    public RuleValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        if (jsonNode.isInt() || jsonNode.isLong()) return new IntValue(jsonNode.intValue());
        if (jsonNode.isDouble() || jsonNode.isFloat()) return new DoubleValue(jsonNode.doubleValue());
        if (jsonNode.isTextual()) return new StringValue(jsonNode.textValue());

        throw new IllegalArgumentException(
                "Unsupported value type in JSON: " + jsonNode.getNodeType()
        );
    }

}
