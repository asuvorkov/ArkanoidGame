package Arkanoid.Block;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 02.02.2018.
 */
public class BlockPurple<I> extends ImageObject<I> {

  public BlockPurple(Vertex corner) {
    super("./Block/block_purple.png", corner, new Vertex(0,0));
  }
}
