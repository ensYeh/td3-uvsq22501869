package fr.uvsq.cprog.collex;

import java.util.Scanner;

public class DnsTUI {
  private final Scanner scanner;
  private final Dns dns;

  public DnsTUI(Dns dns) {
    this.dns = dns;
    this.scanner = new Scanner(System.in);
  }

  public DnsTUI(Dns dns, Scanner scanner) {
    this.dns = dns;
    this.scanner = scanner;
  }

  public Commande nextCommande() {
    System.out.print("> ");
    String ligne = scanner.nextLine().trim();

    if (ligne.isEmpty()) {
      return () -> System.out.println("Commande vide, veuillez réessayer.");
    }

    if (ligne.equalsIgnoreCase("quit")) {
      return new CommandeQuit();
    }

    String[] tokens = ligne.split("\\s+");

    // Commande "ls" avec possibles options
    if (tokens[0].equalsIgnoreCase("ls")) {
      boolean trierParAdresse = false;
      String domaine;

      if (tokens.length == 2) {
        domaine = tokens[1];
      } else if (tokens.length == 3 && tokens[1].equals("-a")) {
        trierParAdresse = true;
        domaine = tokens[2];
      } else {
        return () -> System.out.println("ERREUR : Commande 'ls' invalide. Usage: ls [-a] domaine");
      }
      return new CommandeList(dns, domaine, trierParAdresse);
    }

    // Commande "add" => add adresse.ip nom.qualifie.machine
    if (tokens[0].equalsIgnoreCase("add")) {
      if (tokens.length != 3) {
        return () -> System.out.println("ERREUR : Commande 'add' invalide. Usage: add adresse.ip nom.qualifie.machine");
      }
      return new CommandeAdd(dns, tokens[1], tokens[2]);
    }

    // Si le premier token semble être une IP (chiffres et points)
    if (ligne.matches("^\\d+(\\.\\d+){3}$")) {
      // Adresse IP → afficher nom machine
      return new CommandeGetNom(dns, ligne);
    }

    // Sinon on traite comme un nom qualifié → afficher adresse IP
    if (ligne.matches("^[a-zA-Z0-9_.-]+\\.[a-zA-Z0-9_.-]+$")) {
      return new CommandeGetAdresse(dns, ligne);
    }

    // Commande inconnue
    return () -> System.out.println("ERREUR : Commande non reconnue. Veuillez réessayer.");
  }

  public void affiche(String message) {
    System.out.println(message);
  }
}
