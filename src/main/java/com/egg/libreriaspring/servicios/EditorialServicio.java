package com.egg.libreriaspring.servicios;

import com.egg.libreriaspring.entidades.Editorial;
import com.egg.libreriaspring.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void crearEditorial(String nombre){
     
            Editorial editorial = new Editorial();

            editorial.setNombre(nombre.toUpperCase());
            editorial.setAlta(true);

            editorialRepositorio.save(editorial);
      
    }
    
    @Transactional
    public void modificarEditorial(String id,String nombre){
        Editorial editorial = editorialRepositorio.findById(id).get();
              
        editorial.setNombre(nombre);
        
        editorialRepositorio.save(editorial);
    }
    
    @Transactional(readOnly = true)
    public List<Editorial> buscarTodos(){
        return editorialRepositorio.findAll();
    }
    
    @Transactional(readOnly=true)
    public Editorial buscarPorId(String id){
        Optional<Editorial> editorialOptional = editorialRepositorio.findById(id);
        return editorialOptional.orElse(null);
    }
    
    @Transactional
    public void eliminar(String id){
        Editorial editorial = editorialRepositorio.findById(id).get();
        editorial.setAlta(false);
        editorialRepositorio.save(editorial);
    }
}
