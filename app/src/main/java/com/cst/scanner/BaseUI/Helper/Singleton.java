package com.cst.scanner.BaseUI.Helper;

/**
 * Created by longdg on 28/04/2017.
 */

public class Singleton {
    private static Singleton instance = null;
    public static Singleton getGetInstance(){
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }
    public  boolean isLocation = false;
}
