package fr.uvsq.cprog.collex;

import java.util.List;

public class CommandeList implements Commande {

  private final Dns dns;
  private final String domaine;
  private final boolean trierParAdresse;

  public CommandeList(Dns dns, String domaine, boolean trierParAdresse) {
    this.dns = dns;
    this.domaine = domaine;
    this.trierParAdresse = trierParAdresse;
  }

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
