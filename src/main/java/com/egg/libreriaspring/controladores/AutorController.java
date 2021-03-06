package com.egg.libreriaspring.controladores;

import com.egg.libreriaspring.entidades.Autor;
import com.egg.libreriaspring.servicios.AutorServicio;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/autores")
public class AutorController {
    
    @Autowired
    private AutorServicio servicio;
    
    @GetMapping
    public ModelAndView mostrarAutores(HttpServletRequest request) throws Exception{
        ModelAndView mav = new ModelAndView("autor");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        try{
            if(flashMap != null){
                mav.addObject("exito",flashMap.get("exito"));
                mav.addObject("error",flashMap.get("error"));
            }
            
            List<Autor> autores = servicio.buscarTodos();
            Integer cont=0;
        
            for (Autor a: autores) {
                if (a.getAlta()==false) {
                cont++;
                }   
            }
        
            mav.addObject("autores",autores);
            mav.addObject("cont",cont);
            
        }catch(Exception e){
            e.printStackTrace();
            mav.addObject("excepcion",e.getMessage());
        }
        
        return mav;
        
    }
    
    @GetMapping("/crear")
    public ModelAndView crearAutor(RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView mav = new ModelAndView("autor-formulario");
        
        try{
            mav.addObject("autor", new Autor());
            mav.addObject("title", "Crear Autor");
            mav.addObject("action", "guardar"); 
        }catch(Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            mav.setViewName("redirect:/autores");
        }

        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editarAutor(@PathVariable String id, RedirectAttributes redirectAttributes) throws Exception{
        ModelAndView mav = new ModelAndView("autor-formulario");
        try{
            mav.addObject("autor", servicio.buscarPorId(id));
            mav.addObject("title", "Editar Autor");
            mav.addObject("action", "modificar");
        }catch(Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            mav.setViewName("redirect:/editar/{id}");
        }
        
        return mav;
    }
    
    @GetMapping("/reactivar/{nombre}")
    public ModelAndView reactivarAutor(@PathVariable String nombre,HttpServletRequest request) throws Exception{
        ModelAndView mav = new ModelAndView("autor-alta");
        
        mav.addObject("title", "Reactivar Autor");        
        mav.addObject("autor", servicio.buscarPorNombre(nombre)); 
        mav.addObject("action", "guardar2"); 

        return mav;
    }
    
    @PostMapping("/guardar2")
    public RedirectView activarOn(@RequestParam String id, RedirectAttributes redirectAttributes) throws Exception{
        try{
            servicio.reactivar(id);
            redirectAttributes.addFlashAttribute("exito","AUTOR DADO DE ALTA NUEVAMENTE");
                     
        }catch(Exception e){
            return new RedirectView("/autores");
        }
        
        return new RedirectView("/autores");
    }
    
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre, RedirectAttributes redirectAttributes) throws Exception{
        try{
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new Exception("EL NOMBRE DEL AUTOR ES OBLIGATORIO");
            }
            servicio.crearAutor(nombre);
            redirectAttributes.addFlashAttribute("exito","AUTOR DADO DE ALTA");
           
        }catch(Exception e){
            if(e.getMessage().equals("autorEnLista")){
                redirectAttributes.addFlashAttribute("error","EL AUTOR YA SE ENCUENTRA EN LA LISTA");
                return new RedirectView("/autores");
            }else{
                 return new RedirectView("/autores/reactivar/"+nombre);
            }
            
        }
        
        return new RedirectView("/autores");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre, RedirectAttributes redirectAttributes) throws Exception{
        ModelAndView mav = new ModelAndView("autor-formulario");
        try{
            servicio.modificarAutor(id, nombre);
            redirectAttributes.addFlashAttribute("exito","AUTOR MODIFICADO CON EXITO");
        }catch(Exception e){
            if(e.getMessage().equals("autorEnLista")){
                redirectAttributes.addFlashAttribute("error","HAY UN AUTOR REGISTRADO CON ESE NOMBRE");
                return new RedirectView("/autores");
            }else{
                 return new RedirectView("/autores/reactivar/"+nombre);
            }
        }
        return new RedirectView("/autores");
    }
    
  
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) throws Exception{
        ModelAndView mav = new ModelAndView("autor-formulario");
        try{
            servicio.eliminar(id);
            redirectAttributes.addFlashAttribute("exito","AUTOR ELIMINADO CON EXITO");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            mav.setViewName("redirect:/autores");
        }

        return new RedirectView("/autores");
    }
    
    
}
