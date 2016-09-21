package com.paras.choo.beans;

/**
 * Created by paras on 14-09-2015.
 */
public class AudienceListItems {
    private String title;
    private int tick;

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    private boolean ischeck  = false;
    public AudienceListItems(){}

    public AudienceListItems(String title,boolean ischeck){
        this.title = title;
        this.ischeck = ischeck;
    }
    public AudienceListItems(String title, int tick){
        this.title = title;
        this.tick = tick;
    }
    public String getTitle(){
        return this.title;
    }

    public int getTick(){
        return this.tick;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setTick(int tick){
        this.tick = tick;
    }
}

