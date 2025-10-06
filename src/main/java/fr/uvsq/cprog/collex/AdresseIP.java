package fr.uvsq.cprog.collex;

import java.util.Objects;

public class AdresseIP {
  private final String ip;

  public AdresseIP(String ipDonnee) {
    if (!estValide(ipDonnee)) {
      throw new IllegalArgumentException("Adresse ip invalide : " + ipDonnee);
    }
    this.ip = ipDonnee;
  }

  public static boolean estValide(String ip) {
    if (ip == null) {
      return false;
    }

    String[] parties = ip.split("\\.");
    if (parties.length != 4) {
      return false;
    }

    for (String partie : parties) {
      try {
        int valeur = Integer.parseInt(partie);
        if (valeur < 0 || valeur > 255) {
          return false;
        }
      } catch (NumberFormatException e) {
        return false;
      }
    }

    return true;
  }

  public String getIp() {
    return ip;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AdresseIP)) {
      return false;
    }

    AdresseIP adresseIP = (AdresseIP) o;
    return Objects.equals(ip, adresseIP.ip);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ip);
  }

  @Override
  public String toString() {
    return ip;
  }

}
