package fr.uvsq.cprog.collex;

import java.util.Scanner;

/**
 * Interface utilisateur en mode texte (TUI) pour interagir avec le service DNS.
 * <p>
 * Cette classe lit les commandes saisies par l’utilisateur, les interprète et
 * renvoie l’objet {@code Commande} correspondant. Elle permet également
 * d’afficher des messages à l’écran.
 * </p>
 */
public class DnsTUI {
  private final Scanner scanner;
  private final Dns dns;

  /**
   * Construit une interface utilisateur textuelle en utilisant un {@code Dns}
   * donné.
   * <p>
   * Le scanner de saisie est créé automatiquement à partir de l’entrée standard.
   * </p>
   *
   * @param dns l’objet DNS sur lequel s’appuient les commandes utilisateur
   */
  public DnsTUI(Dns dns) {
    this.dns = dns;
    this.scanner = new Scanner(System.in);
  }

  /**
   * Construit une interface utilisateur textuelle en utilisant un {@code Dns} et
   * un {@code Scanner} personnalisés.
   *
   * @param dns     l’objet DNS à manipuler
   * @param scanner le scanner utilisé pour lire les entrées utilisateur
   */
  public DnsTUI(Dns dns, Scanner scanner) {
    this.dns = dns;
    this.scanner = scanner;
  }

  /**
   * Lit la prochaine commande saisie par l’utilisateur et retourne l’objet
   * {@code Commande} correspondant.
   * <p>
   * Les commandes reconnues sont :
   * <ul>
   * <li><b>ls [-a] domaine</b> — liste les machines d’un domaine (option
   * <code>-a</code> pour trier par adresse IP)</li>
   * <li><b>add adresse.ip nom.qualifie.machine</b> — ajoute une nouvelle entrée
   * DNS</li>
   * <li><b>quit</b> — quitte le programme</li>
   * <li><b>adresse.ip</b> — affiche le nom de machine associé</li>
   * <li><b>nom.qualifie.machine</b> — affiche l’adresse IP associée</li>
   * </ul>
   * Si la commande est invalide, une commande d’erreur est retournée.
   * </p>
   *
   * @return un objet {@code Commande} à exécuter
   */
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

  /**
   * Affiche un message à l’écran.
   *
   * @param message le texte à afficher
   */
  public void affiche(String message) {
    System.out.println(message);
  }
}
