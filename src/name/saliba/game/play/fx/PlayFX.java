package name.saliba.game.play.fx;

import name.panitz.game.framework.fx.GameApplication;
import name.saliba.game.SpaceInvaders.SpaceInvaders;

public class PlayFX extends GameApplication {
  public PlayFX(){
    super(new SpaceInvaders<>());
  }
  public static void main(String[] args) {
    PlayFX.launch();
  }
}
