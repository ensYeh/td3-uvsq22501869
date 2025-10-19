package fr.uvsq.cprog.collex;

import java.io.IOException;

public class CommandeAdd implements Commande {
  private final Dns dns;
  private final String adresseIp;
  private final String nomMachine;

  public CommandeAdd(Dns dns, String adresseIp, String nomMachine) {
    this.dns = dns;
    this.adresseIp = adresseIp;
    this.nomMachine = nomMachine;
  }

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
