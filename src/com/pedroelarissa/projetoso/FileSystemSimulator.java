package com.pedroelarissa.projetoso;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class FileSystemSimulator {
    private Directory root;
    private Directory currentDirectory;
    private File diskFile;
    private Journal journal;

    public FileSystemSimulator(String diskFileName) {
        this.diskFile = new File(diskFileName);
        String journalFileName = diskFile.getParent() == null
                ? "journal.log"
                : diskFile.getParent() + File.separator + "journal.log";
        this.journal = new Journal(journalFileName);

        if (diskFile.exists()) {
            load();
            List<String> pending = journal.loadEntries();
            if (!pending.isEmpty()) {
                System.out.println("Reaplicando operações pendentes do journal...");
            }
            for (String entry : pending) {
                replayJournalEntry(entry);
            }
            save();
        } else {
            this.root = new Directory("meuso:", LocalDateTime.now(), null);
            this.currentDirectory = root;
            save();
        }
    }

    private void replayJournalEntry(String entry) {
        String[] tokens = entry.split(" ", 4); // [data] [COMANDO] arg1 [arg2]

        if (tokens.length < 2) {
            System.err.println("Entrada de journal inválida: " + entry);
            return;
        }

        String cmd = tokens[1].replace("[", "").replace("]", "");

        switch (cmd) {
            case "CREATE_DIR":
                createDirectoryByPath(tokens[2], false);
                break;
            case "DELETE_DIR":
                deleteDirectoryByPath(tokens[2], false);
                break;
            case "RENAME_DIR":
                renameDirectoryByPath(tokens[2], tokens[3], false);
                break;
            case "CREATE_FILE":
                createFileByPath(tokens[2], tokens.length > 3 ? tokens[3] : "", false);
                break;
            case "DELETE_FILE":
                deleteFileByPath(tokens[2], false);
                break;
            case "RENAME_FILE":
                renameFileByPath(tokens[2], tokens[3], false);
                break;
            case "COPY_FILE":
                copyFileByPath(tokens[2], tokens[3], false);
                break;
            default:
                System.err.println("Journal: comando desconhecido → " + entry);
        }
    }

    private void createDirectoryByPath(String fullPath, boolean writeJournal) {
        if (writeJournal) {
            journal.appendEntry("["+LocalDateTime.now()+"] " + "[CREATE_DIR] " + fullPath);
        }
        String normalized = fullPath.startsWith("/") ? fullPath.substring(1) : fullPath;
        String[] parts = normalized.split("/");
        Directory temp = root;

        int startIndex = 0;
        if (parts.length > 0 && parts[0].equals(root.getName())) {
            startIndex = 1;
        }

        for (int i = startIndex; i < parts.length - 1; i++) {
            String nome = parts[i];
            Directory next = temp.getSubDirs().get(nome);
            if (next == null) {
                System.err.println("Replay CREATE_DIR: caminho inválido → " + fullPath);
                return;
            }
            temp = next;
        }

        String nomeNovo = parts[parts.length - 1];
        if (temp.getSubDirs().containsKey(nomeNovo)) {
            return;
        }
        Directory newDir = new Directory(nomeNovo, LocalDateTime.now(), temp);
        temp.getSubDirs().put(nomeNovo, newDir);
    }

    private void deleteDirectoryByPath(String fullPath, boolean writeJournal) {
        if (writeJournal) {
            journal.appendEntry("["+LocalDateTime.now()+"] " + "[DELETE_DIR] " + fullPath);
        }
        String normalized = fullPath.startsWith("/") ? fullPath.substring(1) : fullPath;
        String[] parts = normalized.split("/");
        Directory temp = root;

        int startIndex = 0;
        if (parts.length > 0 && parts[0].equals(root.getName())) {
            startIndex = 1;
        }

        for (int i = startIndex; i < parts.length - 1; i++) {
            Directory next = temp.getSubDirs().get(parts[i]);
            if (next == null) {
                System.err.println("Replay DELETE_DIR: caminho inválido → " + fullPath);
                return;
            }
            temp = next;
        }

        String nomeAlvo = parts[parts.length - 1];
        if (temp.getSubDirs().containsKey(nomeAlvo)) {
            temp.getSubDirs().remove(nomeAlvo);
        }
    }

    private void renameDirectoryByPath(String fullPath, String novoNome, boolean writeJournal) {
        if (writeJournal) {
            journal.appendEntry("["+LocalDateTime.now()+"] " +"[RENAME_DIR] " + fullPath + " " + novoNome);
        }
        String normalized = fullPath.startsWith("/") ? fullPath.substring(1) : fullPath;
        String[] parts = normalized.split("/");
        Directory temp = root;

        int startIndex = 0;
        if (parts.length > 0 && parts[0].equals(root.getName())) {
            startIndex = 1;
        }

        for (int i = startIndex; i < parts.length - 1; i++) {
            Directory next = temp.getSubDirs().get(parts[i]);
            if (next == null) {
                System.err.println("Replay RENAME_DIR: caminho inválido → " + fullPath);
                return;
            }
            temp = next;
        }

        String antigo = parts[parts.length - 1];
        Directory dirAntigo = temp.getSubDirs().get(antigo);
        if (dirAntigo != null) {
            dirAntigo.setName(novoNome);
            temp.getSubDirs().remove(antigo);
            temp.getSubDirs().put(novoNome, dirAntigo);
        }
    }

    private void createFileByPath(String fullPath, String conteudo, boolean writeJournal) {
        if (writeJournal) {
            journal.appendEntry("["+LocalDateTime.now()+"] " +"[CREATE_FILE] " + fullPath + " " + conteudo.replaceAll("\\r?\\n", "\\\\n"));
        }
        String normalized = fullPath.startsWith("/") ? fullPath.substring(1) : fullPath;
        String[] parts = normalized.split("/");
        Directory temp = root;

        int startIndex = 0;
        if (parts.length > 0 && parts[0].equals(root.getName())) {
            startIndex = 1;
        }

        for (int i = startIndex; i < parts.length - 1; i++) {
            Directory next = temp.getSubDirs().get(parts[i]);
            if (next == null) {
                System.err.println("Replay CREATE_FILE: caminho inválido → " + fullPath);
                return;
            }
            temp = next;
        }

        String nomeArquivo = parts[parts.length - 1];
        if (temp.getFiles().containsKey(nomeArquivo)) {
            return;
        }
        MyFile newFile = new MyFile(nomeArquivo, LocalDateTime.now(), conteudo, temp);
        temp.getFiles().put(nomeArquivo, newFile);
    }

    private void deleteFileByPath(String fullPath, boolean writeJournal) {
        if (writeJournal) {
            journal.appendEntry("["+LocalDateTime.now()+"] " +"[DELETE_FILE] " + fullPath);
        }
        String normalized = fullPath.startsWith("/") ? fullPath.substring(1) : fullPath;
        String[] parts = normalized.split("/");
        Directory temp = root;

        int startIndex = 0;
        if (parts.length > 0 && parts[0].equals(root.getName())) {
            startIndex = 1;
        }

        for (int i = startIndex; i < parts.length - 1; i++) {
            Directory next = temp.getSubDirs().get(parts[i]);
            if (next == null) {
                System.err.println("Replay DELETE_FILE: caminho inválido → " + fullPath);
                return;
            }
            temp = next;
        }

        String nomeAlvo = parts[parts.length - 1];
        if (temp.getFiles().containsKey(nomeAlvo)) {
            temp.getFiles().remove(nomeAlvo);
        }
    }

    private void renameFileByPath(String fullPath, String novoNome, boolean writeJournal) {
        if (writeJournal) {
            journal.appendEntry("["+LocalDateTime.now()+"] " +"[RENAME_FILE] " + fullPath + " " + novoNome);
        }
        String normalized = fullPath.startsWith("/") ? fullPath.substring(1) : fullPath;
        String[] parts = normalized.split("/");
        Directory temp = root;

        int startIndex = 0;
        if (parts.length > 0 && parts[0].equals(root.getName())) {
            startIndex = 1;
        }

        for (int i = startIndex; i < parts.length - 1; i++) {
            Directory next = temp.getSubDirs().get(parts[i]);
            if (next == null) {
                System.err.println("Replay RENAME_FILE: caminho inválido → " + fullPath);
                return;
            }
            temp = next;
        }

        String antigo = parts[parts.length - 1];
        MyFile arquivoAntigo = temp.getFiles().get(antigo);
        if (arquivoAntigo != null) {
            arquivoAntigo.setName(novoNome);
            temp.getFiles().remove(antigo);
            temp.getFiles().put(novoNome, arquivoAntigo);
        }
    }

    private void copyFileByPath(String origemPath, String destinoDirPath, boolean writeJournal) {
        if (writeJournal) {
            journal.appendEntry("["+LocalDateTime.now()+"] " +"[COPY_FILE] " + origemPath + " " + destinoDirPath);
        }
        String normalizedOrigem = origemPath.startsWith("/") ? origemPath.substring(1) : origemPath;
        String[] partsOrigem = normalizedOrigem.split("/");
        Directory tempOrigemParent = root;

        int startIndexOrigem = 0;
        if (partsOrigem.length > 0 && partsOrigem[0].equals(root.getName())) {
            startIndexOrigem = 1;
        }

        for (int i = startIndexOrigem; i < partsOrigem.length - 1; i++) {
            Directory next = tempOrigemParent.getSubDirs().get(partsOrigem[i]);
            if (next == null) {
                System.err.println("Replay COPY_FILE: caminho inválido → " + origemPath);
                return;
            }
            tempOrigemParent = next;
        }

        MyFile arquivoOrigem = tempOrigemParent.getFiles().get(partsOrigem[partsOrigem.length - 1]);
        if (arquivoOrigem == null) {
            return;
        }

        String normalizedDestino = destinoDirPath.startsWith("/")
                ? destinoDirPath.substring(1)
                : destinoDirPath;
        String[] partsDestino = normalizedDestino.split("/");
        Directory tempDestino = root;

        int startIndexDestino = 0;
        if (partsDestino.length > 0 && partsDestino[0].equals(root.getName())) {
            startIndexDestino = 1;
        }

        for (int i = startIndexDestino; i < partsDestino.length; i++) {
            Directory next = tempDestino.getSubDirs().get(partsDestino[i]);
            if (next == null) {
                System.err.println("Replay COPY_FILE: diretório destino inválido → " + destinoDirPath);
                return;
            }
            tempDestino = next;
        }

        MyFile copia = new MyFile(arquivoOrigem.getName(), LocalDateTime.now(), arquivoOrigem.getContent(), tempDestino);
        tempDestino.getFiles().put(copia.getName(), copia);
    }

    public void createDirectory(String name) {
        String fullPath = currentDirectory.getFullPath().equals("/")
                ? "/" + name
                : currentDirectory.getFullPath() + "/" + name;
        createDirectoryByPath(fullPath, true);
        save();
    }

    public void deleteDirectory(String name) {
        String fullPath = currentDirectory.getFullPath().equals("/")
                ? "/" + name
                : currentDirectory.getFullPath() + "/" + name;
        deleteDirectoryByPath(fullPath, true);
        save();
    }

    public void renameDirectory(String oldName, String newName) {
        String oldFullPath = currentDirectory.getFullPath().equals("/")
                ? "/" + oldName
                : currentDirectory.getFullPath() + "/" + oldName;
        renameDirectoryByPath(oldFullPath, newName, true);
        save();
    }

    public void createFile(String name, String content) {
        String fullPath = currentDirectory.getFullPath().equals("/")
                ? "/" + name
                : currentDirectory.getFullPath() + "/" + name;
        createFileByPath(fullPath, content, true);
        save();
    }

    public void deleteFile(String name) {
        String fullPath = currentDirectory.getFullPath().equals("/")
                ? "/" + name
                : currentDirectory.getFullPath() + "/" + name;
        deleteFileByPath(fullPath, true);
        save();
    }

    public void renameFile(String oldName, String newName) {
        String oldFullPath = currentDirectory.getFullPath().equals("/")
                ? "/" + oldName
                : currentDirectory.getFullPath() + "/" + oldName;
        renameFileByPath(oldFullPath, newName, true);
        save();
    }

    public void copyFile(String fileName, Directory targetDir) {
        String origemFull = currentDirectory.getFullPath().equals("/")
                ? "/" + fileName
                : currentDirectory.getFullPath() + "/" + fileName;
        String destinoFull = targetDir.getFullPath();
        copyFileByPath(origemFull, destinoFull, true);
        save();
    }

    public void listContents() {
        for (String dirName : currentDirectory.getSubDirs().keySet()) {
            System.out.println("📂 " + dirName);
        }
        for (String fileName : currentDirectory.getFiles().keySet()) {
            System.out.println("📄 " + fileName);
        }
    }

    public void changeDirectory(String path) {
        if (path.equals("/")) {
            currentDirectory = root;
            return;
        }

        String[] parts = path.split("/");
        Directory temp = path.startsWith("/") ? root : currentDirectory;

        for (String part : parts) {
            if (part.isEmpty() || part.equals(".")) {
                continue;
            } else if (part.equals("..")) {
                if (temp.getParent() != null) {
                    temp = temp.getParent();
                }
            } else {
                Directory next = temp.getSubDirs().get(part);
                if (next != null) {
                    temp = next;
                } else {
                    System.out.println("Diretório não encontrado: " + part);
                    return;
                }
            }
        }

        currentDirectory = temp;
    }

    private void updateSubPaths(Directory dir) {
        for (Directory subDir : dir.getSubDirs().values()) {
            updateSubPaths(subDir);
        }
        for (MyFile file : dir.getFiles().values()) {
        }
    }

    private void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(diskFile))) {
            this.root = (Directory) ois.readObject();
            this.currentDirectory = root;
            System.out.println("Sistema de arquivos inicializado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o sistema de arquivos. Criando um novo");
            this.root = new Directory("meuso:", LocalDateTime.now(), null);
            this.currentDirectory = root;
        }
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(diskFile))) {
            oos.writeObject(root);
            System.out.println("Sistema de arquivos salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }
}
