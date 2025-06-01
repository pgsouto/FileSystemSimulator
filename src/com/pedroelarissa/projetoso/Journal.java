package com.pedroelarissa.projetoso;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Journal {
    private final File journalFile;

    public Journal(String journalFileName) {
        this.journalFile = new File(journalFileName);
        try {
            if (!journalFile.exists()) {
                File parent = journalFile.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                journalFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo de journal: " + e.getMessage());
        }
    }

    public synchronized void appendEntry(String entry) {
        try (FileWriter fw = new FileWriter(journalFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(entry);
            out.flush();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no journal: " + e.getMessage());
        }
    }

    public synchronized List<String> loadEntries() {
        List<String> entries = new ArrayList<>();
        try {
            entries = Files.readAllLines(journalFile.toPath());
        } catch (IOException e) {
            System.err.println("Erro ao ler entradas do journal: " + e.getMessage());
        }
        return entries;
    }

    public synchronized void clear() {
        try (FileWriter fw = new FileWriter(journalFile, false)) {
            fw.write("");
            fw.flush();
        } catch (IOException e) {
            System.err.println("Erro ao limpar o journal: " + e.getMessage());
        }
    }
}
