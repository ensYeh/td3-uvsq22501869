package fr.uvsq.cprog.collex;

/**
 * Commande permettant de récupérer l’adresse IP associée à un nom de machine
 * complet.
 * <p>
 * Cette commande interroge la base DNS pour trouver l’entrée correspondant au
 * nom de machine fourni et affiche l’adresse IP correspondante. Si la machine
 * n’existe pas dans la base, un message d’erreur est affiché.
 * </p>
 */
public class CommandeGetAdresse implements Commande {
  private final Dns dns;
  private final String nomMachineComplet;

  /**
   * Construit une nouvelle commande pour récupérer l’adresse IP d’un nom de
   * machine donné.
   *
   * @param dns               l’objet {@code Dns} sur lequel effectuer la
   *                          recherche
   * @param nomMachineComplet le nom complet de la machine recherchée
   */
  public CommandeGetAdresse(Dns dns, String nomMachineComplet) {
    this.dns = dns;
    this.nomMachineComplet = nomMachineComplet;
  }

  /**
   * Exécute la commande de récupération d’adresse IP.
   * <p>
   * Si l’entrée est trouvée, l’adresse IP est affichée. Sinon, un message
   * d’erreur est imprimé pour indiquer que la machine n’a pas été trouvée.
   * </p>
   */
  @Override
  public void execute() {
    DnsItem item = dns.getItem(new NomMachine(nomMachineComplet));
    if (item == null) {
      System.out.println("ERREUR : Machine introuvable pour le nom " + nomMachineComplet);
    } else {
      System.out.println(item.getAdresseIP().getIp());
    }
  }

}
