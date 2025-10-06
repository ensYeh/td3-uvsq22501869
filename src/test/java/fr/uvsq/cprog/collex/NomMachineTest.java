package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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

  @Test
  public void testEstValide() {
    assertTrue(NomMachine.estValide("nom.machine"));
    assertTrue(NomMachine.estValide("www.uvsq.fr"));
    assertFalse(NomMachine.estValide(null));
    assertFalse(NomMachine.estValide(""));
    assertFalse(NomMachine.estValide("sanspoint"));
    assertFalse(NomMachine.estValide(".debut"));
    assertFalse(NomMachine.estValide("fin."));
  }

  @Test
  public void testEqualsEtHashCode() {
    NomMachine nom1 = new NomMachine("serveur.domaine.com");
    NomMachine nom2 = new NomMachine("serveur.domaine.com");
    NomMachine nom3 = new NomMachine("autre.domaine.com");

    assertEquals(nom1, nom2);
    assertEquals(nom1.hashCode(), nom2.hashCode());
    assertNotEquals(nom1, nom3);
    assertNotEquals(nom1.hashCode(), nom3.hashCode());
  }

  @Test
  public void testToString() {
    NomMachine nom = new NomMachine("machine.domaine");
    assertEquals("machine.domaine", nom.toString());
  }
}
