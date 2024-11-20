package br.com.fiap.javags.controller;

import br.com.fiap.javags.dto.request.EletroRequest;
import br.com.fiap.javags.dto.response.EletroResponse;
import br.com.fiap.javags.dto.response.UserResponse;
import br.com.fiap.javags.service.EletroService;
import br.com.fiap.javags.service.UserService;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Controller
@RequestMapping("/eletros")
public class EletroController {

    private  RabbitTemplate rabbitTemplate;

    private final EletroService eletroService;
    private final UserService userService;

    @Autowired
    public EletroController(RabbitTemplate rabbitTemplate, UserService userService, EletroService eletroService) {
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
        this.eletroService = eletroService;
    }

    // Exibe os eletrodomésticos associados a um usuário específico
    @GetMapping("/usuario/{userId}")
    public String getEletrosByUser(@PathVariable Long userId, Model model) {
        UserResponse user = userService.findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado para o ID: " + userId);
        }

        Collection<EletroResponse> eletros = eletroService.findByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("eletros", eletros);
        model.addAttribute("eletroRequest", new EletroRequest()); // Inicializa formulário vazio para criação
        model.addAttribute("currentUserId", userId);
        return "eletros";
    }

    // Salva ou atualiza um eletrodoméstico
    @PostMapping("/usuario/{userId}/salvar")
    public String saveOrUpdateEletro(@PathVariable Long userId,
                                     @Valid @ModelAttribute("eletroRequest") EletroRequest eletroRequest,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            UserResponse user = userService.findUserById(userId);
            Collection<EletroResponse> eletros = eletroService.findByUserId(userId);
            model.addAttribute("user", user);
            model.addAttribute("eletros", eletros);
            model.addAttribute("currentUserId", userId);
            return "eletros";
        }

        eletroRequest.setUserId(userId); // Garante que o eletro está associado ao usuário correto

        if (eletroRequest.getId() == null) {
            eletroService.save(eletroRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Eletrodoméstico criado com sucesso!");
            rabbitTemplate.convertAndSend("email-queue", "Novo eletro cadastrado" );
        } else {
            eletroService.update(eletroRequest.getId(), eletroRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Eletrodoméstico atualizado com sucesso!");
        }

        return "redirect:/eletros/usuario/" + userId;
    }

    // Exibe o formulário de edição
    @GetMapping("/{id}/editar")
    public String editEletro(@PathVariable Long id, Model model) {
        EletroResponse eletro = eletroService.findByIdResponse(id);
        if (eletro == null) {
            throw new IllegalArgumentException("Eletrodoméstico não encontrado para o ID: " + id);
        }

        UserResponse user = userService.findUserById(eletro.getUserId());
        Collection<EletroResponse> eletros = eletroService.findByUserId(eletro.getUserId());

        model.addAttribute("eletroRequest", eletroService.toRequest(eletro));
        model.addAttribute("user", user);
        model.addAttribute("eletros", eletros);
        model.addAttribute("currentUserId", eletro.getUserId());
        return "eletros";
    }

    // Exclui um eletrodoméstico
    @PostMapping("/{id}/excluir")
    public String deleteEletro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Long userId = eletroService.findByIdResponse(id).getUserId();
        eletroService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Eletrodoméstico excluído com sucesso!");
        return "redirect:/eletros/usuario/" + userId;
    }
}
