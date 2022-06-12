package com.szczepix.example.dictionary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

public class DictionaryListAssertion {

    private static ObjectMapper mapper = new ObjectMapper();
    private static MvcResult result;

    DictionaryListAssertion(MvcResult result) {
        DictionaryListAssertion.result = result;
    }

    public static DictionaryListAssertion assertThat(final MvcResult result) {
        return new DictionaryListAssertion(result);
    }

    private static Dictionary[] parseAsList() {
        try {
            return mapper.readValue(result.getResponse().getContentAsString(), Dictionary[].class);
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DictionaryListAssertion isEmptyList() {
        Assertions.assertThat(parseAsList()).isEmpty();
        return this;
    }

    public DictionaryListAssertion hasElements(int count) {
        Assertions.assertThat(parseAsList().length).isEqualTo(count);
        return this;
    }

    public DictionaryAssertion getElement(int index) {
        return new DictionaryAssertion(parseAsList()[index]);
    }
}
