package com.github.dlism.backend.services.Impl;

import com.github.dlism.backend.services.DictionaryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Override
    public List<String> organizationType() {
        var dictionary = new ArrayList<String>();
        dictionary.add("Открытая");
        dictionary.add("Закрытая");
        return dictionary;
    }

    @Override
    public List<String> activityType() {
        return null;
    }
}
