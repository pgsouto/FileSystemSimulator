package com.pedroelarissa.projetoso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directory extends Entry{
    private Map<String, Directory> subDirs;
    private Map<String, MyFile> files;

    public Directory(String name, /*String path, */LocalDateTime createdAt, Directory parent) {
        super(name, /*path,*/ createdAt, parent);
        this.subDirs = new HashMap<>();
        this.files = new HashMap<>();
    }

    public Map<String, MyFile> getFiles() {
        return files;
    }
    public Map<String, Directory> getSubDirs() {
        return subDirs;
    }
    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

}
