package com.szczepix.example.dictionary;

public class DictionaryFactory {
    public static Dictionary create(final Long id, final String key, final String value) {
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        dictionary.setKey(key);
        dictionary.setValue(value);
        return dictionary;
    }
}
