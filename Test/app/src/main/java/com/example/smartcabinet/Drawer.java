package com.example.smartcabinet;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class Drawer {
    private int drawerId;
    private int boxId;
    private int drawerSize;

    public Drawer(){
        this.drawerId = -1;
        this.boxId = 1;
        this.drawerSize = 2;
    }

    public Drawer(int dId, int bId, int dS){
        this.drawerId = dId;
        this.boxId = bId;
        this.drawerSize = dS;
    }
}
