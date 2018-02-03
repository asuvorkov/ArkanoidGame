package Arkanoid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import Arkanoid.Block.*;
import Arkanoid.Player.PlayerMedium;
import name.panitz.game.framework.*;

/**
 * Created by Andrei on 02.02.2018.
 */
public class Arkanoid<I, S> extends AbstractGame<I, S> {
  private List<BlockBlue<I>> blockBlues = new ArrayList<>();
  private List<BlockGreen<I>> blockGreens = new ArrayList<>();
  private List<BlockPurple<I>> blockPurples = new ArrayList<>();
  private List<BlockRed<I>> blockReds = new ArrayList<>();
  private List<BlockYellow<I>> blockYellows = new ArrayList<>();

  private PlayerMedium<I> playerMedium;
  private List<Ball<I>> balls = new ArrayList<>();
  private int lives = 4;

  SoundObject<S> badBonusSound = new SoundObject<>("./Sounds/bad_bonus.wav");
  SoundObject<S> goodBonusSound = new SoundObject<>("./Sounds/good_bonus.wav");
  SoundObject<S> blockBreakSound = new SoundObject<>("./Sounds/block_break.wav");
  SoundObject<S> platformLengthChangeSound = new SoundObject<>("./Sounds/platform_length_change.wav");
  SoundObject<S> startGameSound = new SoundObject<>("./Sounds/start_game.wav");
  SoundObject<S> looseSound = new SoundObject<>("./Sounds/lost.wav");
  SoundObject<S> winSound = new SoundObject<>("./Sounds/won.wav");

  private List<Background<I>> background = new ArrayList<>();
  private static final int GRID_WIDTH = 50;
  private static final int GRID_HEIGHT = 25;
  private String level1 = "                         \n"
                        + "                         \n"
                        + "                         \n"
                        + " bbbbbbbbbbbbbbbbbbbbbbb \n"
                        + " ggggggggggggggggggggggg \n"
                        + " ppppppppppppppppppppppp \n"
                        + " rrrrrrrrrrrrrrrrrrrrrrr \n"
                        + " yyyyyyyyyyyyyyyyyyyyyyy ";

  public Arkanoid() {
    super(new PlayerMedium<I>(new Vertex(600,750)), 1250, 800);
    playerMedium = (PlayerMedium<I>) getPlayer();
    balls.add(new Ball<I> (new Vertex(getPlayer().getPos().x + 50, getPlayer().getPos().y - 15)));
    balls.get(0).setVelocity(new Vertex(1, -1));
    //background.add(new Background<I>(new Vertex(0, 0)));

    BufferedReader r = new BufferedReader(new StringReader(level1));
    int l = 0;
    try {
      for (String line=r.readLine(); line != null; line=r.readLine()){
        int col = 0;
        for (char c : line.toCharArray()) {
          switch (c) {
            case 'b':
              blockBlues.add(new BlockBlue<>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT)));
              break;
            case 'g':
              blockGreens.add(new BlockGreen<>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT)));
              break;
            case 'p':
              blockPurples.add(new BlockPurple<>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT)));
              break;
            case 'r':
              blockReds.add(new BlockRed<>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT)));
              break;
            case 'y':
              blockYellows.add(new BlockYellow<>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT)));
              break;
          }
          col++;
        }
        l++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    getGOss().add(balls);
    getGOss().add(background);

    getGOss().add(blockBlues);
    getGOss().add(blockGreens);
    getGOss().add(blockPurples);
    getGOss().add(blockReds);
    getGOss().add(blockYellows);

    getButtons().add(new Button("PAUSE", this::pause));
    getButtons().add(new Button("START", this::start));
  }

  @Override
  public void paintTo(GraphicsTool<I> g) {
    super.paintTo(g);
    g.drawString(5, 20, "Lives: " + lives);
    g.setColor(0.9, 0.9, 0);
  }

  @Override
  public void doChecks() {

  }

  @Override
  public void keyPressedReaction(KeyCode keycode) {
    if (keycode != null) {
      switch (keycode) {
        case LEFT_ARROW:
          playerMedium.left();
          break;
        case RIGHT_ARROW:
          playerMedium.right();
          break;
        default:
          ;
      }
    }
  }

  @Override
  public void keyReleasedReaction(KeyCode keycode) {
    if (keycode != null) {
      switch (keycode) {
        case LEFT_ARROW:
          playerMedium.stop();
          break;
        case RIGHT_ARROW:
          playerMedium.stop();
          break;
        default:
          ;
      }
    }
  }
}
