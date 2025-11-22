package br.com.fiap.skillup.controller;

import br.com.fiap.skillup.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        // templates/auth/login.html
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm() {
        // templates/auth/register.html
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String nome,
                                  @RequestParam String email,
                                  @RequestParam String senha,
                                  @RequestParam(required = false) String profissaoAtual,
                                  @RequestParam String metaProfissional,
                                  @RequestParam String tipo,
                                  RedirectAttributes redirectAttributes) {

        try {
            usuarioService.registrarNovoUsuario(
                    nome,
                    email,
                    senha,
                    profissaoAtual,
                    metaProfissional,
                    tipo
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Conta criada com sucesso! Faça login."
            );
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao registrar usuário.");
            return "redirect:/register";
        }
    }
}
