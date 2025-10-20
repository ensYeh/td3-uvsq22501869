package fr.uvsq.cprog.collex;

import java.util.Objects;

/**
 * Représente un enregistrement DNS associant une adresse IP à un nom de
 * machine.
 * <p>
 * Cette classe est immuable : une fois créée, l'association ne peut plus être
 * modifiée.
 * </p>
 */
public class DnsItem {
  private final AdresseIP adresseIp;
  private final NomMachine nomMachine;

  /**
   * Construit un nouvel enregistrement DNS à partir d’une adresse IP et d’un nom
   * de machine.
   *
   * @param adrIp   l’adresse IP à associer
   * @param nomMach le nom de machine correspondant
   * @throws IllegalArgumentException si l’un des deux paramètres est {@code null}
   */
  public DnsItem(AdresseIP adrIp, NomMachine nomMach) {
    if (adrIp == null || nomMach == null) {
      throw new IllegalArgumentException("AdresseIP et NomMachine ne peuvent pas être null");
    }
    this.adresseIp = adrIp;
    this.nomMachine = nomMach;
  }

  /**
   * Retourne l’adresse IP associée à cet enregistrement.
   *
   * @return l’objet {@code AdresseIP} correspondant
   */
  public AdresseIP getAdresseIP() {
    return adresseIp;
  }

  /**
   * Retourne le nom de machine associé à cet enregistrement.
   *
   * @return l’objet {@code NomMachine} correspondant
   */
  public NomMachine getNomMachine() {
    return nomMachine;
  }

  /**
   * Compare cet objet à un autre pour vérifier l’égalité. Deux objets
   * {@code DnsItem} sont égaux s’ils ont la même adresse IP et le même nom de
   * machine.
   *
   * @param o l’objet à comparer
   * @return {@code true} si les deux enregistrements sont identiques,
   *         {@code false} sinon
   */
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

  /**
   * Calcule le code de hachage basé sur l’adresse IP et le nom de machine.
   *
   * @return le code de hachage de cet enregistrement DNS
   */
  @Override
  public int hashCode() {
    return Objects.hash(adresseIp, nomMachine);
  }

  /**
   * Retourne une représentation textuelle de l’enregistrement DNS.
   * <p>
   * Le format est : {@code "adresseIP nomMachine"}.
   * </p>
   *
   * @return la chaîne représentant l’association IP-nom
   */
  @Override
  public String toString() {
    return adresseIp + " " + nomMachine;
  }
}
