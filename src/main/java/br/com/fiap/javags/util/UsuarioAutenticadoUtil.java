//package br.com.fiap.javags.util;
//
//import br.com.fiap.javags.entity.Usuario;
//import br.com.fiap.javags.service.UserService;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UsuarioAutenticadoUtil {
//
//    private final UserService userService;
//
//    public UsuarioAutenticadoUtil(UserService userService) {
//        this.userService = userService;
//    }
//
//    public Usuario obterUsuarioAtual() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated()) {
//            Object principal = auth.getPrincipal();
//            if (principal instanceof OAuth2User) {
//                OAuth2User oAuth2User = (OAuth2User) principal;
//                String email = oAuth2User.getAttribute("email");
//                if (email != null) {
//                    return userService.findByEmail(email);
//                }
//            }
//        }
//        throw new RuntimeException("Usuário não autenticado");
//    }
//}
