package Arkanoid.Block;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

/**
 * Created by Andrei on 02.02.2018.
 */
public class BlockBlue<I> extends ImageObject<I> {

  public BlockBlue(Vertex corner) {
    super("./Block/block_blue.png", corner, new Vertex(0,0));
  }
}

