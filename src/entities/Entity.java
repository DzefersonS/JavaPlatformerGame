package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * The Entity class is an abstract class representing a game entity.
 * It provides common properties and functionality for game entities.
 */
public abstract class Entity {

    protected float x;      // The X-coordinate of the entity
    protected float y;      // The Y-coordinate of the entity
    protected int width;    // The width of the entity
    protected int height;   // The height of the entity
    protected int aniTick;  // The animation tick
    protected int aniIndex; // The animation index
    protected Rectangle2D.Float hitbox; // The hitbox of the entity
    protected int state;    // The state of the entity
    protected float airSpeed; // The air speed of the entity
    protected boolean inAir;  // Flag indicating whether the entity is in the air
    protected int maxHealth;  // The maximum health of the entity
    protected int currentHealth; // The current health of the entity
    protected Rectangle2D.Float attackBox; // The attack box of the entity
    protected float walkSpeed; // The walking speed of the entity

    /**
     * Constructs a new Entity with the specified position, width, and height.
     *
     * @param x      the X-coordinate of the entity
     * @param y      the Y-coordinate of the entity
     * @param width  the width of the entity
     * @param height the height of the entity
     */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Draws the attack box of the entity.
     *
     * @param g          the graphics object
     * @param xLvlOffset the X-level offset
     */
    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    /**
     * Draws the hitbox of the entity.
     *
     * @param g          the graphics object
     * @param xLvlOffset the X-level offset
     */
    protected void drawHitbox(Graphics g, int xLvlOffset) {
        // For debugging hitbox
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    /**
     * Initializes the hitbox with the specified width and height.
     *
     * @param width  the width of the hitbox
     * @param height the height of the hitbox
     */
    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    /**
     * Returns the hitbox of the entity.
     *
     * @return the hitbox of the entity
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Returns the animation index of the entity.
     *
     * @return the animation index
     */
    public int getAniIndex() {
        return aniIndex;
    }
}