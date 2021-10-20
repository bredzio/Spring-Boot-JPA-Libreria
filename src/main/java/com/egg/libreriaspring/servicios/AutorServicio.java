package com.egg.libreriaspring.servicios;

import com.egg.libreriaspring.entidades.Autor;
import com.egg.libreriaspring.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {
    
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Transactional
    public void crearAutor(String nombre) throws Exception{        
        nombre=nombre.toUpperCase();
        try{
            Autor autor = new Autor();
            if(nombre==null || nombre.trim().isEmpty()){
                throw new Exception("EL NOMBRE DEL AUTOR ES OBLIGATORIO");
            }
            
            if(autorRepositorio.buscarAutoresPorNombre(nombre).isEmpty()==false){
                throw new Exception ("autorRegistrado");
            }
            
            autor.setNombre(nombre);
            autor.setAlta(true);
            autorRepositorio.save(autor);
            
        }catch(Exception e){
            throw e;
        }
    }
    
    
    @Transactional
    public void modificarAutor(String id,String nombre) throws Exception{
        nombre=nombre.toUpperCase();
        try{
            
            if(autorRepositorio.buscarAutoresPorNombre(nombre).isEmpty()==false){
                throw new Exception("El autor ya se encontraba registrado");
            }
            
            Autor autor = autorRepositorio.findById(id).get();
            autor.setNombre(nombre);
            autorRepositorio.save(autor);
            
        }catch(Exception e){
            throw e;
        }
        
    }
    
    @Transactional(readOnly = true)
    public List<Autor> buscarTodos(){
        return autorRepositorio.findAll();
    }
    
    @Transactional(readOnly=true)
    public Autor buscarPorId(String id){
        Optional<Autor> autorOptional = autorRepositorio.findById(id);
        return autorOptional.orElse(null);
    }
    
    @Transactional
    public void eliminar(String id){
        try{
            Autor autor = autorRepositorio.findById(id).get();
            autor.setAlta(false);
            autorRepositorio.save(autor);
        }catch(Exception e){
            throw e;
        }
    }
    
    @Transactional
    public void reactivar(String id){
        try{
            Autor autor = autorRepositorio.findById(id).get();
            autor.setAlta(true);
            autorRepositorio.save(autor);
        }catch(Exception e){
            throw e;
        }
    }
    
    @Transactional(readOnly=true)
    public Autor buscarPorNombre(String nombre){
        Autor autorOptional = autorRepositorio.buscarAutoresPorNombre(nombre).get(0);
        return autorOptional;
    }
    
}
