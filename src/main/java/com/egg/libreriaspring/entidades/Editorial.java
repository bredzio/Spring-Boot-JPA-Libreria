package com.egg.libreriaspring.entidades;

import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class Editorial {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private Boolean alta;
    
    @OneToMany(mappedBy="editorial")
    private List<Libro> libros;

    public Editorial() {
    }

    public Editorial(String id, String nombre, Boolean alta, List<Libro> libros) {
        this.id = id;
        this.nombre = nombre;
        this.alta = alta;
        this.libros = libros;
    }    
}
