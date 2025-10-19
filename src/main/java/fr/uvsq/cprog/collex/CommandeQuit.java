package fr.uvsq.cprog.collex;

public class CommandeQuit implements Commande{
  public static class ExitException extends RuntimeException {}
  
  @Override
  public void execute() {
    System.out.println("Fin du programme.");
    //throw new ExitException(); // Uniquement pour faire le test.
    System.exit(0);
  }
}
