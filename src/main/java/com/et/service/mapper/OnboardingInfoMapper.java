package com.et.service.mapper;


import com.et.domain.*;
import com.et.domain.enumeration.OnboardingChecklistStep;
import com.et.service.dto.OnboardingInfoDTO;

import org.mapstruct.*;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link OnboardingInfo} and its DTO {@link OnboardingInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OnboardingInfoMapper extends EntityMapper<OnboardingInfoDTO, OnboardingInfo> {

    default OnboardingInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        OnboardingInfo onboardingInfo = new OnboardingInfo();
        onboardingInfo.setId(id);
        return onboardingInfo;
    }
}
