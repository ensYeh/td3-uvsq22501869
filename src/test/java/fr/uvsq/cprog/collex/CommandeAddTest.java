package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandeAddTest {
  private Path tempFile;
  private Dns dns;
  private final ByteArrayOutputStream sortieCapturée = new ByteArrayOutputStream();
  private final PrintStream sortieOriginale = System.out;

  @Before
  public void setUp() throws IOException {
    tempFile = Files.createTempFile("dns_test_add", ".txt");
    dns = new Dns(tempFile);
    System.setOut(new PrintStream(sortieCapturée));
  }

  @After
  public void tearDown() throws IOException {
    Files.deleteIfExists(tempFile);
    System.setOut(sortieOriginale);
  }

  @Test
  public void testAjoutOk() {
    CommandeAdd cmd = new CommandeAdd(dns, "10.123.45.67", "machine.ajout.fr");
    cmd.execute();

    String sortie = sortieCapturée.toString().trim();
    assertTrue(sortie.contains("Ajout réussi"));

    // Vérifier que l’item est accessible dans dns
    DnsItem item = dns.getItem(new AdresseIP("10.123.45.67"));
    assertTrue(item != null && item.getNomMachine().getNomComplet().equals("machine.ajout.fr"));
  }

  @Test
  public void testErreurAdresseExistante() throws IOException {
    dns.addItem(new AdresseIP("10.123.45.67"), new NomMachine("machine.conflit.fr"));

    CommandeAdd cmd = new CommandeAdd(dns, "10.123.45.67", "nouveau.machine.fr");
    cmd.execute();

    String sortie = sortieCapturée.toString().trim();
    assertTrue(sortie.contains("ERREUR"));
    assertTrue(sortie.contains("adresse IP existe déjà"));
  }

  @Test
  public void testErreurNomExistant() throws IOException {
    dns.addItem(new AdresseIP("10.111.22.33"), new NomMachine("machine.unique.fr"));

    CommandeAdd cmd = new CommandeAdd(dns, "10.222.33.44", "machine.unique.fr");
    cmd.execute();

    String sortie = sortieCapturée.toString().trim();
    assertTrue(sortie.contains("ERREUR"));
    assertTrue(sortie.contains("nom de machine existe déjà"));
  }
}
