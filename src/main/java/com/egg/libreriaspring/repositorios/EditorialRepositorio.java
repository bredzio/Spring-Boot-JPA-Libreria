package com.egg.libreriaspring.repositorios;

import com.egg.libreriaspring.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String>{
     @Query("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    List<Editorial> buscarEditorialesPorNombre(@Param("nombre") String nombre);
}
