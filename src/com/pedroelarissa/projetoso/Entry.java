package com.pedroelarissa.projetoso;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Entry implements Serializable {
    protected String name;
    protected String path;
    protected LocalDateTime createdAt;

    public Entry(String name, String path, LocalDateTime createdAt) {
        this.name = name;
        this.path = path;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public abstract boolean isDirectory();
}
