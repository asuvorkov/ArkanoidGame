package Arkanoid;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 02.02.2018.
 */
class Background<I> extends ImageObject<I> {

  Background(Vertex corner) {
    super("background.png", corner, new Vertex(0,0));
  }
}
