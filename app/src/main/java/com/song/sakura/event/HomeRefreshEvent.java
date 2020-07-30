package com.song.sakura.event;

public class HomeRefreshEvent {
    public boolean isRefresh = false;
    public boolean isOver = false;

    public HomeRefreshEvent(boolean isRefresh, boolean isOver) {
        this.isRefresh = isRefresh;
        this.isOver = isOver;
    }
}
