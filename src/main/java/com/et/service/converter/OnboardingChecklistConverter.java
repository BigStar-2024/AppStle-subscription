package com.et.service.converter;

import com.et.domain.enumeration.OnboardingChecklistStep;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class OnboardingChecklistConverter implements AttributeConverter<List<OnboardingChecklistStep>, String> {
    @Override
    public String convertToDatabaseColumn(List<OnboardingChecklistStep> onboardingChecklistSteps) {
        if (onboardingChecklistSteps == null) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (OnboardingChecklistStep step : onboardingChecklistSteps) {
            sb.append("\"");
            sb.append(step.toString());
            sb.append("\"");
            if (onboardingChecklistSteps.indexOf(step) < onboardingChecklistSteps.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public List<OnboardingChecklistStep> convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return List.of();
        }

        ArrayList<OnboardingChecklistStep> list = new ArrayList<>();

        JsonParser springParser = JsonParserFactory.getJsonParser();
        List<String> values = new ArrayList<>();
        try {
            values = springParser.parseList(s).stream().map(Object::toString).collect(Collectors.toList());
        } catch (JsonParseException e) {
            System.err.println(e.getMessage());
        }
        for (String val : values) {
            try {
                list.add(OnboardingChecklistStep.valueOf(val));
            } catch(IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        return list;
    }
}
