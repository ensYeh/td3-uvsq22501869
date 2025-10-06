package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NomMachineTest {
  @Test
  public void testConstructeurValide() {
    NomMachine nom = new NomMachine("www.uvsq.fr");
    assertEquals("www.uvsq.fr", nom.getNomComplet());
    assertEquals("www", nom.getNomMachine());
    assertEquals("uvsq.fr", nom.getNomDomaine());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructeurSansPoint() {
    new NomMachine("machineSansPoint");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructeurPointDebut() {
    new NomMachine(".domaineInvalide");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructeurPointFin() {
    new NomMachine("machineInvalide.");
  }
}
