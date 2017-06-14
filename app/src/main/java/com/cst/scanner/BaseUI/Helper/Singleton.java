package com.cst.scanner.BaseUI.Helper;

import com.cst.scanner.Model.FileObject;

import java.util.ArrayList;

/**
 * Created by longdg on 28/04/2017.
 */

public class Singleton {
    private static Singleton instance = null;
    public static String KEY_SHAREPREFERENCE = "KEY_SHAREPREFERENCE";
    public static Singleton getGetInstance(){
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }
    public ArrayList<FileObject> arrayList = new ArrayList<>();
    public  boolean isLocation = false;
    public String linkUrlImage = "";
    public String key_json = "image_link_arr";
    public boolean isStorage = false;
    public int positionSeleted = -1;
    public String where = "";
}
