package fr.uvsq.cprog.collex;

import java.util.Objects;

/**
 * Représente une adresse IPv4 et permet de la comparer à d'autres adresses IP.
 * <p>
 * Cette classe est immuable : une fois créée, l'adresse IP ne peut pas être
 * modifiée. Elle vérifie la validité de l'adresse donnée (quatre octets entre 0
 * et 255).
 * </p>
 */
public class AdresseIP implements Comparable<AdresseIP> {
  private final String ip;

  /**
   * Construit une nouvelle instance d'AdresseIP à partir d'une chaîne donnée.
   *
   * @param ipDonnee l'adresse IP à utiliser (au format "x.x.x.x")
   * @throws IllegalArgumentException si l'adresse IP n'est pas valide
   */
  public AdresseIP(String ipDonnee) {
    if (!estValide(ipDonnee)) {
      throw new IllegalArgumentException("Adresse ip invalide : " + ipDonnee);
    }
    this.ip = ipDonnee;
  }

  /**
   * Vérifie si une chaîne représente une adresse IPv4 valide.
   *
   * @param ip la chaîne à vérifier
   * @return {@code true} si l'adresse est valide, {@code false} sinon
   */
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

  /**
   * Retourne la représentation textuelle de l'adresse IP.
   *
   * @return la chaîne représentant l'adresse IP
   */
  public String getIp() {
    return ip;
  }

  /**
   * Compare cette adresse IP à un autre objet pour vérifier l'égalité.
   *
   * @param o l'objet à comparer
   * @return {@code true} si les deux adresses IP sont identiques, {@code false}
   *         sinon
   */
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

  /**
   * Calcule le code de hachage de cette adresse IP.
   *
   * @return la valeur de hachage correspondant à l'adresse IP
   */
  @Override
  public int hashCode() {
    return Objects.hash(ip);
  }

  /**
   * Retourne la représentation textuelle de cette adresse IP.
   *
   * @return l'adresse IP sous forme de chaîne
   */
  @Override
  public String toString() {
    return ip;
  }

  /**
   * Compare cette adresse IP à une autre pour définir un ordre naturel.
   * <p>
   * La comparaison s'effectue octet par octet (du premier au dernier).
   * </p>
   *
   * @param o l'autre adresse IP à comparer
   * @return un nombre négatif si cette adresse est inférieure à l'autre, un
   *         nombre positif si elle est supérieure, ou 0 si elles sont égales
   */
  @Override
  public int compareTo(AdresseIP o) {
    String[] thisParts = this.ip.split("\\.");
    String[] otherParts = o.ip.split("\\.");

    for (int i = 0; i < 4; i++) {
      int thisOctet = Integer.parseInt(thisParts[i]);
      int otherOctet = Integer.parseInt(otherParts[i]);
      int cmp = Integer.compare(thisOctet, otherOctet);
      if (cmp != 0) {
        return cmp;
      }
    }
    return 0;
  }

}
