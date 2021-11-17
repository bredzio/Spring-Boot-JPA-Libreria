package com.egg.libreriaspring.servicios;

import com.egg.libreriaspring.entidades.Usuario;
import com.egg.libreriaspring.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService{
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio; 
    
    @Autowired
    private BCryptPasswordEncoder encoder;
    
    private final String MENSAJE_USERNAME = "No existe un usuario registrado con el correo %s";
    
    @Transactional
    public void crear(Long dni, String nombre, String apellido, LocalDate fechaNacimiento, String correo, String clave) throws Exception{
        if (usuarioRepositorio.existsById(dni)) {
            throw new Exception("Ya existe un usuario asociado con el DNI ingresado");
        }
        
        if(usuarioRepositorio.existsUsuarioByCorreo(correo)){
            throw new Exception("Ya existe un usuario asociado al correo ingresado");
        }
        
        
        Usuario usuario = new Usuario();
        usuario.setDni(dni);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setCorreo(correo);
        usuario.setClave(encoder.encode(clave));
        usuario.setAlta(true);
        
        usuarioRepositorio.save(usuario);
        
    }
    
    @Transactional
    public void modificar(Long dni, String nombre, String apellido, LocalDate fechaNacimiento, String correo, String clave) {
        usuarioRepositorio.modificar(dni, nombre, apellido, fechaNacimiento, correo, encoder.encode(clave));
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorDni(Long dni) {
        return usuarioRepositorio.findById(dni).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepositorio.findByCorreo(correo);
    }

    @Transactional
    public void habilitar(Long dni) {
        usuarioRepositorio.habilitar(dni);
    }

    @Transactional
    public void eliminar(Long dni) {
        usuarioRepositorio.deleteById(dni);
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        
        if(usuario == null){
            throw new UsernameNotFoundException("No existe un usuario asociado al correo ingresado");
        }
        
        if(!usuario.getAlta()){
            throw new UsernameNotFoundException("USUARIO DADO DE BAJA");
        }       
        
        return new User(usuario.getCorreo(),usuario.getClave(),Collections.emptyList());
    }
    
}
