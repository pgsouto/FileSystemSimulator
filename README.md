# Simulador de Sistema de Arquivos com Journaling em Java
## 👥 Integrantes
- **Pedro Guilherme**
- **Larissa Antonia**


## 📄 Introdução

Este projeto tem como objetivo simular o funcionamento de um **Sistema de Arquivos com Journaling**, implementado na linguagem Java. O sistema é capaz de realizar operações básicas de manipulação de arquivos e diretórios, além de registrar essas operações por meio de um mecanismo de journaling.

O projeto busca fornecer uma compreensão prática dos conceitos envolvidos no gerenciamento de arquivos em sistemas operacionais.

---

## 🧠 Metodologia

O simulador foi desenvolvido em Java, utilizando chamadas de métodos com parâmetros específicos que simulam operações típicas de um sistema de arquivos

Cada operação executada é refletida na tela e registrada no journal, simulando o comportamento de sistemas de arquivos reais que utilizam journaling para garantir consistência e recuperação em caso de falhas

---

## 🗂️ Parte 1: Sistema de Arquivos com Journaling

### 🔸 O que é um Sistema de Arquivos?

Um sistema de arquivos é responsável por gerenciar como os dados são armazenados e recuperados em dispositivos de armazenamento. Ele organiza os dados em arquivos e diretórios, controlando acesso, permissões e integridade.

### 🔸 Importância do Journaling

O **Journaling** é uma técnica utilizada para garantir a integridade dos dados em casos de falhas, como quedas de energia ou travamentos. Ele registra todas as operações que modificam o sistema de arquivos em um log (journal) antes que sejam efetivamente aplicadas.

### 🔸 Tipos de Journaling

- **Write-Ahead Logging:** As operações são registradas no journal antes de serem aplicadas
- **Log-Structured File System:** O sistema inteiro é tratado como um log contínuo, otimizando escrita sequencial
- **Metadata Journaling:** Apenas alterações nos metadados (como nomes e permissões) são registradas

---

## 🏗️ Parte 2: Arquitetura do Simulador

### 🔹 Estrutura do projeto
**📝 Observação:** a estrutura principal do projeto está np package `/src/pedroelarissa/projetoso`

O simulador utiliza classes Java para representar:
- **Entry:** Classe abstrata na qual MyFile e Directory herdam em seus atributos e comportamentos em comum
- **MyFile:** Classe de arquivos no sistema
- **Directory:** Diretórios que podem conter arquivos e outros diretórios
- **FileSystemSimulator:** Classe principal que gerencia todo o sistema de arquivos.Ela permitindo criar, deletar, renomear e copiar diretórios e arquivos além de implementar Journal para garantir a integridade dos dados
- **FileSystemTerminal:** Classe responsável pela interface de terminal para o sistema de arquivos simulado 
- **Journal:** Responsável por registrar todas as operações realizadas

### 🔹 Utilitários/Bibliotecas utilizados
#### Nativos
- **java.io.*:** Usado na manipulação dos arquivos para ler e gravar o estado do sistema de arquivos em `disk.dat` simulando persistência e isolamento dos arquivos e diretórios
- **Serializable:** Implementado pela classe abstrata Entry para garantir salvamento dos objetos em disco
#### Customizados
- **TerminalEffects:** implementa animações de digitação pra maior personalização do projeto

### 🔹 Estruturas de Dados
- **List:** Implementado em FileSystemSimulator, serve para armazenar e gerenciar os arquivos e diretórios dentro de outro diretório, facilitando navegação e exibição do sistema de arquivos.
- **Map:** Implementado por Directoy, ele está sendo usado para armazenar arquivos e subdiretórios associados ao nome (chave), porque facilita buscas e operações por nome. Um diretório guarda seus subdiretórios em Map<String, Directory> subDirs e arquivos em Map<String, MyFile> files, onde as chaves são os nomes
### 🔹 Implementação do Journaling

O journaling é implementado através de uma classe dedicada chamada `Journal` que atua como um arquivo de log persistente, onde são registradas (jornalizadas) operações ou eventos importantes do sistema de arquivos simulado. Ele funciona como uma espécie de diário para registrar ações, que podem ser consultadas depois para recuperação ou auditoria.
O arquivo de journal é um arquivo de texto simples, onde cada entrada é uma linha representando uma operação ou evento.

Operações realizadas por Journal:

- Criação do arquivo de journal caso ele não exista
- **appendEntry(String entry):** Adiciona uma nova linha (entrada) ao final do arquivo, registrando uma nova ação/evento
- **loadEntries():** Lê todas as linhas do arquivo e retorna uma lista com todas as entradas já registradas
- **clear():** Apaga todo o conteúdo do arquivo, limpando o journal

O log registra o tipo de operação, os objetos envolvidos e o timestamp.

---

## 💻 Parte 3: Implementação em Java

- **FileSystemSimulator:** Implementa as funcionalidades do sistema de arquivos, com métodos como `createFile()`, `deleteFile()`, `createDirectory()`, entre outros.

- **FileSystemTerminal:** Implementa interface interativa para utilização do sistema de arquivos ao rodar projeto. Contém a função `main()`

- **Entry:** Classe abstrata que é pai de MyFile e Directory, fazendo que esses herdem atributos e métodos em comum de importância pro sistema como `getFullPath()`

- **File e Directory:** Classes que modelam arquivos e diretórios, contendo atributos como nome, caminho e conteúdo.

- **Journal:** Gerencia o log das operações, permitindo visualização do histórico e simulação de recuperação em caso de falhas.

---

## 🚀 Parte 4: Instalação e Funcionamento

### 🔧 Requisitos

- Java JDK 8 ou superior
- IDE de preferência: IntelliJ

### 📥 Instalação

1. Clone o repositório:

```bash
git clone https://github.com/pgsouto/FileSystemSimulator
