package com.egg.libreriaspring.entidades;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE editorial SET alta = false WHERE id = ?")
public class Editorial {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private Boolean alta;
    
    @CreatedDate
    @Column(nullable=false, updatable=false)
    private LocalDateTime creacion;
    
    @LastModifiedDate
    private LocalDateTime modificacion;
    
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
