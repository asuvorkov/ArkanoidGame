package Arkanoid;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 03.02.2018.
 */
public class Ball<I> extends ImageObject<I> {
  public Ball(Vertex corner) {
    super("./ball.png", corner, new Vertex(0,0));
  }
}
