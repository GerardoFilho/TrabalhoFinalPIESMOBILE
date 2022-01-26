package br.com.quixada.aniheart.model;

import java.util.ArrayList;
import java.util.List;

public class Manga {

    private String titulo;
    private String autores;
    private String linkImage;
    private List<String> capitulos;
    private List<Mensagem> mensagens;
    private Studio studio;

    public Manga() {
    }

    public Manga(String titulo, String autores, String linkImage, Studio studio) {
        this.titulo = titulo;
        this.autores = autores;
        this.linkImage = linkImage;
        this.mensagens = new ArrayList<>();
        this.capitulos = new ArrayList<>();
        this.studio = studio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }

    public List<String> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<String> capitulos) {
        this.capitulos = capitulos;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

}
