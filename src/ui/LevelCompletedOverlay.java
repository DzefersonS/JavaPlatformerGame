package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.URMButtons.URM_SIZE;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage image;
    private int bgX, bgY, bgW, bgH;
    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImage();
        initButtons();
    }

    private void initImage() {
        image = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMPLETED_OVERLAY);
        bgW = (int) (image.getWidth() * Game.SCALE);
        bgH = (int) (image.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);
    }

    private void initButtons() {
        int menuX = (int) (343 * Game.SCALE);
        int nextX = (int) (433 * Game.SCALE);
        int y = (int) (185 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    public void update() {
        next.update();
        menu.update();

    }

    public void draw(Graphics g) {
        g.drawImage(image, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }

    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if(isIn(menu, e)) {
            menu.setMouseOver(true);
        } else if(isIn(next, e)) {
            next.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(menu, e)) {
            if(menu.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
            }
        } else if(isIn(next, e)) {
            if(next.isMousePressed()) {
                playing.loadNextLevel();
                playing.getGame().getAudioPlayer().setLevelSong();
            }
        }

        menu.resetBools();
        next.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(menu, e)) {
            menu.setMousePressed(true);
        } else if(isIn(next, e)) {
            next.setMousePressed(true);
        }
    }
}