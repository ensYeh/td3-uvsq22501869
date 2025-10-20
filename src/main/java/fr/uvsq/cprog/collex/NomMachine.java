package fr.uvsq.cprog.collex;

import java.util.Objects;

/**
 * Représente un nom de machine complet, composé d’un nom d’hôte et d’un nom de
 * domaine.
 * <p>
 * Par exemple : {@code "serveur.exemple.com"} où "serveur" est le nom de la
 * machine et "exemple.com" le domaine. Cette classe est immuable : ses champs
 * ne peuvent pas être modifiés après création.
 * </p>
 */
public class NomMachine {
  private final String nomComplet;
  private final String nomMachine;
  private final String nomDomaine;

  /**
   * Construit une instance de {@code NomMachine} à partir d’un nom complet.
   *
   * @param nomComplet le nom complet (incluant le domaine)
   * @throws IllegalArgumentException si le nom complet n’est pas valide
   */
  public NomMachine(String nomComplet) {
    if (!estValide(nomComplet)) {
      throw new IllegalArgumentException("Nom complet invalide : " + nomComplet);
    }

    this.nomComplet = nomComplet;
    int index = nomComplet.indexOf('.');
    this.nomMachine = nomComplet.substring(0, index);
    this.nomDomaine = nomComplet.substring(index + 1);
  }

  /**
   * Vérifie si le nom de machine fourni est valide.
   * <p>
   * Un nom valide doit contenir au moins un point ('.') séparant le nom d’hôte et
   * le domaine, et ne pas commencer ni se terminer par un point.
   * </p>
   *
   * @param nom le nom à vérifier
   * @return {@code true} si le nom est valide, {@code false} sinon
   */
  public static boolean estValide(String nom) {
    if (nom == null || nom.isBlank()) {
      return false;
    }

    int index = nom.indexOf('.');
    if (index <= 0 || index == nom.length() - 1) {
      return false;
    }

    return true;
  }

  /**
   * Retourne le nom complet de la machine (incluant le domaine).
   *
   * @return le nom complet de la machine
   */
  public String getNomComplet() {
    return nomComplet;
  }

  /**
   * Retourne uniquement le nom de la machine (avant le premier point).
   *
   * @return le nom d’hôte de la machine
   */
  public String getNomMachine() {
    return nomMachine;
  }

  /**
   * Retourne le nom de domaine associé à la machine (après le premier point).
   *
   * @return le nom de domaine
   */
  public String getNomDomaine() {
    return nomDomaine;
  }

  /**
   * Compare cet objet avec un autre pour vérifier l’égalité. Deux objets
   * {@code NomMachine} sont égaux s’ils ont le même nom complet.
   *
   * @param o l’objet à comparer
   * @return {@code true} si les deux noms sont identiques, {@code false} sinon
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof NomMachine)) {
      return false;
    }

    NomMachine machine = (NomMachine) o;
    return Objects.equals(nomComplet, machine.nomComplet);
  }

  /**
   * Calcule le code de hachage basé sur le nom complet de la machine.
   *
   * @return le code de hachage correspondant au nom complet
   */
  @Override
  public int hashCode() {
    return Objects.hash(nomComplet);
  }

  /**
   * Retourne la représentation textuelle du nom complet de la machine.
   *
   * @return le nom complet sous forme de chaîne
   */
  @Override
  public String toString() {
    return nomComplet;
  }
}
