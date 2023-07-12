package com.github.dlism.backend.services.Impl;

import com.github.dlism.backend.services.DictionaryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Override
    public List<String> organizationType() {
        var organizationType = new ArrayList<String>();
        organizationType.add("Открытая");
        organizationType.add("Закрытая");
        return organizationType;
    }

    @Override
    public List<String> activityType() {
        var activityTypes = new ArrayList<String>();
        activityTypes.add("Маркетинг");
        activityTypes.add("Реклама");
        activityTypes.add("Оброзование");
        activityTypes.add("IT");
        return activityTypes;
    }
}
