package br.com.quixada.aniheart.model;

public class Mensagem {

    private String messenger;
    private String email;
    private String name;
    private Long timestamp;

    public Mensagem() {
    }

    public Mensagem(String messenger, String email, String name) {
        this.messenger = messenger;
        this.email = email;
        this.name = name;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
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

