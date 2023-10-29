package pe.edu.cibertec.DAWII_CL2_CastilloTaraEnzo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.DAWII_CL2_CastilloTaraEnzo.model.Usuario;
import pe.edu.cibertec.DAWII_CL2_CastilloTaraEnzo.service.UsuarioService;

@AllArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(){
        return "frontoffice/auth/frmLogin";
    }

    @GetMapping("/actualizar")
    public String cambiar(){
        return "frontoffice/auth/frmActualizarPass";
    }

    @GetMapping("/registrar")
    public String registrar(){
        return "frontoffice/auth/frmRegistroUsuario";
    }

    @PostMapping("/login-success")
    public String loginSucces(HttpServletRequest request){
        UserDetails usuario = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        HttpSession session = request.getSession();
        session.setAttribute("usuario", usuario.getUsername());
        return "frontoffice/auth/home";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@ModelAttribute Usuario usuario){
        usuarioService.saveUser(usuario);
        return "frontoffice/auth/frmLogin";
    }

    @PostMapping("/auth/cambiar-password")
    public String cambiarPassword(@RequestParam String newPassword, @RequestParam String confirmPassword, Model model) {
        if (newPassword.equals(confirmPassword)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();

            Usuario usuario = usuarioService.findUserByUserName(currentUserName);
            usuario.setPassword(newPassword);
            usuarioService.saveUser(usuario);

            model.addAttribute("mensaje", "Contraseña cambiada correctamente");
        } else {
            model.addAttribute("mensaje", "Las contraseñas no coinciden. Vuelva a intentarlo.");
        }
        return "frontoffice/auth/frmLogin";
    }
}
