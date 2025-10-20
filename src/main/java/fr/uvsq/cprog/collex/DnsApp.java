package fr.uvsq.cprog.collex;

/**
 * Classe principale de l’application DNS en mode console.
 * <p>
 * Elle initialise le service DNS et l’interface utilisateur textuelle, puis
 * exécute la boucle principale pour traiter les commandes saisies par
 * l’utilisateur.
 * </p>
 */
public class DnsApp {
  private final Dns dns;
  private final DnsTUI tui;

  /**
   * Construit l’application DNS avec un service {@code Dns} donné.
   * <p>
   * Initialise également l’interface utilisateur textuelle.
   * </p>
   *
   * @param dns le service {@code Dns} à utiliser
   */
  public DnsApp(Dns dns) {
    this.dns = dns;
    this.tui = new DnsTUI(dns);
  }

  /**
   * Démarre la boucle principale de l’application.
   * <p>
   * Lit les commandes saisies par l’utilisateur via la TUI et les exécute. La
   * boucle continue indéfiniment jusqu’à l’exécution d’une commande
   * {@code CommandeQuit}.
   * </p>
   */
  public void run() {
    while (true) {
      Commande cmd = tui.nextCommande();
      cmd.execute();
      // Quitter si CommandeQuit (optionnel, car CommandeQuit fait System.exit)
      if (cmd instanceof CommandeQuit) {
        break;
      }
    }
  }

  /**
   * Point d’entrée de l’application.
   * <p>
   * Initialise le service DNS en chargeant le fichier {@code config.properties}
   * et le fichier de base DNS, puis démarre l’application. En cas d’erreur, un
   * message est affiché et la trace de l’exception est imprimée.
   * </p>
   *
   * @param args les arguments de ligne de commande (non utilisés)
   */
  public static void main(String[] args) {
    try {
      Dns dns = new Dns(); // Charge config.properties et fichier DNS
      DnsApp app = new DnsApp(dns);
      app.run();
    } catch (Exception e) {
      System.err.println("Erreur lors du lancement de l'application : " + e.getMessage());
      e.printStackTrace();
    }
  }
}
