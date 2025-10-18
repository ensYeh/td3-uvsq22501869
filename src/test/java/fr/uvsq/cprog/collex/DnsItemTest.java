package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DnsItemTest {
  @Test
  public void testConstructeurValide() {
    AdresseIP ip = new AdresseIP("192.168.0.1");
    NomMachine nom = new NomMachine("machine.domaine.local");
    DnsItem item = new DnsItem(ip, nom);

    assertEquals(ip, item.getAdresseIP());
    assertEquals(nom, item.getNomMachine());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructeurAdresseIPNull() {
    new DnsItem(null, new NomMachine("machine.domaine.local"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructeurNomMachineNull() {
    new DnsItem(new AdresseIP("192.168.0.1"), null);
  }

  @Test
  public void testEqualsEtHashCode() {
    AdresseIP ip1 = new AdresseIP("10.0.0.1");
    NomMachine nom1 = new NomMachine("host1.domain.com");
    DnsItem item1 = new DnsItem(ip1, nom1);

    AdresseIP ip2 = new AdresseIP("10.0.0.1");
    NomMachine nom2 = new NomMachine("host1.domain.com");
    DnsItem item2 = new DnsItem(ip2, nom2);

    AdresseIP ip3 = new AdresseIP("10.0.0.2");
    NomMachine nom3 = new NomMachine("host2.domain.com");
    DnsItem item3 = new DnsItem(ip3, nom3);

    assertEquals(item1, item2);
    assertEquals(item1.hashCode(), item2.hashCode());

    assertNotEquals(item1, item3);
    assertNotEquals(item1.hashCode(), item3.hashCode());

    assertNotEquals(item2, item3);
  }

  @Test
  public void testEqualsAvecDifferentsObjects() {
    AdresseIP ip = new AdresseIP("1.1.1.1");
    NomMachine nom = new NomMachine("nom.domaine");
    DnsItem item = new DnsItem(ip, nom);

    assertFalse(item.equals(null));
    assertFalse(item.equals("une chaîne"));
  }

  @Test
  public void testToString() {
    AdresseIP ip = new AdresseIP("192.168.123.45");
    NomMachine nom = new NomMachine("host.domain.com");
    DnsItem item = new DnsItem(ip, nom);

    String attendu = "192.168.123.45 host.domain.com";
    assertEquals(attendu, item.toString());
  }
}
