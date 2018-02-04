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
  private static final int VIEWPORT_WIDTH = 1250;
  private static final int VIEWPORT_HEIGHT = 800;
  private static final Vertex PLAYER_START = new Vertex(600,750);
  private static final Vertex BALL_VELOCITY_START = new Vertex(10,-10);

  private List<BlockBlue<I>> blockBlues = new ArrayList<>();
  private List<BlockGreen<I>> blockGreens = new ArrayList<>();
  private List<BlockPurple<I>> blockPurples = new ArrayList<>();
  private List<BlockRed<I>> blockReds = new ArrayList<>();
  private List<BlockYellow<I>> blockYellows = new ArrayList<>();

  private PlayerMedium<I> playerMedium;
  private List<Ball<I>> balls = new ArrayList<>();
  private int lives = 1;
  private int score = 0;

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
    super(new PlayerMedium<I>(PLAYER_START), VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    playerMedium = (PlayerMedium<I>) getPlayer();
    balls.add(new Ball<I> (new Vertex(getPlayer().getPos().x + 50, getPlayer().getPos().y - 15)));
    balls.get(0).setVelocity(BALL_VELOCITY_START);
    background.add(new Background<I>(new Vertex(0, 0)));
    playSound(startGameSound);

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

    getGOss().add(background);
    getGOss().add(balls);
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
    g.drawString(550, 20, "Score: " + score);
    if (lives <= 0){
      g.drawString(450, 300, 50, "Final score: " + score);
      g.drawString(330, 400, 50, "You loosed, try one more time");
    }
    if (blockBlues.size() == 0 && blockGreens.size() == 0 && blockPurples.size() == 0
        && blockReds.size() == 0 && blockYellows.size() == 0){
      playSound(winSound);
      g.drawString(450, 300, 50, "Final score: " + score);
      g.drawString(600, 400, 50, "You won!");
      g.drawString(450, 500, 50, "Donate to get new levels");
    }
    g.setColor(0.9, 0.9, 0);
  }

  @Override
  public void doChecks() {
    viewportCollision();
    ballIsDown();
    roundRestart();
    ballPlayerCollision();
    ballBlockCollision();
  }

  private void ballBlockCollision() {
    for (Ball b: balls){

      for (int i = 0; i < blockBlues.size(); i++){
        if (b.touches(blockBlues.get(i))){
          if (b.isLeftOf(blockBlues.get(i)) || b.isRightOf(blockBlues.get(i))){
            b.setVelocity(new Vertex(b.getVelocity().x * (-1), b.getVelocity().y));
          }else {
            b.setVelocity(new Vertex(b.getVelocity().x, b.getVelocity().y * (-1)));
          }
          playSound(blockBreakSound);
          blockBlues.remove(i);
          score += 500;
        }
      }

      for (int i = 0; i < blockGreens.size(); i++){
        if (b.touches(blockGreens.get(i))){
          if (b.isLeftOf(blockGreens.get(i)) || b.isRightOf(blockGreens.get(i))){
            b.setVelocity(new Vertex(b.getVelocity().x * (-1), b.getVelocity().y));
          }else {
            b.setVelocity(new Vertex(b.getVelocity().x, b.getVelocity().y * (-1)));
          }
          playSound(blockBreakSound);
          blockGreens.remove(i);
          score += 400;
        }
      }

      for (int i = 0; i < blockPurples.size(); i++){
        if (b.touches(blockPurples.get(i))){
          if (b.isLeftOf(blockPurples.get(i)) || b.isRightOf(blockPurples.get(i))){
            b.setVelocity(new Vertex(b.getVelocity().x * (-1), b.getVelocity().y));
          }else {
            b.setVelocity(new Vertex(b.getVelocity().x, b.getVelocity().y * (-1)));
          }
          playSound(blockBreakSound);
          blockPurples.remove(i);
          score += 300;
        }
      }

      for (int i = 0; i < blockReds.size(); i++){
        if (b.touches(blockReds.get(i))){
          if (b.isLeftOf(blockReds.get(i)) || b.isRightOf(blockReds.get(i))){
            b.setVelocity(new Vertex(b.getVelocity().x * (-1), b.getVelocity().y));
          }else {
            b.setVelocity(new Vertex(b.getVelocity().x, b.getVelocity().y * (-1)));
          }
          playSound(blockBreakSound);
          blockReds.remove(i);
          score += 200;
        }
      }

      for (int i = 0; i < blockYellows.size(); i++){
        if (b.touches(blockYellows.get(i))){
          if (b.isLeftOf(blockYellows.get(i)) || b.isRightOf(blockYellows.get(i))){
            b.setVelocity(new Vertex(b.getVelocity().x * (-1), b.getVelocity().y));
          }else {
            b.setVelocity(new Vertex(b.getVelocity().x, b.getVelocity().y * (-1)));
          }
          playSound(blockBreakSound);
          blockYellows.remove(i);
          score += 100;
        }
      }
    }
  }

  private void ballPlayerCollision() {
    for (Ball b: balls){
      if (b.touches(playerMedium)){
        b.setVelocity(new Vertex(b.getVelocity().x, b.getVelocity().y * (-1)));
      }
    }
  }

  private void roundRestart() {
    if (balls.size() <= 0 && lives > 0){
      playerMedium.getPos().moveTo(PLAYER_START);
      balls.add(new Ball<I> (new Vertex(getPlayer().getPos().x + 50, getPlayer().getPos().y - 15)));
      balls.get(0).setVelocity(BALL_VELOCITY_START);
    }
  }

  private void ballIsDown() {
    for (int i = 0; i < balls.size(); i++){
      if (balls.get(i).getPos().y + balls.get(i).getHeight() >= VIEWPORT_HEIGHT){
        balls.remove(i);
        if (balls.size() <= 0){
          playSound(looseSound);
          lives--;
        }
      }
    }
  }


  private void viewportCollision() {
    for (Ball b: balls){
      if (b.getPos().x + b.getWidth() >= VIEWPORT_WIDTH || b.getPos().x <= 0){
        b.setVelocity(new Vertex(b.getVelocity().x * (-1), b.getVelocity().y));
      }
      if (b.getPos().y <= 0){
        b.setVelocity(new Vertex(b.getVelocity().x, b.getVelocity().y * (-1)));
      }
    }
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
