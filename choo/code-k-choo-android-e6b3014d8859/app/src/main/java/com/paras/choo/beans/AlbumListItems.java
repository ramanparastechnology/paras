package com.paras.choo.beans;

/**
 * Created by paras on 14-09-2015.
 */
public class AlbumListItems {
    private String title;
    private int icon;
    private int tick;

    public AlbumListItems(){}

    public AlbumListItems(int icon, String title, int tick){
        this.tick = tick;
        this.title = title;
        this.icon = icon;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }
}
