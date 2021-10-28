package com.egg.libreriaspring.entidades;

import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class Autor {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private Boolean alta;

    @OneToMany(mappedBy="autor")
    private List<Libro> libros;

    public Autor() {
    }

    public Autor(String id, String nombre, Boolean alta, List<Libro> libros) {
        this.id = id;
        this.nombre = nombre;
        this.alta = alta;
        this.libros = libros;
    }
}
    