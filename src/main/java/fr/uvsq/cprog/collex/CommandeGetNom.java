package fr.uvsq.cprog.collex;

public class CommandeGetNom implements Commande {
  private final Dns dns;
  private final String adresseIp;
  
  public CommandeGetNom(Dns dns, String adresseIp) {
    this.dns = dns;
    this.adresseIp = adresseIp;
  }
  
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
