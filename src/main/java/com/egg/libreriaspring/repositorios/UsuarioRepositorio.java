package com.egg.libreriaspring.repositorios;

import com.egg.libreriaspring.entidades.Usuario;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long>{
    boolean existsUsuarioByCorreo(String correo);
    
      // Alternativa de actualización de datos a través de consulta
    @Modifying
    @Query("UPDATE Usuario u SET u.nombre = :nombre, u.apellido = :apellido, u.fechaNacimiento = :fechaNacimiento, u.correo = :correo, u.clave = :clave WHERE u.dni = :dni")
    void modificar(@Param("dni") Long dni, @Param("nombre") String nombre, @Param("apellido") String apellido, @Param("fechaNacimiento") LocalDate fechaNacimiento, @Param("correo") String correo, @Param("clave") String clave);

    // Alternativa de actualización de datos a través de consulta
    @Modifying
    @Query("UPDATE Usuario u SET u.alta = true WHERE u.dni = :dni")
    void habilitar(@Param("dni") Long dni);

    // Creación de consulta a partir de JPQL
    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
    Usuario buscarPorCorreo(@Param("correo") String correo);

    // Creación de consulta nativa
    @Query(value = "SELECT * FROM usuario WHERE correo = ?1", nativeQuery = true)
    Usuario buscarPorCorreoSQL(String correo);

    // Creación de consulta a partir del nombre de método
    Usuario findByCorreo(String correo);
    
    // Creación de consulta a partir del nombre de método
    boolean existsByCorreo(String correo);
}
