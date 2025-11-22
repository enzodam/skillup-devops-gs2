package br.com.fiap.skillup.dto;

public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String profissaoAtual;
    private String metaProfissional;
    private String tipo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfissaoAtual() { return profissaoAtual; }
    public void setProfissaoAtual(String profissaoAtual) { this.profissaoAtual = profissaoAtual; }

    public String getMetaProfissional() { return metaProfissional; }
    public void setMetaProfissional(String metaProfissional) { this.metaProfissional = metaProfissional; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
