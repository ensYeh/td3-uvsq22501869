package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandeGetAdresseTest {
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
  public void testAfficheAdresseQuandExistant() throws Exception {
    Dns dns = new Dns();
    
    dns.addItem(new AdresseIP("192.168.0.1"), new NomMachine("machine.test.fr"));
    
    CommandeGetAdresse cmd = new CommandeGetAdresse(dns, "machine.test.fr");
    cmd.execute();
    
    String sortie = sortieCapturee.toString().trim();
    assertEquals("192.168.0.1", sortie);
  }
  
  @Test
  public void testAfficheMessageErreurQuandInexistant() throws Exception {
    Dns dns = new Dns();
    CommandeGetAdresse cmd = new CommandeGetAdresse(dns, "inexistant.fr");
    cmd.execute();

    String sortie = sortieCapturee.toString().trim();
    assertEquals("ERREUR : Machine introuvable pour le nom inexistant.fr", sortie);
  }
}
