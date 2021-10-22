
package com.egg.libreriaspring.repositorios;

import com.egg.libreriaspring.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String>{
    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    List<Libro> buscarLibrosPorNombre(@Param("titulo") String titulo);
}
