package gamestates;
/**
 * The Gamestate enum represents the different states of the game.
 * The available states are: PLAYING, MENU, OPTIONS, and QUIT.
 * The current state of the game is stored in the 'state' variable.
 * This enum is used to track and manage the current state of the game.
 */
public enum Gamestate {

    /**
     * The PLAYING state indicates that the game is currently being played.
     */
    PLAYING,

    /**
     * The MENU state indicates that the game is in the main menu.
     */
    MENU,

    /**
     * The OPTIONS state indicates that the game is in the options menu.
     */
    OPTIONS,

    /**
     * The QUIT state indicates that the game is about to be exited.
     */
    QUIT;

    /**
     * The state variable represents the current state of the game.
     * By default, it is set to MENU.
     */
    public static Gamestate state = MENU;

}
