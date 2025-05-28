package com.pedroelarissa.projetoso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directory extends Entry{
    private Map<String, Entry> entries;

    public Directory(String name, String path, LocalDateTime createdAt) {
        super(name, path, createdAt);
        this.entries = new HashMap<>();

    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    public void addEntry(Entry addedEntry){
        if(entries.containsKey(addedEntry.getName())) {
            System.out.println("Operação impedida: já existe um arquivo ou diretório com o nome inserido");
            return;
        }
        entries.put(addedEntry.getName(), addedEntry);
    }
    public void removeEntry(String name){
        if (!entries.containsKey(name)){
            System.out.println("Operação impedida: arquivo ou diretório não encontrado");
        }
        entries.remove(name);
    }
    public Entry getEntry(String name){
        if (!entries.containsKey(name)){
            System.out.println("Operação impedida: arquivo ou diretório não encontrado");
        }
        return entries.get(name);
    }

    public void listEntries() {
        List<Entry> entriesList = new ArrayList<>();
        for (String name : entries.keySet()) {
            Entry entry = entries.get(name);
            String type = (entry.isDirectory()) ? "[DIR] " : "[FILE] ";
            System.out.println(type + name);
            entriesList.add(entry);
        }
    }
}
