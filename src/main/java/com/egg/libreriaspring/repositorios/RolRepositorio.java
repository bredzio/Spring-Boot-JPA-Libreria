
package com.egg.libreriaspring.repositorios;

import com.egg.libreriaspring.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, Integer>{
}
