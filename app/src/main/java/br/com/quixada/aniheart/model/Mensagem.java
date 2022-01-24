package br.com.quixada.aniheart.model;

public class Mensagem {

    private String mensagem;
    private String email;
    private String name;
    private Long timestamp;

    public Mensagem() {
    }

    public Mensagem(String mensagem, String email, String name) {
        this.mensagem = mensagem;
        this.email = email;
        this.name = name;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

