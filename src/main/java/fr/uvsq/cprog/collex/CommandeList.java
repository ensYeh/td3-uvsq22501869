package fr.uvsq.cprog.collex;

import java.util.List;

/**
 * Commande permettant de lister les machines d’un domaine donné dans la base
 * DNS.
 * <p>
 * La liste peut être triée soit par nom de machine, soit par adresse IP, selon
 * l’option spécifiée lors de la création de la commande. Si aucune machine
 * n’existe pour le domaine, un message approprié est affiché.
 * </p>
 */
public class CommandeList implements Commande {

  private final Dns dns;
  private final String domaine;
  private final boolean trierParAdresse;

  /**
   * Construit une commande de liste des machines pour un domaine donné.
   *
   * @param dns             l’objet {@code Dns} sur lequel effectuer la recherche
   * @param domaine         le domaine dont on souhaite lister les machines
   * @param trierParAdresse {@code true} pour trier par adresse IP, {@code false}
   *                        pour trier par nom
   */
  public CommandeList(Dns dns, String domaine, boolean trierParAdresse) {
    this.dns = dns;
    this.domaine = domaine;
    this.trierParAdresse = trierParAdresse;
  }

  /**
   * Exécute la commande de liste des machines.
   * <p>
   * Affiche toutes les machines du domaine demandé avec leur adresse IP. Si
   * aucune machine n’est trouvée, affiche un message indiquant l’absence
   * d’enregistrements pour ce domaine.
   * </p>
   */
  @Override
  public void execute() {
    List<DnsItem> items = dns.getItems(domaine, trierParAdresse);
    if (items.isEmpty()) {
      System.out.println("Pas de machines pour le domaine " + domaine);
      return;
    }

    for (DnsItem item : items) {
      System.out.println(item.getAdresseIP().getIp() + " " + item.getNomMachine().getNomComplet());
    }

  }

}
