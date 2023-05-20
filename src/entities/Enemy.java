package entities;

import audio.AudioPlayer;
import main.Game;

import java.awt.geom.Rectangle2D;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GRAVITY;
import static utils.HelpMethods.*;
import static utils.Constants.Directions.*;

/**
 * The abstract Enemy class represents an enemy entity in the game.
 * It provides common functionality and properties for different types of enemies.
 * Subclasses of this class define specific enemy types.
 */
public abstract class Enemy extends Entity {

    protected int enemyType;                // The type of the enemy
    protected boolean firstUpdate = true;   // Flag to indicate if it's the first update of the enemy
    protected int walkDir = LEFT;           // The walking direction of the enemy
    protected boolean wasLeft = true;       // Flag to indicate if the enemy was facing left
    protected int tileY;                    // The tile position of the enemy on the Y-axis
    protected float attackDistance = Game.TILES_SIZE;   // The distance at which the enemy can attack
    protected boolean active = true;        // Flag to indicate if the enemy is active
    protected boolean attackChecked;        // Flag to indicate if the enemy's attack has been checked
    protected boolean isHurt = false;       // Flag to indicate if the enemy is hurt
    private Game game;                      // The game object

    /**
     * Constructs a new Enemy object with the specified position, dimensions, enemy type, and game object.
     *
     * @param x the X-coordinate of the enemy's position
     * @param y the Y-coordinate of the enemy's position
     * @param width the width of the enemy's hitbox
     * @param height the height of the enemy's hitbox
     * @param enemyType the type of the enemy
     * @param game the game object
     */
    public Enemy(float x, float y, int width, int height, int enemyType, Game game) {
        super(x, y, width, height);
        this.game = game;
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    /**
     * Checks if it's the first update of the enemy and updates the inAir flag accordingly.
     *
     * @param levelData the level data array
     */
    protected void firstUpdateCheck(int[][] levelData) {
        if(!IsEntityOnFloor(hitbox, levelData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    /**
     * Updates the enemy's position and inAir flag when in the air.
     *
     * @param levelData the level data array
     */
    protected void updateInAir(int[][] levelData) {
        if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    /**
     * Moves the enemy in the current walk direction.
     *
     * @param levelData the level data array
     */
    protected void move(int[][] levelData) {
        float xSpeed = 0;

        if(walkDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            if(IsFloor(hitbox, xSpeed, levelData)) {
                hitbox.x += xSpeed;
                return;
            }
        }

        changeWalkDir();
    }

    /**
     * Turns the enemy towards the player based on their relative position.
     *
     * @param player the player object
     */
    protected void turnTowardsPlayer(Player player) {
        if(player.hitbox.x > hitbox.x) {
            walkDir = RIGHT;
            wasLeft = false;
        } else {
            walkDir = LEFT;
            wasLeft = true;
        }
    }

    /**
     * Checks if the enemy can see the player based on their position and line of sight.
     *
     * @param levelData the level data array
     * @param player the player object
     * @return true if the enemy can see the player, false otherwise
     */
    protected boolean canSeePlayer(int[][] levelData, Player player) {
        int playerTileY = Math.round(player.getHitbox().y / Game.TILES_SIZE);
        if(playerTileY == tileY) {
            if(isPlayerInRange(player)) {
                if(IsClearSight(levelData, hitbox, player.hitbox, tileY)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the player is within the attack range of the enemy.
     *
     * @param player the player object
     * @return true if the player is in range, false otherwise
     */
    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);

        return absValue <= attackDistance * 5;
    }

    /**
     * Checks if the player is within the attack range of the enemy.
     *
     * @param player the player object
     * @return true if the player is in range, false otherwise
     */
    protected boolean isPlayerInAttackRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);

        return absValue <= attackDistance;
    }

    /**
     * Changes the state of the enemy.
     *
     * @param state the new state
     */
    protected void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     * Hurts the enemy by reducing its health and changes its state accordingly.
     *
     * @param amount the amount of damage
     */
    public void hurt(int amount) {
        currentHealth -= amount;
        if(currentHealth <= 0) {
            game.getAudioPlayer().playEffect(AudioPlayer.GOBLIN_DEATH);
            newState(DEATH);
        } else {
            game.getAudioPlayer().playEffect(AudioPlayer.GOBLIN_DAMAGED);
            newState(HIT);
            isHurt = true;
        }
    }

    /**
     * Checks if the enemy's attack intersects with the player and reduces the player's health if true.
     *
     * @param attackBox the attack hitbox of the enemy
     * @param player the player object
     */
    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if(attackBox.intersects(player.hitbox)) {
            player.changeHealth(-GetEnemyDmg(enemyType));
        }
        attackChecked = true;
    }

    /**
     * Updates the animation tick and advances to the next animation frame if necessary.
     */
    protected void updateAniTick() {
        aniTick++;
        if(aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(enemyType, state)) {
                aniIndex = 0;
                switch(state) {
                    case ATTACK -> {
                        state = IDLE;
                    } case HIT -> {
                        state = IDLE;
                        isHurt = false;
                    }
                    case DEATH -> {
                        active = false;
                    }
                }
            }
        }
    }

    /**
     * Changes the walking direction of the enemy.
     */
    protected void changeWalkDir() {
        if(walkDir == LEFT) {
            walkDir = RIGHT;
            wasLeft = false;
        } else {
            walkDir = LEFT;
            wasLeft = true;
        }
    }

    /**
     * Gets the state of the enemy.
     *
     * @return the state of the enemy
     */
    public int getState() {
        return state;
    }

    /**
     * Checks if the enemy was facing left.
     *
     * @return true if the enemy was facing left, false otherwise
     */
    public boolean getWasLeft() {
        return wasLeft;
    }

    /**
     * Checks if the enemy is active.
     *
     * @return true if the enemy is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Resets the enemy to its initial state.
     */
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }
}