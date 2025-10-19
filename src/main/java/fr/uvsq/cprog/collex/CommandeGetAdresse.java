package fr.uvsq.cprog.collex;

public class CommandeGetAdresse implements Commande {
  private final Dns dns;
  private final String nomMachineComplet;
  
  public CommandeGetAdresse(Dns dns, String nomMachineComplet) {
    this.dns = dns;
    this.nomMachineComplet = nomMachineComplet;
  }
  
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
