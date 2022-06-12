package com.szczepix.example.dictionary;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DictionaryControllerTest.DictionaryControllerTestConfiguration.class)
@WebMvcTest(controllers = { DictionaryController.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DictionaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Captor
    ArgumentCaptor<Dictionary> dictionaryArgumentCaptor;

    @Test
    public void shouldFindAllWithNoElements() throws Exception {
        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        DictionaryListAssertion.assertThat(result)
                .isEmptyList();
    }

    @Test
    public void shouldFindAllWithOneElement() throws Exception {
        when(dictionaryRepository.findAll()).thenReturn(createResponse(1));

        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        DictionaryListAssertion.assertThat(result)
                .hasElements(1)
                .getElement(0)
                    .hasId(0)
                    .hasKey("key_0")
                    .hasValue("value_0");
    }

    @Test
    public void shouldFindByKeysWithNoElements() throws Exception {
        when(dictionaryRepository.findByKeyIn(any())).thenReturn(createResponse(0));

        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/by/keys/key_1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        DictionaryListAssertion.assertThat(result)
                .isEmptyList();
    }

    @Test
    public void shouldFindByKeysWithOneElement() throws Exception {
        when(dictionaryRepository.findByKeyIn(any())).thenReturn(createResponse(1));

        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/by/keys/key_0,key_1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        DictionaryListAssertion.assertThat(result)
                .hasElements(1)
                .getElement(0)
                .hasId(0)
                .hasKey("key_0")
                .hasValue("value_0"); }

    @Test
    public void shouldFindByKeysWithManyElements() throws Exception {
        when(dictionaryRepository.findByKeyIn(any())).thenReturn(createResponse(3));

        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/by/keys/key_0,key_1,key_2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("[{\"id\":0,\"key\":\"key_0\",\"value\":\"value_0\"},{\"id\":1,\"key\":\"key_1\",\"value\":\"value_1\"},{\"id\":2,\"key\":\"key_2\",\"value\":\"value_2\"}]");
    }

    @Test
    public void shouldFindByValuesWithNoElements() throws Exception {
        when(dictionaryRepository.findByValueIn(any())).thenReturn(createResponse(0));

        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/by/values/value_1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        DictionaryListAssertion.assertThat(result)
                .isEmptyList();
    }

    @Test
    public void shouldFindByValuesWithOneElement() throws Exception {
        when(dictionaryRepository.findByValueIn(any())).thenReturn(createResponse(1));

        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/by/values/value_0,value_1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        DictionaryListAssertion.assertThat(result)
                .hasElements(1)
                .getElement(0)
                .hasId(0)
                .hasKey("key_0")
                .hasValue("value_0");
    }

    @Test
    public void shouldFindByValuesWithManyElements() throws Exception {
        when(dictionaryRepository.findByValueIn(any())).thenReturn(createResponse(3));

        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/by/values/value_0,value_1,value_2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        DictionaryListAssertion.assertThat(result)
                .hasElements(3)
                .getElement(0)
                .hasId(0)
                .hasKey("key_0")
                .hasValue("value_0");

        DictionaryListAssertion.assertThat(result)
                .hasElements(3)
                .getElement(1)
                .hasId(1)
                .hasKey("key_1")
                .hasValue("value_1");

        DictionaryListAssertion.assertThat(result)
                .hasElements(3)
                .getElement(2)
                .hasId(2)
                .hasKey("key_2")
                .hasValue("value_2");
    }

    @Test
    public void shouldFindByIdWithNoElement() throws Exception {
        when(dictionaryRepository.findById(any())).thenReturn(Optional.empty());

        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/by/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("");
    }

    @Test
    public void shouldFindByIdWithElement() throws Exception {
        when(dictionaryRepository.findById(any())).thenReturn(Optional.of(DictionaryFactory.create(1L, "key", "value")));

        MvcResult result = this.mockMvc
                .perform(get("/crud/api/v1/find/by/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        DictionaryAssertion.assertThat(result)
                .hasId(1)
                .hasKey("key")
                .hasValue("value");
    }

    @Test
    public void shouldCreateWithElement() throws Exception {
        when(dictionaryRepository.save(any())).thenReturn(DictionaryFactory.create(1L, "key", "value"));

        MvcResult result = this.mockMvc
                .perform(
                        post("/crud/api/v1/create")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"id\":1,\"key\":\"key\",\"value\":\"value\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(dictionaryRepository, times(1)).save(dictionaryArgumentCaptor.capture());
        Dictionary dictionary = dictionaryArgumentCaptor.getValue();
        assertThat(dictionary).isNotNull();
        assertThat(dictionary.getId()).isEqualTo(1L);
        assertThat(dictionary.getKey()).isEqualTo("key");
        assertThat(dictionary.getValue()).isEqualTo("value");

        DictionaryAssertion.assertThat(result)
                .hasId(1)
                .hasKey("key")
                .hasValue("value");
    }

    @Test
    public void shouldUpdateWithNoElement() throws Exception {
        when(dictionaryRepository.findById(any())).thenReturn(Optional.empty());

        MvcResult result = this.mockMvc
                .perform(
                        put("/crud/api/v1/update/1")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"id\":1,\"key\":\"key\",\"value\":\"value\"}"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("");
    }

    @Test
    public void shouldUpdateWithElement() throws Exception {
        when(dictionaryRepository.findById(any())).thenReturn(Optional.of(DictionaryFactory.create(1L, "null", "null")));

        MvcResult result = this.mockMvc
                .perform(
                        put("/crud/api/v1/update/1")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"id\":1,\"key\":\"key\",\"value\":\"value\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(dictionaryRepository, times(1)).save(dictionaryArgumentCaptor.capture());
        Dictionary dictionary = dictionaryArgumentCaptor.getValue();
        assertThat(dictionary).isNotNull();
        assertThat(dictionary.getId()).isEqualTo(1L);
        assertThat(dictionary.getKey()).isEqualTo("key");
        assertThat(dictionary.getValue()).isEqualTo("value");

        DictionaryAssertion.assertThat(result)
                .hasId(1)
                .hasKey("key")
                .hasValue("value");
    }

    @Test
    public void shouldDeleteWithNoElement() throws Exception {
        when(dictionaryRepository.findById(any())).thenReturn(Optional.empty());

        MvcResult result = this.mockMvc
                .perform(
                        delete("/crud/api/v1/delete/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("");
    }

    @Test
    public void shouldDeleteWithElement() throws Exception {
        when(dictionaryRepository.findById(any())).thenReturn(Optional.of(DictionaryFactory.create(1L, "null", "null")));

        MvcResult result = this.mockMvc
                .perform(
                        delete("/crud/api/v1/delete/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(dictionaryRepository, times(1)).deleteById(anyLong());
        assertThat(result.getResponse().getContentAsString()).isEqualTo("");
    }

    private List<Dictionary> createResponse(final int count) {
        List<Dictionary> dictionaries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dictionaries.add(DictionaryFactory.create((long) i, "key_" + i, "value_" + i));
        }
        return dictionaries;
    }

    @Configuration
    static class DictionaryControllerTestConfiguration {

        @Bean
        public DictionaryRepository repository() {
            return mock(DictionaryRepository.class);
        }

        @Bean
        public DictionaryController dictionaryController() {
            return new DictionaryController(repository());
        }
    }
}
