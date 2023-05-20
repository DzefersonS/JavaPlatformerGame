package utils;

import main.Game;

/**
 * The Constants class contains various constant values used in the game.
 * It provides constants related to gameplay elements, objects, enemies, UI, directions, and player actions.
 */
public class Constants {

    public static final float GRAVITY = 0.04f * Game.SCALE;   // The gravity constant
    public static final int ANI_SPEED = 15;                   // The animation speed

    /**
     * The ObjectConstants class contains constants related to game objects.
     */
    public static class ObjectConstants {
        public static final int POTION = 0;                     // Object type: potion
        public static final int BARREL = 1;                     // Object type: barrel
        public static final int BOX = 2;                        // Object type: box
        public static final int SPIKE = 3;                      // Object type: spike

        public static final int POTION_VALUE = 15;              // The value of a potion

        public static final int CONTAINER_SIZE_DEFAULT = 32;    // The default container size
        public static final int CONTAINER_SIZE = (int) (CONTAINER_SIZE_DEFAULT * Game.SCALE);   // The scaled container size

        public static final int POTION_SIZE_DEFAULT = 16;       // The default potion size
        public static final int POTION_SIZE = (int) (POTION_SIZE_DEFAULT * Game.SCALE);   // The scaled potion size

        public static final int SPIKE_SIZE_DEFAULT = 32;        // The default spike size
        public static final int SPIKE_SIZE = (int) (SPIKE_SIZE_DEFAULT * Game.SCALE);   // The scaled spike size

        /**
         * Returns the number of sprite frames for the specified object type.
         *
         * @param objectType the object type
         * @return the number of sprite frames
         */
        public static int GetSpriteAmount(int objectType) {
            switch(objectType) {
                case POTION -> {
                    return 13;
                } case BARREL, BOX -> {
                    return 4;
                } case SPIKE -> {
                    return 10;
                }
            }
            return 1;
        }
    }

    /**
     * The EnemyConstants class contains constants related to enemies.
     */
    public static class EnemyConstants {
        public static final int GOBLIN = 0;                     // Enemy type: goblin

        public static final int IDLE = 0;                       // Enemy state: idle
        public static final int RUN = 1;                        // Enemy state: run
        public static final int ATTACK = 2;                     // Enemy state: attack
        public static final int HIT = 3;                        // Enemy state: hit
        public static final int DEATH = 4;                      // Enemy state: death

        public static final int GOBLIN_SIZE_DEFAULT = 150;      // The default goblin size
        public static final int GOBLIN_SIZE = (int) (GOBLIN_SIZE_DEFAULT * Game.SCALE);   // The scaled goblin size

        public static final int GOBLIN_DRAW_OFFSET_X = (int) (62 * Game.SCALE);     // The X-axis draw offset for goblins
        public static final int GOBLIN_DRAW_OFFSET_Y = (int) (68 * Game.SCALE);     // The Y-axis draw offset for goblins

        /**
         * Returns the number of sprite frames for the specified enemy type and state.
         *
         * @param enemyType the enemy type
         * @param enemyState the enemy state
         * @return the number of sprite frames
         */
        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch(enemyType) {
                case GOBLIN:
                    switch(enemyState) {
                        case RUN, ATTACK -> {
                            return 8;
                        } case IDLE, HIT, DEATH -> {
                            return 4;
                        }
                    }
            }
            return 0;
        }

        /**
         * Returns the maximum health for the specified enemy type.
         *
         * @param enemyType the enemy type
         * @return the maximum health
         */
        public static int GetMaxHealth(int enemyType) {
            switch(enemyType) {
                case GOBLIN -> {
                    return 20;
                }
            }
            return 1;
        }

        /**
         * Returns the damage amount for the specified enemy type.
         *
         * @param enemyType the enemy type
         * @return the damage amount
         */
        public static int GetEnemyDmg(int enemyType) {
            switch(enemyType) {
                case GOBLIN -> {
                    return 20;
                }
            }
            return 0;
        }
    }

    /**
     * The Enviroment class contains constants related to the game environment.
     */
    public static class Enviroment {
        public static final int TREE_WIDTH_DEFAULT = 620;       // The default tree width
        public static final int TREE_HEIGHT_DEFAULT = 360;      // The default tree height

        public static final int TREE_WIDTH = (int) (TREE_WIDTH_DEFAULT * Game.SCALE);     // The scaled tree width
        public static final int TREE_HEIGHT = (int) (TREE_HEIGHT_DEFAULT * Game.SCALE);   // The scaled tree height
    }

    /**
     * The UI class contains constants related to the user interface.
     */
    public static class UI {
        /**
         * The Buttons class contains constants related to UI buttons.
         */
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;        // The default button width
            public static final int B_HEIGHT_DEFAULT = 56;        // The default button height
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);     // The scaled button width
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);   // The scaled button height
        }

        /**
         * The PauseButtons class contains constants related to pause menu buttons.
         */
        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;      // The default sound button size
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE) - (int) (5.5 * Game.SCALE);   // The scaled sound button size
        }

        /**
         * The URMButtons class contains constants related to URM (User-Recorded Macro) buttons.
         */
        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;        // The default URM button size
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);   // The scaled URM button size
        }

        /**
         * The VolumeButtons class contains constants related to volume buttons.
         */
        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;     // The default volume button width
            public static final int VOLUME_DEFAULT_HEIGHT = 44;    // The default volume button height
            public static final int SLIDER_DEFAULT_WIDTH = 215;    // The default slider width

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);     // The scaled volume button width
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);   // The scaled volume button height
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);     // The scaled slider width
        }
    }

    /**
     * The Directions class contains constants representing directions.
     */
    public static class Directions {
        public static final int LEFT = 0;                       // Direction: left
        public static final int UP = 1;                         // Direction: up
        public static final int RIGHT = 2;                      // Direction: right
        public static final int DOWN = 3;                       // Direction: down
    }

    /**
     * The PlayerConstants class contains constants related to the player.
     */
    public static class PlayerConstants {
        public static final int ATTACK_1 = 0;                   // Player action: attack 1
        public static final int ATTACK_2 = 1;                   // Player action: attack 2
        public static final int ATTACK_3 = 2;                   // Player action: attack 3
        public static final int DEATH = 3;                      // Player action: death
        public static final int JUMP_FALL = 4;                  // Player action: jump or fall
        public static final int IDLE = 5;                       // Player action: idle
        public static final int JUMP = 6;                       // Player action: jump
        public static final int RUN = 7;                        // Player action: run
        public static final int HIT = 8;                        // Player action: hit

        /**
         * Returns the number of sprite frames for the specified player action.
         *
         * @param player_action the player action
         * @return the number of sprite frames
         */
        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case IDLE, RUN -> {
                    return 8;
                }
                case DEATH -> {
                    return 6;
                }
                case ATTACK_1, ATTACK_2, ATTACK_3, HIT -> {
                    return 4;
                }
                case JUMP, JUMP_FALL -> {
                    return 2;
                }
                default -> {
                    return 1;
                }
            }
        }
    }
}

