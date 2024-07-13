package com.Libreria.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer fechaNacimineto,
        @JsonAlias("death_year") Integer fechaFallecimiento
        ) {
}
