package entities;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;

/**
 * The EnemyManager class manages the enemies in the game.
 * It handles loading and updating enemies, drawing them on the screen, and checking for enemy-player interactions.
 */
public class EnemyManager {

    private Playing playing;                   // The Playing game state
    private BufferedImage[][] goblinArray;     // The array of goblin sprites
    private ArrayList<Goblin> goblins = new ArrayList<>();   // The list of goblins

    /**
     * Constructs a new EnemyManager object with the specified Playing game state.
     *
     * @param playing the Playing game state
     */
    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    /**
     * Loads the enemies from the specified level.
     *
     * @param level the level containing the enemies
     */
    public void loadEnemies(Level level) {
        goblins = level.getGoblins();
        System.out.println("Size of goblins " + goblins.size());
    }

    /**
     * Updates the enemies based on the level data and player position.
     *
     * @param levelData the level data array
     * @param player the player object
     */
    public void update(int[][] levelData, Player player) {
        boolean isAnyEnemyAlive = false;
        for(Goblin goblin : goblins) {
            if(goblin.isActive()) {
                goblin.update(levelData, player);
                isAnyEnemyAlive = true;
            }
        }
        if(!isAnyEnemyAlive) {
            playing.setLevelCompleted(true);
        }
    }

    /**
     * Draws the enemies on the screen with the specified X level offset.
     *
     * @param g the graphics object to draw on
     * @param xLvlOffset the X level offset
     */
    public void draw(Graphics g, int xLvlOffset) {
        drawGoblins(g, xLvlOffset);
    }

    /**
     * Draws the goblins on the screen with the specified X level offset.
     *
     * @param g the graphics object to draw on
     * @param xLvlOffset the X level offset
     */
    private void drawGoblins(Graphics g, int xLvlOffset) {
        for(Goblin goblin : goblins) {
            if(goblin.isActive()) {
                if(goblin.getWasLeft()) {
                    g.drawImage(goblinArray[goblin.getState()][goblin.getAniIndex()], (int) (goblin.getHitbox().x - GOBLIN_DRAW_OFFSET_X) - xLvlOffset + GOBLIN_SIZE, (int) (goblin.getHitbox().y - GOBLIN_DRAW_OFFSET_Y), -GOBLIN_SIZE, GOBLIN_SIZE, null);
                } else {
                    g.drawImage(goblinArray[goblin.getState()][goblin.getAniIndex()], (int) (goblin.getHitbox().x - GOBLIN_DRAW_OFFSET_X) - xLvlOffset, (int) (goblin.getHitbox().y - GOBLIN_DRAW_OFFSET_Y), GOBLIN_SIZE, GOBLIN_SIZE, null);
                }
            }
        }
    }

    /**
     * Checks if the enemy's attack intersects with any active goblins and reduces their health if true.
     *
     * @param attackBox the attack hitbox of the player
     */
    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for(Goblin g : goblins) {
            if(g.isActive()) {
                if(attackBox.intersects(g.getHitbox())) {
                    g.hurt(10);
                    return;
                }
            }
        }
    }

    /**
     * Loads the goblin sprites from the sprite atlas.
     */
    private void loadEnemyImgs() {
        goblinArray = new BufferedImage[5][8];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.GOBLIN_SPRITE);

        for(int j = 0; j < goblinArray.length; j++) {
            for(int i = 0; i < goblinArray[j].length; i++) {
                goblinArray[j][i] = temp.getSubimage(i * GOBLIN_SIZE_DEFAULT, j * GOBLIN_SIZE_DEFAULT, GOBLIN_SIZE_DEFAULT, GOBLIN_SIZE_DEFAULT);
            }
        }
    }

    /**
     * Resets all the enemies to their initial state.
     */
    public void resetAllEnemies() {
        for(Goblin g : goblins) {
            g.resetEnemy();
        }
    }
}