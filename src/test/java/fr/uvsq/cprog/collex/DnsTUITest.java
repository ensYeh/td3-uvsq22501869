package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DnsTUITest {
  private final ByteArrayOutputStream sortieCapturee = new ByteArrayOutputStream();
  private final PrintStream sortieOriginale = System.out;
  private Path tempFile;
  private Dns dns;

  @Before
  public void setUp() throws Exception {
    tempFile = Files.createTempFile("dns_test", ".txt");
    dns = new Dns(tempFile);

    System.setOut(new PrintStream(sortieCapturee));
  }

  @After
  public void tearDown() throws Exception {
    Files.deleteIfExists(tempFile);
    System.setOut(sortieOriginale);
  }

  private DnsTUI prepareDnsTUIAvecEntree(String input) {
    Scanner scannerSimulé = new Scanner(new ByteArrayInputStream(input.getBytes()));
    return new DnsTUI(dns, scannerSimulé);
  }

  @Test
  public void testCommandeQuit() {
    DnsTUI tui = prepareDnsTUIAvecEntree("quit\n");
    Commande cmd = tui.nextCommande();
    assertTrue(cmd instanceof CommandeQuit);
  }

  @Test
  public void testCommandeGetAdresseNomExiste() throws Exception {
    dns.addItem(new AdresseIP("10.0.0.1"), new NomMachine("machine.test.fr"));

    DnsTUI tui = prepareDnsTUIAvecEntree("machine.test.fr\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String sortie = sortieCmd.toString().trim();
    assertEquals("10.0.0.1", sortie);
  }

  @Test
  public void testCommandeGetAdresseNomInexistant() {
    DnsTUI tui = prepareDnsTUIAvecEntree("inexistant.fr\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String sortie = sortieCmd.toString().trim();
    assertTrue(sortie.contains("ERREUR"));
  }

  @Test
  public void testCommandeGetNomExiste() throws Exception {
    dns.addItem(new AdresseIP("10.0.0.2"), new NomMachine("machine2.test.fr"));

    DnsTUI tui = prepareDnsTUIAvecEntree("10.0.0.2\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String sortie = sortieCmd.toString().trim();
    assertEquals("machine2.test.fr", sortie);
  }

  @Test
  public void testCommandeGetNomInexistante() {
    DnsTUI tui = prepareDnsTUIAvecEntree("1.2.3.4\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String sortie = sortieCmd.toString().trim();
    assertTrue(sortie.contains("ERREUR"));
  }

  @Test
  public void testCommandeLsTrieParNom() throws Exception {
    dns.addItem(new AdresseIP("10.1.1.1"), new NomMachine("alpha.domaine.fr"));
    dns.addItem(new AdresseIP("10.1.1.2"), new NomMachine("bravo.domaine.fr"));

    DnsTUI tui = prepareDnsTUIAvecEntree("ls domaine.fr\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String[] lignes = sortieCmd.toString().trim().split(System.lineSeparator());
    assertEquals(2, lignes.length);
    assertEquals("10.1.1.1 alpha.domaine.fr", lignes[0]);
    assertEquals("10.1.1.2 bravo.domaine.fr", lignes[1]);
  }

  @Test
  public void testCommandeLsTrieParAdresse() throws Exception {
    dns.addItem(new AdresseIP("10.2.2.3"), new NomMachine("charlie.domaine.fr"));
    dns.addItem(new AdresseIP("10.2.2.1"), new NomMachine("delta.domaine.fr"));

    DnsTUI tui = prepareDnsTUIAvecEntree("ls -a domaine.fr\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String[] lignes = sortieCmd.toString().trim().split(System.lineSeparator());
    assertEquals(2, lignes.length);
    assertEquals("10.2.2.1 delta.domaine.fr", lignes[0]);
    assertEquals("10.2.2.3 charlie.domaine.fr", lignes[1]);
  }

  @Test
  public void testCommandeLsDomaineVide() {
    DnsTUI tui = prepareDnsTUIAvecEntree("ls domaine.inexistant\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String sortie = sortieCmd.toString().trim();
    assertTrue(sortie.contains("Pas de machines pour le domaine"));
  }

  @Test
  public void testCommandeAddAjoutEtErreur() throws Exception {
    DnsTUI tuiAjout = prepareDnsTUIAvecEntree("add 10.10.10.10 machine.add.fr\n");
    Commande cmdAjout = tuiAjout.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmdAjout.execute();
    String sortie1 = sortieCmd.toString().trim();
    assertTrue(sortie1.contains("Ajout réussi"));

    // Tenter de rajouter même adresse → erreur
    DnsTUI tuiErreur = prepareDnsTUIAvecEntree("add 10.10.10.10 autre.machine.fr\n");
    Commande cmdErreur = tuiErreur.nextCommande();

    sortieCmd.reset();
    cmdErreur.execute();
    String sortie2 = sortieCmd.toString().trim();
    assertTrue(sortie2.contains("ERREUR"));
  }

  @Test
  public void testCommandeInconnue() {
    DnsTUI tui = prepareDnsTUIAvecEntree("commandeInvalide\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String sortie = sortieCmd.toString().trim();
    assertTrue(sortie.contains("ERREUR"));
  }

  @Test
  public void testCommandeLsOptionErronee() {
    DnsTUI tui = prepareDnsTUIAvecEntree("ls -wrong domaine.fr\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String sortie = sortieCmd.toString().trim();
    assertTrue(sortie.contains("ERREUR"));
  }

  @Test
  public void testCommandeAddSyntaxeIncorrecte() {
    DnsTUI tui = prepareDnsTUIAvecEntree("add 10.10.10.10\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String sortie = sortieCmd.toString().trim();
    assertTrue(sortie.contains("ERREUR"));
  }

  @Test
  public void testCommandeLsSyntaxeIncorrecte() {
    DnsTUI tui = prepareDnsTUIAvecEntree("ls -a -b domaine.fr\n");
    Commande cmd = tui.nextCommande();

    ByteArrayOutputStream sortieCmd = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sortieCmd));

    cmd.execute();
    String sortie = sortieCmd.toString().trim();
    assertTrue(sortie.contains("ERREUR"));
  }
}
