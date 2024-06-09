package com.cw.models;

public class Volume {
    private String UUID;
    private String nome;
    private String pontoMontagem;
    private Long volumeTotal;
    private Integer fkMaquina;

    public Volume(String UUID, String nome, String pontoMontagem, Long volumeTotal, Integer fkMaquina) {
        this.UUID = UUID;
        this.nome = nome;
        this.pontoMontagem = pontoMontagem;
        this.volumeTotal = volumeTotal;
        this.fkMaquina = fkMaquina;
    }

    public Volume(String UUID, String nome, String pontoMontagem, Long volumeTotal) {
        this.UUID = UUID;
        this.nome = nome;
        this.pontoMontagem = pontoMontagem;
        this.volumeTotal = volumeTotal;
    }

    public Volume(Volume other) {
        this.UUID = other.UUID;
        this.nome = other.nome;
        this.pontoMontagem = other.pontoMontagem;
        this.volumeTotal = other.volumeTotal;
        this.fkMaquina = other.fkMaquina;
    }

    public Volume() {
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPontoMontagem() {
        return pontoMontagem;
    }

    public void setPontoMontagem(String pontoMontagem) {
        this.pontoMontagem = pontoMontagem;
    }

    public Long getVolumeTotal() {
        return volumeTotal;
    }

    public void setVolumeTotal(Long volumeTotal) {
        this.volumeTotal = volumeTotal;
    }

    public Integer getFkMaquina() {
        return fkMaquina;
    }

    public void setFkMaquina(Integer fkMaquina) {
        this.fkMaquina = fkMaquina;
    }

    @Override
    public String toString() {
        return "Volume{" +
                "UUID='" + UUID + '\'' +
                ", nome='" + nome + '\'' +
                ", pontoMontagem='" + pontoMontagem + '\'' +
                ", volumeTotal=" + volumeTotal +
                ", fkMaquina=" + fkMaquina +
                '}';
    }
}
