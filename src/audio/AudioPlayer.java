package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * The AudioPlayer class is responsible for playing audio clips and managing the volume and muting of songs and effects.
 * It provides methods to load songs and effects, play specific songs and effects, and control the volume and muting of audio.
 */
public class AudioPlayer {

    /**
     * Represents the menu state of the game.
     */
    public static int MENU = 0;

    /**
     * Represents the in-game state of the game.
     */
    public static int INGAME = 1;

    /**
     * Represents the identifier for the player death sound effect.
     */
    public static int PLAYER_DEATH = 0;

    /**
     * Represents the identifier for the goblin death sound effect.
     */
    public static int GOBLIN_DEATH = 1;

    /**
     * Represents the identifier for the player damaged sound effect.
     */
    public static int PLAYER_DAMAGED = 2;

    /**
     * Represents the identifier for the goblin damaged sound effect.
     */
    public static int GOBLIN_DAMAGED = 3;

    /**
     * Represents the identifier for the first player attack sound effect.
     */
    public static int PLAYER_ATTACK_ONE = 4;

    /**
     * Represents the identifier for the second player attack sound effect.
     */
    public static int PLAYER_ATTACK_TWO = 5;

    /**
     * Represents the identifier for the third player attack sound effect.
     */
    public static int PLAYER_ATTACK_THREE = 6;

    /**
     * Represents the identifier for the goblin attack sound effect.
     */
    public static int GOBLIN_ATTACK = 7;

    /**
     * Represents the identifier for the player jump sound effect.
     */
    public static int PLAYER_JUMP = 8;

    /**
     * Represents the identifier for the player jump land sound effect.
     */
    public static int PLAYER_JUMP_LAND = 9;

    private Clip[] songs;       // Array to store the loaded songs
    private Clip[] effects;     // Array to store the loaded sound effects
    private int currentSongID;  // The ID of the currently playing song
    private float volume = 1f;    // The volume level for both songs and sound effects
    private boolean songMute;   // Flag to indicate whether songs are muted or not
    private boolean effectMute; // Flag to indicate whether sound effects are muted or not
    private Random r = new Random();  // Random object for generating random numbers

    /**
     * Constructs a new AudioPlayer object and loads the songs.
     * It sets the initial volume level and plays the menu song.
     */
    public AudioPlayer() {
        loadSongs();
        loadEffects();
        playSong(MENU);
    }

    /**
     * Loads the songs into the songs array using their names.
     */
    private void loadSongs() {
        String[] names = {"main_menu", "ingame"};
        songs = new Clip[names.length];
        for(int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }
    }

    /**
     * Loads the sound effects into the effects array using their names.
     */
    private void loadEffects() {
        String[] effectNames = {"player_death", "goblin_death", "player_damaged", "goblin_damaged", "player_atk_1", "player_atk_2", "player_atk_3", "goblin_atk", "player_jump", "player_jump_land"};
        effects = new Clip[effectNames.length];
        for(int i = 0; i < effects.length; i++) {
            effects[i] = getClip(effectNames[i]);
        }
        updateEffectsVolume();
    }

    /**
     * Retrieves a Clip object for the specified audio file name.
     *
     * @param name the name of the audio file
     * @return the Clip object for the specified audio file
     */
    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;
        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Toggles the mute state of songs.
     * If the songs were muted, they will be unmuted, and vice versa.
     */
    public void toggleSongMute() {
        this.songMute = !songMute;
        for(Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    /**
     * Toggles the mute state of sound effects.
     * If the sound effects were muted, they will be unmuted, and vice versa.
     */
    public void toggleEffectMute() {
        this.songMute = !songMute;
        for(Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
        if(!effectMute) {
            playEffect(PLAYER_ATTACK_THREE);
        }
    }

    /**
     * Sets the volume level for both songs and sound effects.
     *
     * @param volume the volume level (range: 0.0 to 1.0)
     */
    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    /**
     * Stops the currently playing song.
     */
    public void stopSong() {
        if(songs[currentSongID].isActive()) {
            songs[currentSongID].stop();
        }
    }

    /**
     * Sets the current song to the in-game song and starts playing it.
     */
    public void setLevelSong() {
        playSong(INGAME);
    }

    /**
     * Stops the currently playing song.
     */
    public void lvlCompleted() {
        stopSong();
    }

    /**
     * Plays the attack sound effect for the player.
     */
    public void playAttackSound() {
        playEffect(PLAYER_ATTACK_ONE);
    }

    /**
     * Plays the specified sound effect.
     *
     * @param effect the identifier of the sound effect to play
     */
    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    /**
     * Plays the specified song.
     *
     * @param song the identifier of the song to play
     */
    public void playSong(int song) {
        stopSong();
        currentSongID = song;
        updateSongVolume();
        songs[currentSongID].setMicrosecondPosition(0);
        songs[currentSongID].loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Updates the volume of the currently playing song based on the volume level.
     */
    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    /**
     * Updates the volume of the sound effects based on the volume level.
     */
    private void updateEffectsVolume() {
        for(Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}