package com.et.liquid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import liqp.Template;
import liqp.TemplateContext;
import liqp.filters.Filter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class LiquidUtils {

    public static final ObjectMapper OBJECT_MAPPER = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDefaultPropertyInclusion(
                JsonInclude.Value.construct(JsonInclude.Include.NON_NULL,
                        JsonInclude.Include.ALWAYS));
        return objectMapper;
    }

    @PostConstruct
    public void init() {
        Filter.registerFilter(new Filter("img_url") {
            @Override
            public Object apply(Object value, Object... params) {
                return value; //TODO needs implementation
            }
        });

        Filter.registerFilter(new Filter("dateToSeconds"){
            @Override
            public Object apply(Object value, TemplateContext context, Object... params) {
                String text = super.asString(value, context);
                try {
                    List<String> patternStrings = Arrays.asList("MMM d, yyyy", "yyyy-MM-dd", "MM/dd/yyyy", "M/dd/yyyy", "M/dd/yyyy", "M/d/yyyy", "M/d/yyyy", "M/dd/yy", "MM/dd/yy", "M/d/yy", "MM/d/yy", "yyyy-MM-dd'T'HH:mm:ssz", "yy-MM-dd'T'HH:mm:ss", "d/M/yyyy H:mm", "dd/M/yyyy H:mm", "dd/MM/yyyy H:mm", "M/dd/yyyy H:mm", "MM/dd/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd H:mm:ss");
                    for(String pattern : patternStrings) {
                        try {
                            Date date = new SimpleDateFormat(pattern).parse(text);
                            long seconds = date.getTime() / 1000;
                            return seconds;
                        }catch (Exception ex){
                        }
                    }
                }catch (Exception e){
                }
                return text;
            }
        });
    }

    public String getValue(Object model, String liquidContent) {
        Template template = Template.parse(liquidContent);
        return template.render(OBJECT_MAPPER.convertValue(model, Map.class));
    }
}
