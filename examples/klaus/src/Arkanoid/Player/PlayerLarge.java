package Arkanoid.Player;

import name.panitz.game.framework.FallingImage;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 02.02.2018.
 */
public class PlayerLarge<I> extends FallingImage<I> {
  public PlayerLarge(Vertex corner) {
    super("./Player/platform_l.png", corner, new Vertex(0,0));
  }
}
