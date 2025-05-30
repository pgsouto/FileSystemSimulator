package com.pedroelarissa.projetoso;
import java.io.*;
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
            this.root = new Directory("meuso:", LocalDateTime.now(), null);
            this.currentDirectory = root;
            save();
        }
    }

    public void createDirectory(String name){
        if(currentDirectory.getSubDirs().containsKey(name)) {
            System.out.println("Operação impedida: Diretório já existe");
            return;
        }
        Directory newDir = new Directory(name, /*currentDirectory.getFullPath() + "/" + name,*/ LocalDateTime.now(), currentDirectory);
        currentDirectory.getSubDirs().put(name, newDir);
        save();
    }

    public void deleteDirectory(String name){
        if(currentDirectory.getSubDirs().containsKey(name)){
            currentDirectory.getSubDirs().remove(name);
            save();
        } else{
            System.out.println("Operação impedida: Diretório não encontrado");
        }
    }

    public void renameDirectory(String oldName, String newName){
        Directory renamedDir = currentDirectory.getSubDirs().get(oldName);
        if(renamedDir != null){
            renamedDir.setName(newName);
            currentDirectory.getSubDirs().remove(oldName);
            currentDirectory.getSubDirs().put(newName, renamedDir);
            save();
        } else {
            System.out.println("Operação impedida: diretório não encontrado");
        }
    }

    public void createFile(String name, String content){
        if(currentDirectory.getFiles().containsKey(name)){
            System.out.println("Operação impedida: arquivo já existe");
            return;
        }
        MyFile newFile = new MyFile(name, /*currentDirectory.getFullPath() + "/" + name,*/ LocalDateTime.now(), content, currentDirectory);
        currentDirectory.getFiles().put(name, newFile);
        save();
    }

    public void deleteFile(String name){
        if (currentDirectory.getFiles().containsKey(name)) {
            currentDirectory.getFiles().remove(name);
            save();
        } else {
            System.out.println("Operação impedida: arquivo não encontrado");
        }
    }

    public void renameFile(String oldname, String newName){
        MyFile renamedFile = currentDirectory.getFiles().get(oldname);
        if(renamedFile != null){
            renamedFile.setName(newName);
            currentDirectory.getFiles().remove(oldname);
            currentDirectory.getFiles().put(newName, renamedFile);
            save();
        }else{
            System.out.println("Operação impedida: arquivo não encontrado");
        }
    }

    public void copyFile(String fileName, Directory targetDir) {
        MyFile file = currentDirectory.getFiles().get(fileName);
        if(file != null){
            MyFile copiedFile = new MyFile(file.getName(), /*targetDir.getFullPath() +"/"+ file.getName(),*/ LocalDateTime.now(), file.getContent(), targetDir);
            targetDir.getFiles().put(file.getName(), copiedFile);
            save();

        } else {
            System.out.println("Operação impedida: arquivo não encontrado");
        }

    }

    public void listContents() {
        for (String dirName : currentDirectory.getSubDirs().keySet()) {
            System.out.println("📂 " + dirName);
        }

        for (String fileName : currentDirectory.getFiles().keySet()) {
            System.out.println("📄 " + fileName);
        }
    }

    public void changeDirectory(String name) {
        if (name.equals("..")) {
            if (currentDirectory.getParent() != null) {
                currentDirectory = currentDirectory.getParent();
            }
        } else if (currentDirectory.getSubDirs().containsKey(name)) {
            currentDirectory = currentDirectory.getSubDirs().get(name);
        } else {
            System.out.println("Diretório não encontrado.");
        }
    }

    private void updateSubPaths(Directory dir) {
        for (Directory subDir : dir.getSubDirs().values()) {
            /*subDir.setFullPath(dir.getFullPath() + "/" + subDir.getName());*/
            updateSubPaths(subDir);
        }
        for (MyFile file : dir.getFiles().values()) {
            /*file.setFullPath(dir.getFullPath() + "/" + file.getName());*/
        }
    }


    private void load(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(diskFile))){
            this.root = (Directory) ois.readObject();
            this.currentDirectory = root;
            System.out.println("Sistema de arquivos inicializado com sucesso!");

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Erro ao carregar o sistema de arquivos. Criando um novo");
            this.root = new Directory("meuso:",/* "/", */LocalDateTime.now(), null);
            this.currentDirectory = root;

        }
    }

    private void save(){
        try(ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(diskFile))) {
            oos.writeObject(root);
            System.out.println("Sistema de arquivos salvo com sucesso!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }
}
