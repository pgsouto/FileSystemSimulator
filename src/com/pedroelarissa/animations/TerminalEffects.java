package com.pedroelarissa.animations;

public class TerminalEffects {

    /**
     * Efeito de digitação (Typewriter).
     * @param message Mensagem a ser exibida.
     * @param delayMs Tempo em milissegundos entre cada caractere.
     */
    public static void typewriter(String message, int delayMs) {
        for (char c : message.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Animação interrompida.");
                break;
            }
        }
        System.out.println();
    }
}
