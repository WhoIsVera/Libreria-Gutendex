package com.Libreria.repository;


import com.Libreria.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibreriaRepository extends JpaRepository<Libros, Long> {
    @Query("SELECT l FROM Libros l WHERE LOWER(l.title) = LOWER(:title)")
    Optional<Libros> findByTitleExact(@Param("title") String title);

    List<Libros> findByLanguagesContaining(String idiomaBuscado);
}
