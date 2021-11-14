package com.egg.libreriaspring.servicios;

import com.egg.libreriaspring.entidades.Autor;
import com.egg.libreriaspring.entidades.Editorial;
import com.egg.libreriaspring.entidades.Libro;
import com.egg.libreriaspring.repositorios.LibroRepositorio;
import com.egg.libreriaspring.utilidad.Utilidad;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {
    
    private Utilidad utilidad1;
    
    @Autowired
    private LibroRepositorio libroRepositorio;
    
    @Transactional
    public void crearLibro(String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Autor autor, Editorial editorial)throws Exception{
        titulo=titulo.toUpperCase();
        try{
            Libro libro = new Libro();
            if(titulo==null || titulo.trim().isEmpty()){
                throw new Exception("EL TITULO DEL LIBRO ES OBLIGATORIO");
            }
            
            if(libroRepositorio.buscarLibrosPorNombre(titulo).isEmpty()==false){
                if(libroRepositorio.buscarLibrosPorNombre(titulo).get(0).getAlta()==false){
                    throw new Exception ("libroRegistrado");
                }
            }
            
            if(libroRepositorio.buscarLibrosPorNombre(titulo).isEmpty()==false){
                    throw new Exception ("libroEnLista");
            }
            
            if(ejemplares<ejemplaresPrestados){
                throw new Exception("libroPrestadosMayor");
            }
            
            String isbn= utilidad1.generarIsbn();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(ejemplares-ejemplaresPrestados);
            libro.setAlta(true);
            libro.setAutor(autor);
            libro.setEditorial(editorial);

            libroRepositorio.save(libro);
        }catch(Exception e){
            throw e;
        }
    }
    
    @Transactional
    public void modificarLibro(String id,String isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados)throws Exception{
        titulo=titulo.toUpperCase();
        try{
            if(libroRepositorio.buscarLibrosPorNombre(titulo).isEmpty()==false){
                if(libroRepositorio.buscarLibrosPorNombre(titulo).get(0).getAlta()==false){
                    throw new Exception ("libroRegistrado");
                }
            }
            
            if(libroRepositorio.buscarLibrosPorNombre(titulo).isEmpty()==false){
                    throw new Exception ("libroEnLista");
            }
            
            if(ejemplares<ejemplaresPrestados){
                throw new Exception("libroPrestadosMayor");
            }
            
            
            Libro libro = libroRepositorio.findById(id).get();

            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(ejemplares-ejemplaresPrestados);

            libroRepositorio.save(libro);
        }catch(Exception e){
            throw e;
        }
    }
    
    @Transactional (readOnly=true)
    public List<Libro> buscarTodos(){
        return libroRepositorio.findAll();
    }
    
    @Transactional (readOnly=true)
    public Libro buscarPorId(String id){
        Optional<Libro> libroOptional = libroRepositorio.findById(id);
        return libroOptional.orElse(null);
    }
    
    @Transactional
    public void eliminar(String id){
        try{
            libroRepositorio.deleteById(id);
        }catch(Exception e){
            throw e;
        }
    }
    
    @Transactional
    public void reactivar(String id){
        try{
            Libro libro = libroRepositorio.findById(id).get();
            libro.setAlta(true);
            libroRepositorio.save(libro);
        }catch(Exception e){
            throw e;
        }
    }
    
    @Transactional(readOnly=true)
    public Libro buscarPorNombre(String titulo){
        Libro libroOptional = libroRepositorio.buscarLibrosPorNombre(titulo).get(0);
        return libroOptional;
    }
    
    
}
