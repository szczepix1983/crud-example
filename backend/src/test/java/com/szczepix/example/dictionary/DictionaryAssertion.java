package com.szczepix.example.dictionary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

public class DictionaryAssertion {

    private static ObjectMapper mapper = new ObjectMapper();
    private static Dictionary dictionary;

    DictionaryAssertion(final Dictionary dictionary) {
        DictionaryAssertion.dictionary = dictionary;
    }

    public static DictionaryAssertion assertThat(final MvcResult result) {
        return new DictionaryAssertion(parse(result));
    }

    private static Dictionary parse(final MvcResult result) {
        try {
            return mapper.readValue(result.getResponse().getContentAsString(), Dictionary.class);
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DictionaryAssertion hasId(final int id) {
        Assertions.assertThat(dictionary.getId()).isEqualTo(id);
        return this;
    }

    public DictionaryAssertion hasKey(final String key) {
        Assertions.assertThat(dictionary.getKey()).isEqualTo(key);
        return this;
    }

    public DictionaryAssertion hasValue(final String value) {
        Assertions.assertThat(dictionary.getValue()).isEqualTo(value);
        return this;
    }
}
