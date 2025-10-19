package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandeGetNomTest {
  private final ByteArrayOutputStream sortieCapturee = new ByteArrayOutputStream();
  private final PrintStream sortieOriginale = System.out;

  @Before
  public void setUp() {
    System.setOut(new PrintStream(sortieCapturee));
  }

  @After
  public void tearDown() {
    System.setOut(sortieOriginale);
  }

  @Test
  public void testAfficheNomQuandExistant() throws Exception {
    Dns dns = new Dns();
    dns.addItem(new AdresseIP("10.123.45.67"), new NomMachine("autre-machine.test.fr"));

    CommandeGetNom cmd = new CommandeGetNom(dns, "10.123.45.67");
    cmd.execute();

    String sortie = sortieCapturee.toString().trim();
    assertEquals("autre-machine.test.fr", sortie);
  }

  @Test
  public void testAfficheMessageErreurQuandInexistant() throws Exception {
    Dns dns = new Dns();
    
    CommandeGetNom cmd = new CommandeGetNom(dns, "10.10.10.10");
    cmd.execute();

    String sortie = sortieCapturee.toString().trim();
    assertEquals("ERREUR : Adresse IP introuvable 10.10.10.10", sortie);
  }
}
