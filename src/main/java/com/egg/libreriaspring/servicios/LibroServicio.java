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
    public void crearLibro(String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Autor autor, Editorial editorial){
        Libro libro = new Libro();
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
    }
    
    @Transactional
    public void modificarLibro(String id,String isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Autor autor, Editorial editorial){
        Libro libro = libroRepositorio.findById(id).get();
        
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplares-ejemplaresPrestados);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        
        libroRepositorio.save(libro);
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
        Libro libro = libroRepositorio.findById(id).get();
        libro.setAlta(false);
        libroRepositorio.save(libro);
    }
    
    
}
