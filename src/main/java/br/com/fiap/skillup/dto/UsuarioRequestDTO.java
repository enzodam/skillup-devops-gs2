package br.com.fiap.skillup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioRequestDTO {

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private String profissaoAtual;

    @NotBlank
    private String metaProfissional;

    /**
     * "USER" ou "ADMIN"
     */
    @NotBlank
    private String tipo;

    // getters e setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getProfissaoAtual() {
        return profissaoAtual;
    }

    public void setProfissaoAtual(String profissaoAtual) {
        this.profissaoAtual = profissaoAtual;
    }

    public String getMetaProfissional() {
        return metaProfissional;
    }

    public void setMetaProfissional(String metaProfissional) {
        this.metaProfissional = metaProfissional;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
