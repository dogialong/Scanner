package com.cst.scanner.Model;

/**
 * Created by longdg on 04/05/2017.
 */

public class FileObject {
    private String nameFile,dateFile,status,pathFile;

    public FileObject(String nameFile,String pathFile  ,String dateFile ,String status) {
        this.nameFile = nameFile;
        this.dateFile = dateFile;
        this.status = status;
        this.pathFile = pathFile;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getDateFile() {
        return dateFile;
    }

    public void setDateFile(String dateFile) {
        this.dateFile = dateFile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}