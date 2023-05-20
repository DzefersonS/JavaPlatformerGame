package entities;

import audio.AudioPlayer;
import main.Game;
import java.awt.geom.Rectangle2D;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;

/**
 * The Goblin class represents a goblin enemy in the game.
 * It extends the Enemy class and provides specific behavior and characteristics for goblins.
 */
public class Goblin extends Enemy {

    private Rectangle2D.Float attackBox; // The attack box of the goblin
    private int attackBoxOffsetX; // The X-offset for the attack box
    private Game game; // The game instance

    /**
     * Constructs a new Goblin with the specified position and game instance.
     *
     * @param x    the X-coordinate of the goblin
     * @param y    the Y-coordinate of the goblin
     * @param game the game instance
     */
    public Goblin(float x, float y, Game game) {
        super(x, y, GOBLIN_SIZE, GOBLIN_SIZE, GOBLIN, game);
        this.game = game;
        initHitbox(25, 31);
        initAttackBox();
    }

    /**
     * Initializes the attack box of the goblin.
     */
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (32 * Game.SCALE), (28 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 5);
    }

    /**
     * Updates the goblin's behavior and state based on the current game state and player position.
     *
     * @param levelData the level data
     * @param player    the player instance
     */
    public void update(int[][] levelData, Player player) {
        updateBehaviour(levelData, player);
        updateAniTick();
        updateAttackBox();
    }

    /**
     * Updates the position and size of the attack box based on the goblin's direction and hitbox.
     */
    private void updateAttackBox() {
        if (walkDir == RIGHT) {
            attackBox.x = hitbox.x + hitbox.width + attackBoxOffsetX - 4 * Game.SCALE;
        } else if (walkDir == LEFT) {
            attackBox.x = hitbox.x - hitbox.width - attackBoxOffsetX - 2 * Game.SCALE;
        }
        attackBox.y = hitbox.y;
    }

    /**
     * Updates the goblin's behavior based on the current game state and player position.
     *
     * @param levelData the level data
     * @param player    the player instance
     */
    private void updateBehaviour(int[][] levelData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(levelData);

        if (inAir) {
            updateInAir(levelData);
        } else if (!isHurt) {
            switch (state) {
                case IDLE -> {
                    newState(RUN);
                }
                case RUN -> {
                    if (canSeePlayer(levelData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerInAttackRange(player)) {
                            game.getAudioPlayer().playEffect(AudioPlayer.GOBLIN_ATTACK);
                            newState(ATTACK);
                        }
                    }
                    move(levelData);
                }
                case ATTACK -> {
                    if (aniIndex == 0) {
                        attackChecked = false;
                    }
                    if (aniIndex == 6 && !attackChecked) {
                        checkEnemyHit(attackBox, player);
                    }
                }
                case HIT -> {

                }
            }
        }
    }
}