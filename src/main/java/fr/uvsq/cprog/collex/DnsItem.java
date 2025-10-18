package fr.uvsq.cprog.collex;

import java.util.Objects;

public class DnsItem {
  private final AdresseIP adresseIp;
  private final NomMachine nomMachine;
  
  public DnsItem(AdresseIP adrIp, NomMachine nomMach) {
    if (adrIp == null || nomMach == null) {
      throw new IllegalArgumentException("AdresseIP et NomMachine ne peuvent pas être null");
    }
    this.adresseIp = adrIp;
    this.nomMachine = nomMach;
  }
  
  public AdresseIP getAdresseIP() {
    return adresseIp;
  }
  
  public NomMachine getNomMachine() {
    return nomMachine;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    
    if (!(o instanceof DnsItem)) {
      return false;
    }
    
    DnsItem dnsItem = (DnsItem) o;
    return Objects.equals(adresseIp, dnsItem.adresseIp) && Objects.equals(nomMachine, dnsItem.nomMachine);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(adresseIp, nomMachine);
  }
  
  @Override
  public String toString() {
    return adresseIp + " " + nomMachine;
  }
}
