package com.pedroelarissa.projetoso;

import com.pedroelarissa.animations.TerminalEffects;

import java.util.Scanner;

public class FileSystemTerminal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileSystemSimulator fs = new FileSystemSimulator("disk.dat");

        TerminalEffects.typewriter("Incializando o sistema...",65);

        TerminalEffects.typewriter("Bem vindo ao nosso sistema de arquivos!",65);
        TerminalEffects.typewriter("Integrantes contribuintes:", 65);
        TerminalEffects.typewriter("Larissa e Pedro", 65);

        while (true) {
            System.out.print(fs.getCurrentDirectory().getFullPath() + " > ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";

            switch (command) {
                case "mkdir":
                    fs.createDirectory(argument);
                    break;
                case "rmdir":
                    fs.deleteDirectory(argument);
                    break;
                case "renameDir":
                    String[] dirArgs = argument.split(" ");
                    if (dirArgs.length == 2) {
                        fs.renameDirectory(dirArgs[0], dirArgs[1]);
                    } else {
                        System.out.println("Uso correto: renameDir nomeAntigo nomeNovo");
                    }
                    break;
                case "cd":
                    fs.changeDirectory(argument);
                    break;
                case "ls":
                    fs.listContents();
                    break;
                case "touch":
                    String[] fileArgs = argument.split(" ", 2);
                    if (fileArgs.length == 2) {
                        fs.createFile(fileArgs[0], fileArgs[1]);
                    } else {
                        System.out.println("Uso correto: touch nomeArquivo conteúdo");
                    }
                    break;
                case "rm":
                    fs.deleteFile(argument);
                    break;
                case "rename":
                    String[] renameArgs = argument.split(" ");
                    if (renameArgs.length == 2) {
                        fs.renameFile(renameArgs[0], renameArgs[1]);
                    } else {
                        System.out.println("Uso correto: rename nomeAntigo nomeNovo");
                    }
                    break;
                case "copy":
                    String[] copyArgs = argument.split(" ");
                    if (copyArgs.length == 2) {
                        String fileName = copyArgs[0];
                        String targetDirName = copyArgs[1];

                        if (fs.getCurrentDirectory().getSubDirs().containsKey(targetDirName)) {
                            fs.copyFile(fileName, fs.getCurrentDirectory().getSubDirs().get(targetDirName));
                        } else {
                            System.out.println("Diretório alvo não encontrado.");
                        }
                    } else {
                        System.out.println("Uso correto: copy nomeArquivo nomeDiretorioAlvo");
                    }
                    break;
                case "exit":
                    System.out.println("💾 Saindo e salvando...");
                    scanner.close();
                    return;
                    case "help":
                    System.out.println(
                        "Comandos disponíveis:\n" +
                        "mkdir nomeDiretorio       - Criar diretório\n" +
                        "rmdir nomeDiretorio       - Remover diretório\n" +
                        "renameDir ant novo        - Renomear diretório\n" +
                        "cd nomeDiretorio ou ..    - Entrar em diretório ou voltar\n" +
                        "ls                        - Listar conteúdo\n" +
                        "touch nomeArquivo conteudo- Criar arquivo\n" +
                        "rm nomeArquivo            - Remover arquivo\n" +
                        "rename ant novo           - Renomear arquivo\n" +
                        "copy arquivo dir          - Copiar arquivo para diretório\n" +
                        "help                      - Mostrar ajuda\n" +
                        "exit                      - Sair"
                    );
                    break;
                default:
                    System.out.println("Comando não reconhecido. Digite 'help' para ajuda.");
            }
        }
    }
}
