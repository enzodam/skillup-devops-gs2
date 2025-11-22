package br.com.fiap.skillup.dto;

public class CursoResponseDTO {

    private Long id;
    private String nome;
    private String area;
    private String nivel;
    private Integer cargaHoraria;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Integer getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(Integer cargaHoraria) { this.cargaHoraria = cargaHoraria; }
}
