package Arkanoid.Block;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 02.02.2018.
 */
public class BlockGreen<I> extends ImageObject<I> {

  public BlockGreen(Vertex corner) {
    super("./Block/block_green.png", corner, new Vertex(0,0));
  }
}
