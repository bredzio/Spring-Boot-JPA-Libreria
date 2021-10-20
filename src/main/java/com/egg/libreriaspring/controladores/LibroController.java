package com.egg.libreriaspring.controladores;

import com.egg.libreriaspring.entidades.Autor;
import com.egg.libreriaspring.entidades.Editorial;
import com.egg.libreriaspring.entidades.Libro;
import com.egg.libreriaspring.servicios.AutorServicio;
import com.egg.libreriaspring.servicios.EditorialServicio;
import com.egg.libreriaspring.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroServicio libroServicio;
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @Autowired
    private AutorServicio autorServicio;
    
    @GetMapping
    public ModelAndView mostrarLibros(){
        ModelAndView mav = new ModelAndView("libro");
        List<Libro> libros = libroServicio.buscarTodos();
        Integer cont=0;
        
        for (Libro l: libros) {
            if (l.getAlta()==false) {
                cont++;
            }
        }
        
        mav.addObject("libros",libros);
        mav.addObject("cont",cont);
        
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crearLibro() {
        ModelAndView mav = new ModelAndView("libro-formulario");
        mav.addObject("libro", new Libro());
        mav.addObject("title", "Crear Libro");
        mav.addObject("autores", autorServicio.buscarTodos());
        mav.addObject("editoriales", editorialServicio.buscarTodos());
        mav.addObject("action", "guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editarLibro(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("libro-formulario");
        mav.addObject("libro", libroServicio.buscarPorId(id));
        mav.addObject("autores", autorServicio.buscarTodos());
        mav.addObject("editoriales", editorialServicio.buscarTodos());
        mav.addObject("title", "Editar Libro");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String titulo,@RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Autor autor, @RequestParam Editorial editorial) {
        libroServicio.crearLibro(titulo, anio, ejemplares, ejemplaresPrestados,autor,editorial);
        return new RedirectView("/libros");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id,@RequestParam String isbn,@RequestParam String titulo,@RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Autor autor, @RequestParam Editorial editorial) {
        libroServicio.modificarLibro(id, isbn, titulo, anio , ejemplares, ejemplaresPrestados, autor, editorial);
        return new RedirectView("/libros");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id) {
        libroServicio.eliminar(id);
        return new RedirectView("/libros");
    }
    
}