package Arkanoid;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 04.02.2018.
 */
public class Block<I> extends ImageObject<I> {
  public Block(Vertex corner, String imagePath) {
    super(imagePath, corner, new Vertex(0,0));
  }
}