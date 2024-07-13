package com.Libreria.repository;

import com.Libreria.model.Author;
import com.Libreria.model.DatosAutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);
    @Query("SELECT a FROM Author a WHERE a.birthYear >= :anoMinimo")
    List<Author> findAutoresByAnoMinimo(int anoMinimo);
}
