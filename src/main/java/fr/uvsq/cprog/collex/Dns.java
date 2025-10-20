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

/**
 * Représente un service DNS capable de gérer une base d’associations entre des
 * noms de machines et leurs adresses IP.
 * <p>
 * Cette classe permet de charger, consulter, ajouter et trier des
 * enregistrements DNS à partir d’un fichier de base. Les données sont
 * persistées dans un fichier texte défini dans un fichier de configuration
 * {@code config.properties}.
 * </p>
 */
public class Dns {
  private final List<DnsItem> items = new ArrayList<>();
  private final Path fichierBase;

  /**
   * Construit une instance de {@code Dns} en utilisant le fichier défini dans le
   * fichier {@code config.properties} du classpath.
   *
   * @throws IOException              si une erreur d’entrée/sortie se produit
   *                                  lors du chargement du fichier de
   *                                  configuration ou de la base DNS
   * @throws IllegalArgumentException si le fichier {@code config.properties} est
   *                                  introuvable ou mal configuré
   */
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

  /**
   * Construit une instance de {@code Dns} à partir d’un fichier de base
   * spécifique.
   *
   * @param fichierBase le chemin vers le fichier contenant la base DNS
   * @throws IOException si une erreur d’entrée/sortie se produit lors du
   *                     chargement de la base
   */
  public Dns(Path fichierBase) throws IOException {
    this.fichierBase = fichierBase;
    items.clear();
    chargerBase();
  }

  /**
   * Charge les enregistrements DNS à partir du fichier de base.
   * <p>
   * Chaque ligne du fichier doit contenir un nom de machine et une adresse IP
   * séparés par un espace. Si le fichier n’existe pas encore, il est créé vide.
   * </p>
   *
   * @throws IOException si une erreur d’entrée/sortie se produit pendant la
   *                     lecture ou la création du fichier
   */
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

  /**
   * Recherche un enregistrement DNS correspondant à une adresse IP donnée.
   *
   * @param ip l’adresse IP à rechercher
   * @return l’objet {@code DnsItem} correspondant, ou {@code null} si aucun
   *         enregistrement n’est trouvé
   */
  public DnsItem getItem(AdresseIP ip) {
    return items.stream().filter(item -> item.getAdresseIP().equals(ip)).findFirst().orElse(null);
  }

  /**
   * Recherche un enregistrement DNS correspondant à un nom de machine donné.
   *
   * @param nom le nom de machine à rechercher
   * @return l’objet {@code DnsItem} correspondant, ou {@code null} si aucun
   *         enregistrement n’est trouvé
   */
  public DnsItem getItem(NomMachine nom) {
    return items.stream().filter(item -> item.getNomMachine().equals(nom)).findFirst().orElse(null);
  }

  /**
   * Retourne la liste des enregistrements DNS correspondant à un domaine donné.
   * <p>
   * Les résultats ne sont pas triés par défaut.
   * </p>
   *
   * @param domaine le nom de domaine recherché
   * @return la liste des éléments {@code DnsItem} appartenant à ce domaine
   */
  public List<DnsItem> getItems(String domaine) {
    return getItems(domaine, false);
  }

  /**
   * Retourne la liste des enregistrements DNS correspondant à un domaine donné,
   * avec une option de tri.
   *
   * @param domaine         le nom de domaine recherché
   * @param trierParAdresse {@code true} pour trier les résultats par adresse IP,
   *                        {@code false} pour trier par nom de machine
   * @return la liste triée des éléments {@code DnsItem} appartenant à ce domaine
   */
  public List<DnsItem> getItems(String domaine, boolean trierParAdresse) {
    List<DnsItem> filtres = items.stream().filter(item -> item.getNomMachine().getNomDomaine().equals(domaine))
        .collect(Collectors.toList());

    Comparator<DnsItem> comparateur;
    if (trierParAdresse) {
      comparateur = Comparator.comparing(DnsItem::getAdresseIP);
    } else {
      comparateur = Comparator.comparing(item -> item.getNomMachine().getNomMachine());
    }

    filtres.sort(comparateur);
    return filtres;
  }

  /**
   * Ajoute un nouvel enregistrement DNS dans la base.
   * <p>
   * Vérifie que l’adresse IP et le nom de machine ne sont pas déjà présents avant
   * l’ajout, puis met à jour le fichier de base.
   * </p>
   *
   * @param ip  l’adresse IP à associer
   * @param nom le nom de machine correspondant
   * @throws IOException              si une erreur d’entrée/sortie se produit
   *                                  lors de la mise à jour du fichier
   * @throws IllegalArgumentException si l’adresse IP ou le nom existent déjà dans
   *                                  la base
   */
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

  /**
   * Met à jour le fichier de base pour refléter le contenu actuel de la liste
   * {@code items}.
   * <p>
   * Le fichier est entièrement réécrit à chaque appel avec la liste des
   * enregistrements au format texte : {@code nom_complet adresse_ip}.
   * </p>
   *
   * @throws IOException si une erreur d’écriture se produit lors de la mise à
   *                     jour du fichier
   */
  private void majFichier() throws IOException {
    List<String> lignes = items.stream()
        .map(item -> item.getNomMachine().getNomComplet() + " " + item.getAdresseIP().getIp())
        .collect(Collectors.toList());
    Files.write(fichierBase, lignes, StandardOpenOption.TRUNCATE_EXISTING);
  }
}
