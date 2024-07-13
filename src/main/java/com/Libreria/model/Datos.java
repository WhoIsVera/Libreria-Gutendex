package com.Libreria.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "datos")
public class Datos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int count;

    private String next;

    private String previous;
    @OneToMany(mappedBy = "datos", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libros> results;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Libros> getResults() {
        return results;
    }

    public void setResults(List<Libros> results) {
        this.results = results;
    }
}
