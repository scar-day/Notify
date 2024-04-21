package me.scarday.notify.social;

public interface Social {
    void sendMessage(Builder builder);
    boolean isConnected();
}
