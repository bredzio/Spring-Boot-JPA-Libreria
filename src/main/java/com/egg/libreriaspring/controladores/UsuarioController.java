package com.egg.libreriaspring.controladores;

import com.egg.libreriaspring.entidades.Usuario;
import com.egg.libreriaspring.servicios.UsuarioServicio;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping
    public ModelAndView mostrarTodos(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuarios");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("usuarios", usuarioServicio.buscarTodos());
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crearUsuario(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuario-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("usuario", flashMap.get("usuario"));
        } else {
            mav.addObject("usuario", new Usuario());
        }

        mav.addObject("title", "Crear Usuario");
        mav.addObject("action", "guardar");
        return mav;
    }
    
    @GetMapping("/editar/{dni}")
    public ModelAndView editarUsuario(@PathVariable Long dni) {
        ModelAndView mav = new ModelAndView("usuario-formulario");
        mav.addObject("usuario", usuarioServicio.buscarPorDni(dni));
        mav.addObject("title", "Editar Usuario");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@ModelAttribute Usuario usuario, RedirectAttributes attributes) {
        try {            
            usuarioServicio.crear(usuario.getDni(), usuario.getNombre(), usuario.getApellido(), usuario.getFechaNacimiento(), usuario.getCorreo(), usuario.getClave());
            attributes.addFlashAttribute("exito", "El usuario ha sido creado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("usuario", usuario);
            attributes.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/usuarios/crear");
        }
        return new RedirectView("/usuarios");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@ModelAttribute Usuario usuario, RedirectAttributes attributes) {
        usuarioServicio.modificar(usuario.getDni(),usuario.getNombre(),usuario.getApellido(),usuario.getFechaNacimiento(),usuario.getCorreo(),usuario.getClave());
        return new RedirectView("/usuarios");
    }

    @PostMapping("/habilitar/{dni}")
    public RedirectView habilitar(@PathVariable Long dni) {
        usuarioServicio.habilitar(dni);
        return new RedirectView("/usuarios");
    }

    @PostMapping("/eliminar/{dni}")
    public RedirectView eliminar(@PathVariable Long dni) {
        usuarioServicio.eliminar(dni);
        return new RedirectView("/usuarios");
    }
    
    
    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, @RequestParam(required =false) String logout, Principal principal){
        ModelAndView modelAndView = new ModelAndView ("login");
        
        if(error != null){
            modelAndView.addObject("error", "Correo o contraseña inválido");
        }
        
        if(logout != null){
            modelAndView.addObject("logout", "Ha salido correctamente de la plataforma");
        }
        
        if(principal != null){
            modelAndView.setViewName("redirect:/");
        }
        
        return modelAndView;
    }
    
    @GetMapping("/signup")
    public ModelAndView signup(HttpServletRequest request, Principal principal){
        ModelAndView modelAndView = new ModelAndView("usuario-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        if(flashMap != null){
            modelAndView.addObject("exito",flashMap.get("exito"));
            modelAndView.addObject("error",flashMap.get("error"));
            modelAndView.addObject("nombre",flashMap.get("nombre"));
            modelAndView.addObject("apellido",flashMap.get("apellido"));
            modelAndView.addObject("correo",flashMap.get("correo"));
            modelAndView.addObject("clave",flashMap.get("clave"));
        }
        
        if(principal != null){
            modelAndView.setViewName("redirect:/");
        }
        
        return modelAndView;
        
    }
    
    @PostMapping("/registro")
    public RedirectView signup(@RequestParam String nombre,@RequestParam Long dni, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave,@RequestParam LocalDate fechaNacimiento, RedirectAttributes attributes){
        RedirectView redirectView = new RedirectView ("/login");
        
        try{
            usuarioServicio.crear(dni, nombre, apellido, fechaNacimiento, correo, clave);
        }catch(Exception e){
            attributes.addFlashAttribute("error", e.getMessage());
            attributes.addFlashAttribute("nombre", nombre);
            attributes.addFlashAttribute("apellido", apellido);
            attributes.addFlashAttribute("correo", correo);
            attributes.addFlashAttribute("clave", clave);
            
            redirectView.setUrl("/signup");
        }
        
        return redirectView;
    }
    
}
