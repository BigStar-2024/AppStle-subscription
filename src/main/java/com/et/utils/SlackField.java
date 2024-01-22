package com.et.utils;

import com.github.seratch.jslack.api.model.Field;
import com.github.seratch.jslack.api.model.Field.FieldBuilder;

public class SlackField {

    Field field;

    public SlackField(String title, String value) {
        FieldBuilder builder = Field.builder();
        builder.title(title);
        builder.value(value);
        builder.valueShortEnough(true);
        field = builder.build();
    }
}