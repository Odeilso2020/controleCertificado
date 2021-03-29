package br.com.progiv.appcontrolecertificado.Model;

import java.io.Serializable;

// Modelo da classe. Abstração da tabela BD.

public class CertificadosModel implements Serializable {

    private Long id;
    private String titulo;
    private String descricao;
    private int quantidade;

    //
    private String foto;
    //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    //
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    //

    @Override
    public String toString() {
        return "Titulo: " + titulo.toString() + "\n Horas: " + quantidade + " Hrs";
    }
}