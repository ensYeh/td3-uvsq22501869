package fr.uvsq.cprog.collex;

public class DnsApp {
  private final Dns dns;
  private final DnsTUI tui;

  public DnsApp(Dns dns) {
    this.dns = dns;
    this.tui = new DnsTUI(dns);
  }

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
