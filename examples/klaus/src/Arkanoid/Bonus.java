package Arkanoid;

import name.panitz.game.framework.FallingImage;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 04.02.2018.
 */
public class Bonus<I> extends FallingImage<I> {
  public Bonus(Vertex corner, String imagePath) {
    super(imagePath, corner, new Vertex(0,7));
  }
}