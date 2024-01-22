package com.et.web.rest.vm;

import com.et.domain.enumeration.DiscountTypeUnit;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class DiscountTypeDeserializer extends JsonDeserializer<DiscountTypeUnit> {
    @Override
    public DiscountTypeUnit deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (StringUtils.isBlank(jsonParser.getValueAsString())) {
            return null;
        } else {
            DiscountTypeUnit discountTypeUnit = deserializationContext.readValue(jsonParser, DiscountTypeUnit.class);
            return discountTypeUnit;
        }
    }
}
