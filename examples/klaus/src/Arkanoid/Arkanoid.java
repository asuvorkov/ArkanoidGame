package Arkanoid;

import java.util.ArrayList;
import java.util.List;

import Arkanoid.Block.BlockBlue;
import Arkanoid.Block.BlockGreen;
import Arkanoid.Block.BlockPurple;
import Arkanoid.Player.PlayerMedium;
import name.panitz.game.framework.*;

/**
 * Created by Andrei on 02.02.2018.
 */
public class Arkanoid<I, S> extends AbstractGame<I, S> {
  List<BlockBlue<I>> blockBlues = new ArrayList<>();
  List<BlockGreen<I>> blockGreens = new ArrayList<>();
  List<BlockPurple<I>> blockPurples = new ArrayList<>();
  int lives = 4;
  PlayerMedium<I> playerMedium;

  public Arkanoid() {
    super(new PlayerMedium<I>(new Vertex(350,700)), 800, 800);
    playerMedium = (PlayerMedium<I>) getPlayer();
    getButtons().add(new Button("PAUSE", this::pause));
    getButtons().add(new Button("START", this::start));
  }

  @Override
  public void paintTo(GraphicsTool<I> g) {
    super.paintTo(g);
    g.drawString(50, 20, "Lives: " + lives);
  }

  SoundObject<S> crash = new SoundObject<S>("crash.wav");

  @Override
  public void doChecks() {

  }

  @Override
  public void keyPressedReaction(KeyCode keycode) {
    if (keycode != null)
      switch (keycode) {
        case LEFT_ARROW:
          playerMedium.left();
          break;
        case RIGHT_ARROW:
          playerMedium.right();
          break;
        case VK_SPACE:
          //TODO implement ball release
          break;
        default:;
      }
  }

  @Override
  public void keyReleasedReaction(KeyCode keycode) {
    if (keycode != null)
      switch (keycode) {
        case LEFT_ARROW:
          playerMedium.stop();
          break;
        case RIGHT_ARROW:
          playerMedium.stop();
          break;
        default:;
      }
  }
}
