package com.szczepix.example.dictionary;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/crud/api/v1")
public class DictionaryController {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryController(final DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dictionary> getDictionaries() {
        return dictionaryRepository.findAll();
    }

    @GetMapping(value = "/find/by/keys/{keys}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dictionary> getDictionariesByKeys(@PathVariable String keys) {
        List<String> keysToFind = Arrays.asList(keys.split(","));
        return dictionaryRepository.findByKeyIn(new HashSet<>(keysToFind));
    }

    @GetMapping(value = "/find/by/values/{values}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dictionary> getDictionariesByValues(@PathVariable String values) {
        List<String> keysToFind = Arrays.asList(values.split(","));
        return dictionaryRepository.findByValueIn(new HashSet<>(keysToFind));
    }

    @GetMapping(value = "/find/by/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dictionary> getDictionary(@PathVariable Long id) {
        Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(id);
        return dictionaryOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dictionary> createDictionary(@RequestBody Dictionary dictionary) {
        Dictionary savedDictionary = dictionaryRepository.save(dictionary);
        return ResponseEntity.ok(savedDictionary);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dictionary> updateDictionary(@PathVariable Long id, @RequestBody Dictionary dictionary) {
        Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(id);
        if (dictionaryOptional.isPresent()) {
            Dictionary dictionaryToUpdate = dictionaryOptional.get();
            dictionaryToUpdate.setKey(dictionary.getKey());
            dictionaryToUpdate.setValue(dictionary.getValue());
            dictionaryRepository.save(dictionaryToUpdate);
            return ResponseEntity.ok(dictionaryToUpdate);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dictionary> deleteDictionary(@PathVariable Long id) {
        if (dictionaryRepository.findById(id).isPresent()) {
            dictionaryRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
