package com.pedroelarissa.projetoso;
import java.io.File;
import java.time.LocalDateTime;

public class FileSystemSimulator {
    private Directory root;
    private Directory currentDirectory;
    private File diskFile;

    public FileSystemSimulator(String diskFileName) {
        this.diskFile = new File(diskFileName);

        if(diskFile.exists()){
            load();
        } else {
            this.root = new Directory("meuso:", "/", LocalDateTime.now());
            this.currentDirectory = root;
            save();
        }
    }

    private void load(){
    }

    private void save(){
    }
}
