package ru.almatel.vats.domain;

public class BotPropertyHolder {
    private String name;
    private String token;
    private Long proxyId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    @Override
    public String toString() {
        return String.format("name: %s, token: %s, proxyId: %d", name, token, proxyId);
    }
}