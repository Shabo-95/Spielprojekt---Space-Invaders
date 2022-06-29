package name.saliba.game.play.swing;

import name.panitz.game.framework.swing.SwingGame;
import name.saliba.game.SpaceInvaders.SpaceInvaders;

public class PlaySwing{
  public static void main(String[] args){
    SwingGame.startGame(new SpaceInvaders<>());
  }
}

