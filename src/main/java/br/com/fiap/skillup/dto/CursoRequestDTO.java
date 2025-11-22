package br.com.fiap.skillup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CursoRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String area;

    @NotBlank
    private String nivel;

    @Positive
    private Integer cargaHoraria;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Integer getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(Integer cargaHoraria) { this.cargaHoraria = cargaHoraria; }
}
