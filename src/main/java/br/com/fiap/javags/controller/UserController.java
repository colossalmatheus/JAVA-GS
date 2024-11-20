package br.com.fiap.javags.controller;

import br.com.fiap.javags.dto.request.UserRequest;
import br.com.fiap.javags.dto.response.UserResponse;
import br.com.fiap.javags.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/usuarios")
public class UserController {

    private final UserService userService;

    // Construtor para injeção de dependência
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Exibe a página principal com a lista de usuários e o formulário de criação.
     */
    @GetMapping
    public String getAllUsers(Model model) {
        Collection<UserResponse> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("isEditing", false);
        model.addAttribute("userRequest", new UserRequest());
        return "usuarios"; // Nome do arquivo Thymeleaf: usuarios.html
    }

    /**
     * Exibe o formulário pré-preenchido para editar um usuário específico.
     */
    @GetMapping("/{id}/editar")
    public String getEditUserForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            UserResponse user = userService.findUserById(id);
            UserRequest userRequest = userService.toRequest(user);
            Collection<UserResponse> users = userService.findAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("isEditing", true);
            model.addAttribute("userRequest", userRequest);
            return "usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Usuário não encontrado.");
            return "redirect:/usuarios";
        }
    }

    /**
     * Processa a criação de um novo usuário.
     */
    @PostMapping
    public String createUser(@Valid @ModelAttribute("userRequest") UserRequest userRequest,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Collection<UserResponse> users = userService.findAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("isEditing", false);
            return "usuarios";
        }
        userService.createUser(userRequest);
        redirectAttributes.addFlashAttribute("message", "Usuário criado com sucesso!");
        return "redirect:/usuarios";
    }

    /**
     * Processa a atualização de um usuário existente.
     */
    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("userRequest") UserRequest userRequest,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Collection<UserResponse> users = userService.findAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("isEditing", true);
            return "usuarios";
        }
        try {
            userService.updateUser(id, userRequest);
            redirectAttributes.addFlashAttribute("message", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar usuário.");
        }
        return "redirect:/usuarios";
    }

    /**
     * Processa a deleção de um usuário.
     */
    @GetMapping("/{id}/excluir")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir usuário.");
        }
        return "redirect:/usuarios";
    }
}
