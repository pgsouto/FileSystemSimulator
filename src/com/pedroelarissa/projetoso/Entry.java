package com.pedroelarissa.projetoso;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Entry implements Serializable {
    protected String name;
    /*protected String path;*/
    protected LocalDateTime createdAt;
    protected Directory parent;
    public Entry(String name,/* String path,*/ LocalDateTime createdAt, Directory parent) {
        this.name = name;
        /*this.path = path;*/
        this.createdAt = createdAt;
        this.parent = parent;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /*
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }*/

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public abstract boolean isDirectory();

    public String getFullPath() {
        if (parent == null) {
            // raiz, pode ser só "/"
            return "/" + name;
        } else {
            // concatena caminho do pai + / + nome atual
            String parentPath = parent.getFullPath();
            if (parentPath.equals("/")) {
                return parentPath + name;
            } else {
                return parentPath + "/" + name;
            }
        }
    }

}
