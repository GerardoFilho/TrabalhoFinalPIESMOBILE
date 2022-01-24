package br.com.quixada.aniheart.model;


public class Usuario {

    private String email;
    private String name;
    private String url;

    public Usuario() {

    }

    public Usuario(String email, String name) {
        this.email = email;
        this.name = name;
        this.url = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


