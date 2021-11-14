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
    public void crearEditorial(String nombre)throws Exception{
     nombre=nombre.toUpperCase();
        try{
            Editorial editorial = new Editorial();
            if(nombre==null || nombre.trim().isEmpty()){
                throw new Exception("EL NOMBRE DE LA EDITORIAL ES OBLIGATORIO");
            }
            if(editorialRepositorio.buscarEditorialesPorNombre(nombre).isEmpty()==false){
                if(editorialRepositorio.buscarEditorialesPorNombre(nombre).get(0).getAlta()==false){
                    throw new Exception ("editorialRegistrado");
                }
            }
            
            if(editorialRepositorio.buscarEditorialesPorNombre(nombre).isEmpty()==false){
                    throw new Exception ("editorialEnLista");
            }
            
            editorial.setNombre(nombre);
            editorial.setAlta(true);
            editorialRepositorio.save(editorial);
        }catch(Exception e){
            throw e;
        }    
    }
    
    @Transactional
    public void modificarEditorial(String id,String nombre)throws Exception{
        nombre=nombre.toUpperCase();
        try{    
           if(editorialRepositorio.buscarEditorialesPorNombre(nombre).isEmpty()==false){
                if(editorialRepositorio.buscarEditorialesPorNombre(nombre).get(0).getAlta()==true){
                    throw new Exception("editorialEnLista");
                }
            }
            
            if(editorialRepositorio.buscarEditorialesPorNombre(nombre).isEmpty()==false){
                if(editorialRepositorio.buscarEditorialesPorNombre(nombre).get(0).getAlta()==false){
                    throw new Exception ("editorialRegistrado");
                }
            }
            
            
            
            Editorial editorial = editorialRepositorio.findById(id).get();

            editorial.setNombre(nombre);

            editorialRepositorio.save(editorial);
            
            }catch(Exception e){
            throw e;
        }
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
        try{
        editorialRepositorio.deleteById(id);
        }catch(Exception e){
            throw e;
        }
    }
    
    @Transactional
    public void reactivar(String id){
        try{
            Editorial editorial = editorialRepositorio.findById(id).get();
            editorial.setAlta(true);
            editorialRepositorio.save(editorial);
        }catch(Exception e){
            throw e;
        }
    }
    
    @Transactional(readOnly=true)
    public Editorial buscarPorNombre(String nombre){
        Editorial editorialOptional = editorialRepositorio.buscarEditorialesPorNombre(nombre).get(0);
        return editorialOptional;
    }
}
