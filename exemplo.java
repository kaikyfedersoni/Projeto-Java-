import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;

public class Exemplo {
  private static long proximoId = 1L;
    public static void main(String[] args) {
        String nomeArquivo = "arquivo.txt";
        Scanner scanner = new Scanner(System.in);
        int num;

        List<Contato> contatos = new ArrayList<>();

        do {
            menu();
            num = scanner.nextInt();

            switch (num) {
                case 1:
                    try {
                        lerArquivo(nomeArquivo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    adicionarContato(scanner, contatos,nomeArquivo);
                    break;
                case 3:
                System.out.print("Digite o ID do contato que deseja remover: ");
                long idContatoRemover = scanner.nextLong();
                removerContato(contatos, nomeArquivo, idContatoRemover);
                break;

                    
                case 4:
                editarContato(scanner, contatos,nomeArquivo);
                break;
              case 5:
                System.out.println("Programa Encerrado!");
                break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        } while (num != 5);
        scanner.close();
    }
    private static void menu() {
        System.out.println("1. Ler arquivo");
        System.out.println("2. Adicionar contato");
        System.out.println("3. Remover contato");
        System.out.println("4. Editar contato");
      System.out.println("5. Sair");
    }
  private static void escreverEmArquivo(String conteudo, String nomeArquivo) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, true))) {
          writer.write(conteudo);
          writer.newLine(); 
          System.out.println("Informações escritas no arquivo com sucesso!");
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
    private static void lerArquivo(String nomeArquivo) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                System.out.println(linha);
            }
        }
    }

    private static void adicionarContato(Scanner scanner, List<Contato> contatos, String nomeArquivo) {
        long id = proximoId;
        scanner.nextLine();

        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o sobrenome: ");
        String sobreNome = scanner.nextLine();
        Contato contato = new Contato(id, nome, sobreNome);
        contatos.add(contato);
      System.out.print("Digite o DDD do telefone: ");
      String ddd = scanner.next();

      System.out.print("Digite o número de telefone: ");
      long numero = scanner.nextLong();

      while (contato.numeroJaCadastrado(numero)) {
          System.out.println("Número de telefone já cadastrado. Por favor, insira outro número:");
          numero = scanner.nextLong();
      }

      Telefone telefone = new Telefone();
      telefone.setDdd(ddd);
      telefone.setNumero(numero);

      contato.adicionarTelefone(telefone);
        System.out.println("Contato adicionado com sucesso!");
      String conteudo = contato.getId() + " " + contato.getNome() + " " + contato.getSobreNome() + " " +
              telefone.getDdd() + " " + telefone.getNumero();
      escreverEmArquivo(conteudo, nomeArquivo);
      proximoId++;
      listarContatos(contatos);
    }
  private static void removerContato(List<Contato> contatos, String nomeArquivo, long idContato) {
      boolean contatoRemovido = false;
    Iterator<Contato> iterator = contatos.iterator();
    while (iterator.hasNext()) {
        Contato contato = iterator.next();
        if (contato.getId().equals(idContato)) {
            iterator.remove();
            contatoRemovido = true;
        }
    }


      if (contatoRemovido) {
          System.out.println("Contato removido com sucesso!");
          atualizarArquivo(contatos, nomeArquivo);
      
      } else {
          System.out.println("Contato não encontrado.");
      }
  }

  private static void atualizarArquivo(List<Contato> contatos, String nomeArquivo) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
          for (Contato contato : contatos) {
              writer.write(contato.getId() + " " + contato.getNome() + " " + contato.getSobreNome());
              for (Telefone telefone : contato.getTelefones()) {
                  writer.write(" " + telefone.getDdd() + " " + telefone.getNumero());
              }
              writer.newLine(); 
          }
          System.out.println("Arquivo atualizado com sucesso!");
        
      } catch (IOException e) {
          System.out.println("Erro ao atualizar o arquivo: " + e.getMessage());
      }
  }
  private static void listarContatos(List<Contato> contatos) {
      if (contatos.isEmpty()) {
          System.out.println("Lista de contatos vazia.");
        proximoId=1;
      } 
  }
  private static void editarContato(Scanner scanner, List<Contato> contatos,String nomeArquivo) {
      System.out.print("Digite o ID do contato que deseja editar: ");
      long idContato = scanner.nextLong();
      scanner.nextLine(); 

      boolean contatoEncontrado = false;
      for (Contato contato : contatos) {
          if (contato.getId() == idContato) {
              System.out.println("Contato encontrado:");
            
              System.out.println("O que você deseja modificar?");
              System.out.println("1. Nome");
              System.out.println("2. Sobrenome");
              System.out.println("3. DDD");
              System.out.println("4. Número de telefone");

              int opcao = scanner.nextInt(); 
              scanner.nextLine(); 
              switch (opcao) {
                  case 1:
                      System.out.print("Novo nome: ");
                      String novoNome = scanner.nextLine();
                      contato.setNome(novoNome);
                      break;
                  case 2:
                      System.out.print("Novo sobrenome: ");
                      String novoSobrenome = scanner.nextLine();
                      contato.setSobreNome(novoSobrenome);
                      break;
                  case 3:
                      System.out.print("Novo DDD: ");
                      String novoDDD = scanner.nextLine();
                      contato.getTelefones().get(0).setDdd(novoDDD);
                      break;
                  case 4:
                      System.out.print("Novo número de telefone: ");
                      long novoNumero = scanner.nextLong();
                      scanner.nextLine(); 
                      contato.getTelefones().get(0).setNumero(novoNumero); 
                      break;
                  default:
                      System.out.println("Opção inválida.");
                      break;
              }

              contatoEncontrado = true;
              System.out.println("Contato editado com sucesso:");
            atualizarArquivo(contatos, nomeArquivo);
break;
          }
          }
      
      if (!contatoEncontrado) {
          System.out.println("Contato não encontrado.");
        
      
      }
  }
  }


    


