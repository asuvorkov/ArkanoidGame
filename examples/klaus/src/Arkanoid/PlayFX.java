package Arkanoid;

import javafx.scene.image.Image;
import name.panitz.game.framework.fx.GameApplication;

import javax.sound.sampled.AudioInputStream;

/**
 * Created by Andrei on 02.02.2018.
 */
public class PlayFX extends GameApplication {
  public PlayFX(){
    super(new Arkanoid<Image, AudioInputStream>());
  }
  public static void main(String[] args) {
    PlayFX.launch();
  }
}
