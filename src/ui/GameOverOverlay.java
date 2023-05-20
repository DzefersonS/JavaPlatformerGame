package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage overlay;
    private BufferedImage arrow;
    private int arrowPos = 0;
    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        overlay = LoadSave.GetSpriteAtlas(LoadSave.GAME_OVER_OVERLAY);
        arrow = LoadSave.GetSpriteAtlas(LoadSave.CHOICE_ARROW);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(overlay, Math.round(Game.GAME_WIDTH / 2.5f), Game.GAME_HEIGHT / 4, Math.round(203 * Game.SCALE), Math.round(203 * Game.SCALE), null);
        g.drawImage(arrow, Math.round(Game.GAME_WIDTH / 2.5f) + (int) (75 * Game.SCALE) + (int) ((11 * Game.SCALE) * arrowPos), Game.GAME_HEIGHT / 4 + (int) (174 * Game.SCALE) + (int) ((12 * Game.SCALE) * arrowPos), Math.round(6 * Game.SCALE), Math.round(7.5f * Game.SCALE), null);
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_S, KeyEvent.VK_W, KeyEvent.VK_DOWN, KeyEvent.VK_UP -> {
                changeArrowPos();
            } case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                if(arrowPos == 0) {
                    playing.resetAll();
                    playing.getGame().getAudioPlayer().setLevelSong();
                } else {
                    playing.resetAll();
                    playing.setGameState(Gamestate.MENU);
                }
            }
        }
    }

    private void changeArrowPos() {
        if (arrowPos == 0) {
            arrowPos = 1;
        } else {
            arrowPos = 0;
        }
    }


}
