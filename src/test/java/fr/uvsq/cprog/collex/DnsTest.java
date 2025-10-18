package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DnsTest {
  private static final Path TEMP_FILE = Path.of("dns_test_base.txt");
  private Dns dns;

  @Before
  public void setup() throws IOException {
    // Crée/écrase le fichier temporaire de base DNS dans le répertoire courant
    List<String> lignes = List.of("ecampus.uvsq.fr 193.51.25.12", "poste.uvsq.fr 193.51.31.154",
        "www.uvsq.fr 193.51.31.90");
    Files.write(TEMP_FILE, lignes);

    // Instancie la classe Dns, elle lira config.properties depuis le classpath
    dns = new Dns();
  }

  @After
  public void cleanup() throws IOException {
    Files.deleteIfExists(TEMP_FILE);
  }

  @Test
  public void testConstructeur_chargeBaseCorrectement() {
    assertNotNull(dns);
    assertEquals("193.51.31.154", dns.getItem(new NomMachine("poste.uvsq.fr")).getAdresseIP().getIp());
    assertEquals("poste.uvsq.fr", dns.getItem(new AdresseIP("193.51.31.154")).getNomMachine().getNomComplet());
  }

  @Test
  public void testGetItem_ipExistante() {
    AdresseIP ip = new AdresseIP("193.51.31.90");
    DnsItem item = dns.getItem(ip);
    assertNotNull(item);
    assertEquals("www.uvsq.fr", item.getNomMachine().getNomComplet());
  }

  @Test
  public void testGetItem_nomMachineExistant() {
    NomMachine nom = new NomMachine("ecampus.uvsq.fr");
    DnsItem item = dns.getItem(nom);
    assertNotNull(item);
    assertEquals("193.51.25.12", item.getAdresseIP().getIp());
  }

  @Test
  public void testGetItem_ipNonExistante() {
    AdresseIP ip = new AdresseIP("8.8.8.8");
    assertNull(dns.getItem(ip));
  }

  @Test
  public void testGetItem_nomMachineNonExistant() {
    NomMachine nom = new NomMachine("inexistant.site.fr");
    assertNull(dns.getItem(nom));
  }

  @Test
  public void testGetItems_listeMachinesParNom() {
    List<DnsItem> items = dns.getItems("uvsq.fr");
    assertEquals(3, items.size());
    assertEquals("ecampus", items.get(0).getNomMachine().getNomMachine());
    assertEquals("poste", items.get(1).getNomMachine().getNomMachine());
    assertEquals("www", items.get(2).getNomMachine().getNomMachine());
  }

  @Test
  public void testGetItems_listeMachinesParAdresse() {
    List<DnsItem> items = dns.getItems("uvsq.fr", true);
    assertEquals(3, items.size());
    assertEquals("193.51.25.12", items.get(0).getAdresseIP().getIp());
    assertEquals("193.51.31.90", items.get(1).getAdresseIP().getIp());
    assertEquals("193.51.31.154", items.get(2).getAdresseIP().getIp());
  }

  @Test
  public void testGetItems_domaineSansEntree() {
    List<DnsItem> items = dns.getItems("domaine.inexistant");
    assertTrue(items.isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddItem_exceptionSiAdresseExistante() throws IOException {
    dns.addItem(new AdresseIP("193.51.31.90"), new NomMachine("nouvelle.uvsq.fr"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddItem_exceptionSiNomExistante() throws IOException {
    dns.addItem(new AdresseIP("10.0.0.1"), new NomMachine("poste.uvsq.fr"));
  }

  @Test
  public void testAddItem_ajouteCorrectement() throws IOException {
    AdresseIP nouvelleIp = new AdresseIP("10.0.0.1");
    NomMachine nouveauNom = new NomMachine("nouveau.domaine.fr");
    dns.addItem(nouvelleIp, nouveauNom);

    DnsItem itemIp = dns.getItem(nouvelleIp);
    DnsItem itemNom = dns.getItem(nouveauNom);

    assertNotNull(itemIp);
    assertNotNull(itemNom);
    assertEquals(itemIp, itemNom);

    List<String> lignes = Files.readAllLines(TEMP_FILE);
    boolean presentDansFichier = lignes.stream()
        .anyMatch(s -> s.contains(nouveauNom.getNomComplet()) && s.contains(nouvelleIp.getIp()));
    assertTrue(presentDansFichier);
  }

  @Test
  public void testMajFichier_contientToutesLesEntreesApresAjout() throws IOException {
    AdresseIP ipTest = new AdresseIP("10.0.0.99");
    NomMachine nomTest = new NomMachine("test.maison.fr");
    dns.addItem(ipTest, nomTest);

    List<String> lignes = Files.readAllLines(TEMP_FILE);

    assertTrue(lignes.stream().anyMatch(l -> l.contains(ipTest.getIp()) && l.contains(nomTest.getNomComplet())));
  }
}
