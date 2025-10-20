package fr.uvsq.cprog.collex;

import java.io.IOException;

/**
 * Commande permettant d’ajouter une nouvelle entrée dans la base DNS.
 * <p>
 * Cette commande crée une association entre une adresse IP et un nom de machine
 * et la stocke dans la base de données gérée par l’objet {@code Dns}.
 * </p>
 */
public class CommandeAdd implements Commande {
  private final Dns dns;
  private final String adresseIp;
  private final String nomMachine;

  /**
   * Construit une nouvelle commande d’ajout DNS.
   *
   * @param dns        l’objet {@code Dns} à mettre à jour
   * @param adresseIp  l’adresse IP à ajouter
   * @param nomMachine le nom de machine correspondant
   */
  public CommandeAdd(Dns dns, String adresseIp, String nomMachine) {
    this.dns = dns;
    this.adresseIp = adresseIp;
    this.nomMachine = nomMachine;
  }

  /**
   * Exécute la commande d’ajout.
   * <p>
   * Vérifie la validité de l’adresse IP et du nom de machine, ajoute
   * l’association à la base DNS et affiche un message de confirmation. Si une
   * erreur survient (données invalides ou problème d’écriture), un message
   * d’erreur est affiché.
   * </p>
   */
  @Override
  public void execute() {
    try {
      dns.addItem(new AdresseIP(adresseIp), new NomMachine(nomMachine));
      System.out.println("Ajout réussi : " + adresseIp + " " + nomMachine);
    } catch (IllegalArgumentException | IOException e) {
      System.out.println("ERREUR : " + e.getMessage());
    }
  }
}
