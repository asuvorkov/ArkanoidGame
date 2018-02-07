package Arkanoid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import name.panitz.game.framework.*;

/**
 * Created by Andrei on 02.02.2018.
 */
public class Arkanoid<I, S> extends AbstractGame<I, S> {
  private static final int VIEWPORT_WIDTH = 1250;
  private static final int VIEWPORT_HEIGHT = 800;
  private static final Vertex PLAYER_START = new Vertex(600,750);
  private static final Vertex BALL_VELOCITY_START = new Vertex(10,-10);

  private static final String PLAYER_XL_IMAGE = "./Player/platform_xl.png";
  private static final String PLAYER_XS_IMAGE = "./Player/platform_xs.png";
  private static final String PLAYER_L_IMAGE = "./Player/platform_l.png";
  private static final String PLAYER_M_IMAGE = "./Player/platform_m.png";
  private static final String PLAYER_S_IMAGE = "./Player/platform_s.png";

  private static final String BLOCK_BLUE_IMAGE = "./Block/block_blue.png";
  private static final String BLOCK_GREEN_IMAGE = "./Block/block_green.png";
  private static final String BLOCK_PURPLE_IMAGE = "./Block/block_purple.png";
  private static final String BLOCK_RED_IMAGE = "./Block/block_red.png";
  private static final String BLOCK_YELLOW_IMAGE = "./Block/block_yellow.png";

  private static final String BONUS_LARGER_PLATFORM = "./Bonus/larger.png";
  private static final String BONUS_MEGA_PLATFORM = "./Bonus/mega_platform.png";
  private static final String BONUS_SCORE_X2 = "./Bonus/scoreX2.png";
  private static final String BONUS_SMALLER_PLATFORM = "./Bonus/smaller.png";

  private boolean won = false;

  private Player<I> player;
  private List<Block<I>> blocks = new ArrayList<>();
  private List<Ball<I>> balls = new ArrayList<>();
  private List<Bonus<I>> bonuses = new ArrayList<>();
  private List<Background<I>> background = new ArrayList<>();

  private int lives = 1;
  private int score = 0;

  private SoundObject<S> badBonusSound = new SoundObject<>("./Sounds/bad_bonus.wav");
  private SoundObject<S> goodBonusSound = new SoundObject<>("./Sounds/good_bonus.wav");
  private SoundObject<S> blockBreakSound = new SoundObject<>("./Sounds/block_break.wav");
  private SoundObject<S> platformLengthChangeSound = new SoundObject<>("./Sounds/platform_length_change.wav");
  private SoundObject<S> startGameSound = new SoundObject<>("./Sounds/start_game.wav");
  private SoundObject<S> looseSound = new SoundObject<>("./Sounds/lost.wav");
  private SoundObject<S> winSound = new SoundObject<>("./Sounds/won.wav");

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

  Arkanoid() {
    super(new Player<I>(PLAYER_START, PLAYER_M_IMAGE), VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    player = (Player<I>) getPlayer();
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
              blocks.add(new Block<I>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT), BLOCK_BLUE_IMAGE)); break;
            case 'g':
              blocks.add(new Block<I>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT), BLOCK_GREEN_IMAGE)); break;
            case 'p':
              blocks.add(new Block<I>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT), BLOCK_PURPLE_IMAGE)); break;
            case 'r':
              blocks.add(new Block<I>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT), BLOCK_RED_IMAGE)); break;
            case 'y':
              blocks.add(new Block<I>
                  (new Vertex(col * GRID_WIDTH, l * GRID_HEIGHT), BLOCK_YELLOW_IMAGE)); break;
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
    getGOss().add(blocks);
    getGOss().add(bonuses);

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
    if (blocks.size() == 0){
      if (!won){
        playSound(winSound);
        won = true;
      }
      g.drawString(450, 300, 50, "Final score: " + score + lives * 10000);
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
    bonusCollision();
  }

  private void bonusCollision() {
    for (int i = 0; i < bonuses.size(); i++){
      if (bonuses.get(i).touches(player)){
        if (Objects.equals(bonuses.get(i).getImageFileName(), BONUS_SMALLER_PLATFORM)){
          playSound(badBonusSound);
          switch (player.getImageFileName()){
            case PLAYER_XL_IMAGE : player.setImageFileName(PLAYER_L_IMAGE); break;
            case PLAYER_L_IMAGE : player.setImageFileName(PLAYER_M_IMAGE); break;
            case PLAYER_M_IMAGE : player.setImageFileName(PLAYER_S_IMAGE); break;
            case PLAYER_S_IMAGE : player.setImageFileName(PLAYER_XS_IMAGE); break;
          }
        }else {
          if (Objects.equals(bonuses.get(i).getImageFileName(), BONUS_LARGER_PLATFORM)){
            playSound(platformLengthChangeSound);
            switch (player.getImageFileName()){
              case PLAYER_XS_IMAGE : player.setImageFileName(PLAYER_S_IMAGE); break;
              case PLAYER_S_IMAGE : player.setImageFileName(PLAYER_M_IMAGE); break;
              case PLAYER_M_IMAGE : player.setImageFileName(PLAYER_L_IMAGE); break;
              case PLAYER_L_IMAGE : player.setImageFileName(PLAYER_XL_IMAGE); break;
            }
          }
          if (Objects.equals(bonuses.get(i).getImageFileName(), BONUS_SCORE_X2)){
            playSound(goodBonusSound);
            score *= 2;
          }
          if (Objects.equals(bonuses.get(i).getImageFileName(), BONUS_MEGA_PLATFORM)){
            playSound(goodBonusSound);
            lives++;
          }
        }
        bonuses.remove(i);
      }else {
        if (bonuses.get(i).getPos().y > VIEWPORT_HEIGHT){
          bonuses.remove(i);
        }
      }
    }
  }

  private void ballBlockCollision() {
    for (Ball b: balls){
      for (int i = 0; i < blocks.size(); i++){
        if (b.touches(blocks.get(i))){
          if (b.getPos().y + b.getHeight() == blocks.get(i).getPos().y
              || b.getPos().y == blocks.get(i).getPos().y + blocks.get(i).getHeight()){
            b.setVelocity(new Vertex(b.getVelocity().x * (-1), b.getVelocity().y));
          }else {
            b.setVelocity(new Vertex(b.getVelocity().x, b.getVelocity().y * (-1)));
          }
          playSound(blockBreakSound);

          int random1 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
          if (random1 < 5){
            int random2 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
            if (random2 == 0){
              bonuses.add(new Bonus<I>
                  (new Vertex(blocks.get(i).getPos().x, blocks.get(i).getPos().y), BONUS_MEGA_PLATFORM));
            }
            if (random2 > 0 && random2 < 3){
              bonuses.add(new Bonus<I>
                  (new Vertex(blocks.get(i).getPos().x, blocks.get(i).getPos().y), BONUS_SCORE_X2));
            }
            if (random2 > 2 && random2 < 7){
              bonuses.add(new Bonus<I>
                  (new Vertex(blocks.get(i).getPos().x, blocks.get(i).getPos().y), BONUS_LARGER_PLATFORM));
            }
            if (random2 > 6){
              bonuses.add(new Bonus<I>
                  (new Vertex(blocks.get(i).getPos().x, blocks.get(i).getPos().y), BONUS_SMALLER_PLATFORM));
            }
          }

          switch (blocks.get(i).getImageFileName()) {
            case BLOCK_BLUE_IMAGE : score += 500; break;
            case BLOCK_GREEN_IMAGE : score += 400; break;
            case BLOCK_PURPLE_IMAGE : score += 300; break;
            case BLOCK_RED_IMAGE : score += 200; break;
            case BLOCK_YELLOW_IMAGE : score += 100; break;
          }
          blocks.remove(i);
        }
      }
    }
  }

  private void ballPlayerCollision() {
    for (Ball b: balls){
      if (b.touches(player)){
        b.setVelocity(new Vertex(b.getVelocity().x, b.getVelocity().y * (-1)));
      }
    }
  }

  private void roundRestart() {
    if (balls.size() <= 0 && lives > 0){
      player.getPos().moveTo(PLAYER_START);
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
          player.left();
          break;
        case RIGHT_ARROW:
          player.right();
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
          player.stop();
          break;
        case RIGHT_ARROW:
          player.stop();
          break;
        default:
          ;
      }
    }
  }
}
