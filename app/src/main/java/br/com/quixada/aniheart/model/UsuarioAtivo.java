package br.com.quixada.aniheart.model;


public class UsuarioAtivo {

    private String login;
    private Long timestamp;

    public UsuarioAtivo() {

    }

    public UsuarioAtivo(String login, Long timestamp) {
        this.login = login;
        this.timestamp = timestamp;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
