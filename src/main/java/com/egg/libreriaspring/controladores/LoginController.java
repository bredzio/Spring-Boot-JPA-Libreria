package com.egg.libreriaspring.controladores;

import com.egg.libreriaspring.entidades.Rol;
import com.egg.libreriaspring.entidades.Usuario;
import com.egg.libreriaspring.servicios.UsuarioServicio;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {
     
    @Autowired
    private UsuarioServicio usuarioServicio;
    //private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal) {
        ModelAndView mav = new ModelAndView("login");
        
        if (error != null) {
            mav.addObject("error", "Correo o contraseña inválida");
        }
        
        if (logout != null) {
            mav.addObject("logout", "Ha salido correctamente de la plataforma");
        }

        if (principal != null) {
            //LOGGER.info("Principal -> {}", principal.getName());
            mav.setViewName("redirect:/");
        }

        return mav;
    }
    
    @GetMapping("/signup")
    public ModelAndView signup(HttpServletRequest request, Principal principal) {
        ModelAndView mav = new ModelAndView("signup");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("nombre", flashMap.get("nombre"));
            mav.addObject("apellido", flashMap.get("apellido"));
            mav.addObject("fechaNacimiento", flashMap.get("fechaNacimiento"));
            mav.addObject("dni", flashMap.get("dni"));
            mav.addObject("correo", flashMap.get("correo"));
            mav.addObject("clave", flashMap.get("clave"));
        }
        if (principal != null) {
            //LOGGER.info("Principal -> {}", principal.getName());
            mav.setViewName("redirect:/");
        }
        return mav;
    }

    @PostMapping("/registro")
    public RedirectView signup(@RequestParam Rol rol,@ModelAttribute Usuario usuario, RedirectAttributes attributes, HttpServletRequest request) {
        RedirectView redirectView = new RedirectView("/login");
    
        try {
            usuarioServicio.crear(usuario.getDni(), usuario.getNombre(), usuario.getApellido(), usuario.getFechaNacimiento(), usuario.getCorreo(), usuario.getClave(),rol);
            attributes.addFlashAttribute("exito", "SE HA REGISTRADO CON ÉXITO.");
            //request.login(correo, clave);
            //redirectView.setUrl("/index");

        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
            attributes.addFlashAttribute("nombre", usuario.getNombre());
            attributes.addFlashAttribute("apellido", usuario.getApellido());
            attributes.addFlashAttribute("correo", usuario.getCorreo());
            attributes.addFlashAttribute("clave", usuario.getClave());
            redirectView.setUrl("/signup");
        }
        return redirectView;
    }
}
