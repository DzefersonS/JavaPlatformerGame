package gamestates;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.URMButtons.*;
/**
 * The GameOptions class is responsible for managing the options screen of the game.
 * It is an extension of the State class and implements the Statemethods interface.
 * It includes methods to load images, draw graphics, and handle user input.
 */
public class GameOptions extends State implements Statemethods {
    private AudioOptions audioOptions;
    private BufferedImage backgroundImage, optionsBackgroundImage;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB;
    /**
     * Constructor to create game options.
     *
     * @param game the game object
     */
    public GameOptions(Game game) {
        super(game);
        loadImages();
        loadButton();
        audioOptions = game.getAudioOptions();
    }
    /**
     * Load button properties.
     */
    private void loadButton() {
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) (340 * Game.SCALE);

        menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }
    /**
     * Load images for the options menu.
     */
    private void loadImages() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);

        bgW = (int) (optionsBackgroundImage.getWidth() * Game.SCALE);
        bgH = (int) (optionsBackgroundImage.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * Game.SCALE);
    }
    /**
     * Update method for the options menu.
     */
    @Override
    public void update() {
        menuB.update();
        audioOptions.update();
    }
    /**
     * Draw method for the options menu.
     *
     * @param g Graphics context
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImage, bgX, bgY, bgW, bgH, null);

        menuB.draw(g);
        audioOptions.draw(g);
    }
    /**
     * Method to handle mouse dragged event.
     *
     * @param e the MouseEvent
     */
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }
    /**
     * Method to handle mouse pressed event.
     *
     * @param e the MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }
    /**
     * Method to handle mouse released event.
     *
     * @param e the MouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menuB)) {
            if(menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
            }
        } else {
            audioOptions.mouseReleased(e);
        }

        menuB.resetBools();
    }
    /**
     * Method to handle mouse moved event.
     *
     * @param e the MouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent e) {

        menuB.setMouseOver(false);

        if(isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }
    /**
     * Method to handle key pressed event.
     *
     * @param e the KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Gamestate.state = Gamestate.MENU;
        }
    }
    /**
     * Method to handle key released event.
     *
     * @param e the KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
    /**
     * Method to handle mouse clicked event.
     *
     * @param e the MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }
    /**
     * Method to check if mouse event happened within the bounds of the button.
     *
     * @param e the MouseEvent
     * @param b the PauseButton
     * @return true if mouse event is within button bounds, false otherwise
     */
    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}
