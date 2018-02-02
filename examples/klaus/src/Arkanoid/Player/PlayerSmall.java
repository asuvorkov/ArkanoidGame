package Arkanoid.Player;

import name.panitz.game.framework.FallingImage;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 02.02.2018.
 */
public class PlayerSmall<I> extends FallingImage<I> {
  public PlayerSmall(Vertex corner) {
    super("./Player/platform_s.png", corner, new Vertex(0,0));
  }
}
