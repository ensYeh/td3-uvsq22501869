package fr.uvsq.cprog.collex;

import java.util.Objects;

public class NomMachine {
  private final String nomComplet;
  private final String nomMachine;
  private final String nomDomaine;

  public NomMachine(String nomComplet) {
    if (!estValide(nomComplet)) {
      throw new IllegalArgumentException("Nom complet invalide : " + nomComplet);
    }

    this.nomComplet = nomComplet;
    int index = nomComplet.indexOf('.');
    this.nomMachine = nomComplet.substring(0, index);
    this.nomDomaine = nomComplet.substring(index + 1);
  }

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

  public String getNomComplet() {
    return nomComplet;
  }

  public String getNomMachine() {
    return nomMachine;
  }

  public String getNomDomaine() {
    return nomDomaine;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(nomComplet);
  }

  @Override
  public String toString() {
    return nomComplet;
  }
}
