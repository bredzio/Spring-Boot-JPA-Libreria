package com.egg.libreriaspring.controladores;

import com.egg.libreriaspring.entidades.Editorial;
import com.egg.libreriaspring.servicios.EditorialServicio;
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
@RequestMapping("/editoriales")
public class EditorialController {
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping
    public ModelAndView mostrarEditoriales(HttpServletRequest request) throws Exception{
        ModelAndView mav = new ModelAndView("editorial");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        try{
            if(flashMap != null){
                mav.addObject("exito",flashMap.get("exito"));
                mav.addObject("error",flashMap.get("error"));
            }
            List<Editorial> editoriales = editorialServicio.buscarTodos();
            Integer cont=0;

            for (Editorial e: editoriales) {
                if (e.getAlta()==false) {
                    cont++;
                }
            }
            
             mav.addObject("editoriales",editoriales);
            mav.addObject("cont",cont);
        
        }catch(Exception e){
            e.printStackTrace();
            mav.addObject("excepcion",e.getMessage());
        }
        
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crearEditorial(RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView mav = new ModelAndView("editorial-formulario");
        try{
        mav.addObject("editorial", new Editorial());
        mav.addObject("title", "Crear Editorial");
        mav.addObject("action", "guardar");
        }catch(Exception e){
          e.printStackTrace();
          redirectAttributes.addFlashAttribute("error",e.getMessage());
          mav.setViewName("redirect:/editoriales");  
        }
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editarEditorial(@PathVariable String id, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView mav = new ModelAndView("editorial-formulario");
        try{
        mav.addObject("editorial", editorialServicio.buscarPorId(id));
        mav.addObject("title", "Editar Editorial");
        mav.addObject("action", "modificar");
        }catch(Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            mav.setViewName("redirect:/editar/{id}");
        }
        return mav;
    }
    
    @GetMapping("/reactivar/{nombre}")
    public ModelAndView reactivarEditorial(@PathVariable String nombre,HttpServletRequest request) throws Exception{
        ModelAndView mav = new ModelAndView("editorial-alta");
        
        mav.addObject("title", "Reactivar Editorial");        
        mav.addObject("editorial", editorialServicio.buscarPorNombre(nombre)); 
        mav.addObject("action", "guardar2"); 

        return mav;
    }
    
    @PostMapping("/guardar2")
    public RedirectView activarOn(@RequestParam String id, RedirectAttributes redirectAttributes) throws Exception{
        try{
            editorialServicio.reactivar(id);
            redirectAttributes.addFlashAttribute("exito","EDITORIAL DADA DE ALTA NUEVAMENTE");
                     
        }catch(Exception e){
            return new RedirectView("/editoriales");
        }
        
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre, RedirectAttributes redirectAttributes) throws Exception {
        try{
           if (nombre == null || nombre.trim().isEmpty()) {
                throw new Exception("EL NOMBRE DE LA EDITORIAL ES OBLIGATORIO");
            } 
           editorialServicio.crearEditorial(nombre);
            redirectAttributes.addFlashAttribute("exito","EDITORIAL DADA DE ALTA");
        }catch(Exception e){
            if(e.getMessage().equals("editorialEnLista")){
                redirectAttributes.addFlashAttribute("error","LA EDITORIAL YA SE ENCUENTRA EN LA LISTA");
                return new RedirectView("/editoriales");
            }else{
                 return new RedirectView("/editoriales/reactivar/"+nombre);
            }
        }        

        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView mav = new ModelAndView("editorial-formulario");
        try{
            editorialServicio.modificarEditorial(id, nombre);
            redirectAttributes.addFlashAttribute("exito","EDITORIAL MODIFICADA CON EXITO");
        }catch(Exception e){
          if(e.getMessage().equals("editorialEnLista")){
                redirectAttributes.addFlashAttribute("error","HAY UNA EDITORIAL REGISTRADA CON ESE NOMBRE");
                return new RedirectView("/editoriales");
            }else{
                 return new RedirectView("/editoriales/reactivar/"+nombre);
            }  
        }
        
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id,RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView mav = new ModelAndView("editorial-formulario");
        try{
            editorialServicio.eliminar(id);
            redirectAttributes.addFlashAttribute("exito","EDITORIAL ELIMINADA CON EXITO");

        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            mav.setViewName("redirect:/editoriales");
        }
        
        return new RedirectView("/editoriales");
    }
    
}
