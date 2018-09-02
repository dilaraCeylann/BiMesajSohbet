package com.elias.bimesajsohbet;

import android.support.v7.widget.RecyclerView;

public class Konusmalar {

    private RecyclerView myChatsList;

    private String user_status;

    public Konusmalar(){

    }

    public Konusmalar(String user_status) {
        this.user_status = user_status;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
}
