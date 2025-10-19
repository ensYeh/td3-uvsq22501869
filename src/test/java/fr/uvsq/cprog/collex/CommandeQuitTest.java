package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class CommandeQuitTest {
  @Test
  public void testFinProgramme() {
    CommandeQuit cmd = new CommandeQuit();
    assertThrows(CommandeQuit.ExitException.class, cmd::execute);
  }
}
