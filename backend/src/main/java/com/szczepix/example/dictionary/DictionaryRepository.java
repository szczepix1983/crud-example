package com.szczepix.example.dictionary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    List<Dictionary> findByKey(String key);
    List<Dictionary> findByKeyIn(Collection<String> keys);
    List<Dictionary> findByValue(String value);
    List<Dictionary> findByValueIn(Collection<String> keys);
}
