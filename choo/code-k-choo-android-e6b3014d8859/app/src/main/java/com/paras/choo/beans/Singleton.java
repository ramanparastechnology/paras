package com.paras.choo.beans;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paras on 24-08-2015.
 */
public class Singleton {
    List<ParseObject> ticketListTemp =  new ArrayList<ParseObject>();
    private static Singleton instance = null;
    Singleton(){}
    public static Singleton getInstance() {
        if(instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public List<ParseObject> getTicketListTemp() {
        return ticketListTemp;
    }

    public void setTicketListTemp(List<ParseObject> ticketListTemp) {
        this.ticketListTemp = ticketListTemp;
    }
}
