
package com.egg.libreriaspring.servicios;

import com.egg.libreriaspring.entidades.Rol;
import com.egg.libreriaspring.repositorios.RolRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolServicio {
   
   @Autowired
   private RolRepositorio rolRepositorio;
   
   @Transactional
   public void crear(String nombre){
       Rol rol = new Rol();
       rol.setNombre(nombre);
       rolRepositorio.save(rol);
   }
   
   @Transactional
   public List<Rol> buscarTodos(){
       return rolRepositorio.findAll();
   }
   
   
   
}
