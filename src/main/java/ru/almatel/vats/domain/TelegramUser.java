package ru.almatel.vats.domain;

public class TelegramUser {
    private String apiKey;
    private String name;
    private String chatId;
    private int state;
    private String description;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId.toString();
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId.toString();
    }



    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
