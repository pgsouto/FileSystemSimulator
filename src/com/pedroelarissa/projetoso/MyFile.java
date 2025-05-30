package com.pedroelarissa.projetoso;

import java.time.LocalDateTime;

public class MyFile extends Entry{
    private String content;
    private long size;

    public MyFile(String name, /*String path,*/ LocalDateTime createdAt, String content, Directory parent) {
        super(name,/* path,*/ createdAt, parent);
        this.content = (content == null) ? "" : content;
        this.size = this.content.getBytes().length;;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

}
