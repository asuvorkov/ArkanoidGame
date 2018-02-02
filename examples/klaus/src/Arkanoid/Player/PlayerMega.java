package Arkanoid.Player;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 02.02.2018.
 */
public class PlayerMega<I> extends ImageObject<I> {
  public PlayerMega(Vertex corner) {
    super("./Player/platform_mega.png", corner, new Vertex(0,0));
  }
}
