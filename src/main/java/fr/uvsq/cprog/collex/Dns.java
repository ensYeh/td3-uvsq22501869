package fr.uvsq.cprog.collex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Dns {
  private final List<DnsItem> items = new ArrayList<>();
  private final Path fichierBase;

  public Dns() throws IOException {
    Properties props = new Properties();
    try (var inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
      if (inputStream == null) {
        throw new IllegalArgumentException("Fichier config.properties introuvable dans le classpath");
      }
      props.load(inputStream);
    }
    String fichier = props.getProperty("dns.file");
    if (fichier == null || fichier.isBlank()) {
      throw new IllegalArgumentException("Le fichier de base doit être défini dans config.properties");
    }
    fichierBase = Path.of(fichier);
    chargerBase();
  }

  private void chargerBase() throws IOException {
    if (!Files.exists(fichierBase)) {
      Files.createFile(fichierBase);
      return;
    }
    List<String> lignes = Files.readAllLines(fichierBase);
    for (String ligne : lignes) {
      String[] es = ligne.strip().split(" ");
      if (es.length == 2) {
        AdresseIP ip = new AdresseIP(es[1]);
        NomMachine nom = new NomMachine(es[0]);
        items.add(new DnsItem(ip, nom));
      }
    }
  }

  public DnsItem getItem(AdresseIP ip) {
    return items.stream().filter(item -> item.getAdresseIP().equals(ip)).findFirst().orElse(null);
  }

  public DnsItem getItem(NomMachine nom) {
    return items.stream().filter(item -> item.getNomMachine().equals(nom)).findFirst().orElse(null);
  }

  public List<DnsItem> getItems(String domaine) {
    return getItems(domaine, false);
  }

  public List<DnsItem> getItems(String domaine, boolean trierParAdresse) {
    List<DnsItem> filtrés = items.stream().filter(item -> item.getNomMachine().getNomDomaine().equals(domaine))
        .collect(Collectors.toList());

    Comparator<DnsItem> comparateur;
    if (trierParAdresse) {
      comparateur = Comparator.comparing(DnsItem::getAdresseIP);
    } else {
      comparateur = Comparator.comparing(item -> item.getNomMachine().getNomMachine());
    }

    filtrés.sort(comparateur);
    return filtrés;
  }

  public void addItem(AdresseIP ip, NomMachine nom) throws IOException {
    if (getItem(ip) != null) {
      throw new IllegalArgumentException("ERREUR : L'adresse IP existe déjà !");
    }
    if (getItem(nom) != null) {
      throw new IllegalArgumentException("ERREUR : Le nom de machine existe déjà !");
    }
    DnsItem nouvelItem = new DnsItem(ip, nom);
    items.add(nouvelItem);
    majFichier();
  }

  private void majFichier() throws IOException {
    List<String> lignes = items.stream()
        .map(item -> item.getNomMachine().getNomComplet() + " " + item.getAdresseIP().getIp())
        .collect(Collectors.toList());
    Files.write(fichierBase, lignes, StandardOpenOption.TRUNCATE_EXISTING);
  }
}
