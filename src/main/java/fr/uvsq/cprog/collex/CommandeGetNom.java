package fr.uvsq.cprog.collex;

/**
 * Commande permettant de récupérer le nom complet d’une machine à partir d’une
 * adresse IP.
 * <p>
 * Cette commande interroge la base DNS pour trouver l’entrée correspondant à
 * l’adresse IP fournie et affiche le nom de machine complet associé. Si
 * l’adresse IP n’existe pas dans la base, un message d’erreur est affiché.
 * </p>
 */
public class CommandeGetNom implements Commande {
  private final Dns dns;
  private final String adresseIp;

  /**
   * Construit une nouvelle commande pour récupérer le nom complet d’une machine à
   * partir d’une adresse IP.
   *
   * @param dns       l’objet {@code Dns} sur lequel effectuer la recherche
   * @param adresseIp l’adresse IP de la machine recherchée
   */
  public CommandeGetNom(Dns dns, String adresseIp) {
    this.dns = dns;
    this.adresseIp = adresseIp;
  }

  /**
   * Exécute la commande de récupération du nom de machine.
   * <p>
   * Si l’entrée correspondant à l’adresse IP est trouvée, le nom complet de la
   * machine est affiché. Sinon, un message d’erreur est imprimé pour indiquer que
   * l’adresse IP n’a pas été trouvée.
   * </p>
   */
  @Override
  public void execute() {
    DnsItem item = dns.getItem(new AdresseIP(adresseIp));
    if (item == null) {
      System.out.println("ERREUR : Adresse IP introuvable " + adresseIp);
    } else {
      System.out.println(item.getNomMachine().getNomComplet());
    }
  }

}
