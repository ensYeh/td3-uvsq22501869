package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AdresseIPTest {
  @Test
  public void testConstructeurValide() {
    AdresseIP ip = new AdresseIP("192.168.0.1");
    assertEquals("192.168.0.1", ip.getIp());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructeurInvalide() {
    new AdresseIP("999.999.999.999");
  }

  @Test
  public void testEstValide() {
    assertTrue(AdresseIP.estValide("0.0.0.0"));
    assertTrue(AdresseIP.estValide("255.255.255.255"));
    assertFalse(AdresseIP.estValide("256.0.0.1"));
    assertFalse(AdresseIP.estValide("192.168.1"));
    assertFalse(AdresseIP.estValide("192.168.1.abc"));
    assertFalse(AdresseIP.estValide(null));
    assertFalse(AdresseIP.estValide("192.168.0.256"));
    assertTrue(AdresseIP.estValide("10.0.0.255"));
  }

  @Test
  public void testEqualsEtHashCode() {
    AdresseIP ip1 = new AdresseIP("10.0.0.1");
    AdresseIP ip2 = new AdresseIP("10.0.0.1");
    AdresseIP ip3 = new AdresseIP("192.168.1.1");

    assertEquals(ip1, ip2);
    assertEquals(ip1.hashCode(), ip2.hashCode());
    assertNotEquals(ip1, ip3);
    assertNotEquals(ip1.hashCode(), ip3.hashCode());
  }
  
  @Test
  public void testCompareTo_equal() {
    AdresseIP ip1 = new AdresseIP("192.168.1.1");
    AdresseIP ip2 = new AdresseIP("192.168.1.1");
    assertEquals(0, ip1.compareTo(ip2));
  }

  @Test
  public void testCompareTo_lessThan() {
    AdresseIP ip1 = new AdresseIP("10.0.0.1");
    AdresseIP ip2 = new AdresseIP("10.0.0.2");
    assertTrue(ip1.compareTo(ip2) < 0);
  }

  @Test
  public void testCompareTo_greaterThan() {
    AdresseIP ip1 = new AdresseIP("192.168.1.5");
    AdresseIP ip2 = new AdresseIP("192.168.1.2");
    assertTrue(ip1.compareTo(ip2) > 0);
  }

  @Test
  public void testCompareTo_differentOctets() {
    AdresseIP ip1 = new AdresseIP("10.1.0.1");
    AdresseIP ip2 = new AdresseIP("10.2.0.1");
    assertTrue(ip1.compareTo(ip2) < 0);

    AdresseIP ip3 = new AdresseIP("11.0.0.1");
    AdresseIP ip4 = new AdresseIP("10.255.255.255");
    assertTrue(ip3.compareTo(ip4) > 0);
  }
}
