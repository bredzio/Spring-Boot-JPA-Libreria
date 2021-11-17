package com.egg.libreriaspring.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE usuario SET alta = false WHERE dni = ?")
public class Usuario {
    @Id
    private Long dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String clave;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creacion;

    @LastModifiedDate
    private LocalDateTime modificacion;
    
    private Boolean alta;

    public Usuario() {
    }

    public Usuario(Long dni, String nombre, String apellido, String correo, String clave, LocalDate fechaNacimiento, LocalDateTime creacion, LocalDateTime modificacion, Boolean alta) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.clave = clave;
        this.fechaNacimiento = fechaNacimiento;
        this.creacion = creacion;
        this.modificacion = modificacion;
        this.alta = alta;
    }
    
    
}
