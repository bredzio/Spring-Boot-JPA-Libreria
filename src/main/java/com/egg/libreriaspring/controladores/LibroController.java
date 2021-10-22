package com.egg.libreriaspring.controladores;

import com.egg.libreriaspring.entidades.Autor;
import com.egg.libreriaspring.entidades.Editorial;
import com.egg.libreriaspring.entidades.Libro;
import com.egg.libreriaspring.servicios.AutorServicio;
import com.egg.libreriaspring.servicios.EditorialServicio;
import com.egg.libreriaspring.servicios.LibroServicio;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
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
    public ModelAndView mostrarLibros(HttpServletRequest request) throws Exception{
        ModelAndView mav = new ModelAndView("libro");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        try{
           if(flashMap != null){
                mav.addObject("exito",flashMap.get("exito"));
                mav.addObject("error",flashMap.get("error"));
           }
           
           List<Libro> libros = libroServicio.buscarTodos();
           Integer cont=0;
        
            for (Libro l: libros) {
                if (l.getAlta()==false) {
                    cont++;
                }
            }
        
            mav.addObject("libros",libros);
            mav.addObject("cont",cont);
        }catch(Exception e){
           e.printStackTrace();
           mav.addObject("excepcion",e.getMessage()); 
        }
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crearLibro(RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView mav = new ModelAndView("libro-formulario");
        try{
        mav.addObject("libro", new Libro());
        mav.addObject("title", "Crear Libro");
        mav.addObject("autores", autorServicio.buscarTodos());
        mav.addObject("editoriales", editorialServicio.buscarTodos());
        mav.addObject("action", "guardar");
        }catch(Exception e){
          e.printStackTrace();
          redirectAttributes.addFlashAttribute("error",e.getMessage());
          mav.setViewName("redirect:/libros");   
        }
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editarLibro(@PathVariable String id,RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView mav = new ModelAndView("libro-formulario");
        try{
            mav.addObject("libro", libroServicio.buscarPorId(id));
            mav.addObject("autores", autorServicio.buscarTodos());
            mav.addObject("editoriales", editorialServicio.buscarTodos());
            mav.addObject("title", "Editar Libro");
            mav.addObject("action", "modificar");
        }catch(Exception e)    {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            mav.setViewName("redirect:/editar/{id}");
        }
        return mav;
    }
    
    @GetMapping("/reactivar/{titulo}")
    public ModelAndView reactivarLibro(@PathVariable String titulo,HttpServletRequest request) throws Exception{
        ModelAndView mav = new ModelAndView("libro-alta");
        
        mav.addObject("title", "Reactivar Libro");        
        mav.addObject("libro", libroServicio.buscarPorNombre(titulo)); 
        mav.addObject("action", "guardar2"); 

        return mav;
    }
    
    @PostMapping("/guardar2")
    public RedirectView activarOn(@RequestParam String id, RedirectAttributes redirectAttributes) throws Exception{
        try{
            libroServicio.reactivar(id);
            redirectAttributes.addFlashAttribute("exito","LIBRO DADO DE ALTA NUEVAMENTE");
                     
        }catch(Exception e){
            return new RedirectView("/libros");
        }
        
        return new RedirectView("/libros");
    }
    
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String titulo,@RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Autor autor, @RequestParam Editorial editorial,RedirectAttributes redirectAttributes) throws Exception {
        try{
            if (titulo == null || titulo.trim().isEmpty()) {
                throw new Exception("EL NOMBRE DEL LIBRO ES OBLIGATORIO");
            } 
            
            libroServicio.crearLibro(titulo, anio, ejemplares, ejemplaresPrestados,autor,editorial);
            redirectAttributes.addFlashAttribute("exito","LIBRO DADO DE ALTA");

        }catch(Exception e){
            if(e.getMessage().equals("libroEnLista")){
                redirectAttributes.addFlashAttribute("error","EL LIBRO YA SE ENCUENTRA EN LA LISTA");
                return new RedirectView("/libros");
            }else{
                 return new RedirectView("/libros/reactivar/"+titulo);
            }
        }
        return new RedirectView("/libros");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id,@RequestParam String isbn,@RequestParam String titulo,@RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Autor autor, @RequestParam Editorial editorial,RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView mav = new ModelAndView("libro-formulario");
        try{
            libroServicio.modificarLibro(id, isbn, titulo, anio , ejemplares, ejemplaresPrestados, autor, editorial);
            redirectAttributes.addFlashAttribute("exito","LIBRO MODIFICADO CON EXITO");
        }catch(Exception e){
            if(e.getMessage().equals("libroEnLista")){
                redirectAttributes.addFlashAttribute("error","HAY UN LIBRO REGISTRADO CON ESE TÍTULO");
                return new RedirectView("/libros");
            }else{
                 return new RedirectView("/libros/reactivar/"+titulo);
            }     
        }
        return new RedirectView("/libros");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id,RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView mav = new ModelAndView("libro-formulario");
        try{
            libroServicio.eliminar(id);
            redirectAttributes.addFlashAttribute("exito","LIBRO ELIMINADO CON EXITO");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            mav.setViewName("redirect:/libros");
        }
        return new RedirectView("/libros");
    }
    
}
