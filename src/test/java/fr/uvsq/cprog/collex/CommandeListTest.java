package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandeListTest {
  private final ByteArrayOutputStream sortieCapturée = new ByteArrayOutputStream();
  private final PrintStream sortieOriginale = System.out;

  @Before
  public void setUp() {
    System.setOut(new PrintStream(sortieCapturée));
  }

  @After
  public void tearDown() {
    System.setOut(sortieOriginale);
  }

  @Test
  public void testAfficheListeTrieeParNom() throws Exception {
    Path tempFile = Files.createTempFile("dns_test", ".txt");
    Dns dns = new Dns(tempFile);

    dns.addItem(new AdresseIP("10.10.10.10"), new NomMachine("alpha.mondomaine.fr"));
    dns.addItem(new AdresseIP("10.10.10.11"), new NomMachine("bravo.mondomaine.fr"));
    dns.addItem(new AdresseIP("10.10.10.12"), new NomMachine("charlie.mondomaine.fr"));

    CommandeList cmd = new CommandeList(dns, "mondomaine.fr", false);
    cmd.execute();

    String[] lignes = sortieCapturée.toString().trim().split(System.lineSeparator());
    assertEquals(3, lignes.length);
    assertEquals("10.10.10.10 alpha.mondomaine.fr", lignes[0]);
    assertEquals("10.10.10.11 bravo.mondomaine.fr", lignes[1]);
    assertEquals("10.10.10.12 charlie.mondomaine.fr", lignes[2]);

    Files.deleteIfExists(tempFile);
  }

  @Test
  public void testAfficheListeTrieeParAdresse() throws Exception {
    Dns dns = new Dns();
    dns.addItem(new AdresseIP("10.20.30.5"), new NomMachine("delta.mondomaine.fr"));
    dns.addItem(new AdresseIP("10.20.30.3"), new NomMachine("echo.mondomaine.fr"));
    dns.addItem(new AdresseIP("10.20.30.4"), new NomMachine("foxtrot.mondomaine.fr"));

    CommandeList cmd = new CommandeList(dns, "mondomaine.fr", true);
    cmd.execute();

    String[] lignes = sortieCapturée.toString().trim().split(System.lineSeparator());
    assertEquals(3, lignes.length);
    // Tri par adresse IP : IP 10.20.30.3, 10.20.30.4, 10.20.30.5
    assertEquals("10.20.30.3 echo.mondomaine.fr", lignes[0]);
    assertEquals("10.20.30.4 foxtrot.mondomaine.fr", lignes[1]);
    assertEquals("10.20.30.5 delta.mondomaine.fr", lignes[2]);
  }

  @Test
  public void testAfficheMessageDomaineSansMachine() throws Exception {
    Dns dns = new Dns(); // vide, aucune entrée

    CommandeList cmd = new CommandeList(dns, "indomaine.inexistant", false);
    cmd.execute();

    String sortie = sortieCapturée.toString().trim();
    assertEquals("Pas de machines pour le domaine indomaine.inexistant", sortie);
  }

  @Test
  public void testExecute_afficheListeVideSiPasDeMachines() throws Exception {
    Dns dns = new Dns();
    // Ajoute une machine sur un autre domaine
    dns.addItem(new AdresseIP("10.1.1.1"), new NomMachine("uniq.mondiff.fr"));

    CommandeList cmd = new CommandeList(dns, "mondomaine.vide", true);
    cmd.execute();

    String sortie = sortieCapturée.toString().trim();
    assertEquals("Pas de machines pour le domaine mondomaine.vide", sortie);
  }
}
