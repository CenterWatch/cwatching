package com.cw.models;

public class Ocorrencia {
    private Integer idOcorrencia;
    private String titulo;
    private String descricao;
    private String tipo;
    private Boolean resolvido;
    private String criadoEm;
    private String resolvidoEm;
    private Integer fkSessao;
    private Integer fkAtribuido;

    public Ocorrencia(String titulo, String descricao, String tipo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    public Ocorrencia() {
    }

    public Integer getIdOcorrencia() {
        return idOcorrencia;
    }

    public void setIdOcorrencia(Integer idOcorrencia) {
        this.idOcorrencia = idOcorrencia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getResolvido() {
        return resolvido;
    }

    public void setResolvido(Boolean resolvido) {
        this.resolvido = resolvido;
    }

    public String getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(String criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getResolvidoEm() {
        return resolvidoEm;
    }

    public void setResolvidoEm(String resolvidoEm) {
        this.resolvidoEm = resolvidoEm;
    }

    public Integer getFkSessao() {
        return fkSessao;
    }

    public void setFkSessao(Integer fkSessao) {
        this.fkSessao = fkSessao;
    }

    public Integer getFkAtribuido() {
        return fkAtribuido;
    }

    public void setFkAtribuido(Integer fkAtribuido) {
        this.fkAtribuido = fkAtribuido;
    }

    @Override
    public String toString() {
        return "Ocorrencia{" +
                "idOcorrencia=" + idOcorrencia +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", tipo='" + tipo + '\'' +
                ", resolvido=" + resolvido +
                ", criadoEm='" + criadoEm + '\'' +
                ", resolvidoEm='" + resolvidoEm + '\'' +
                ", fkSessao=" + fkSessao +
                ", fkAtribuido=" + fkAtribuido +
                '}';
    }
}
