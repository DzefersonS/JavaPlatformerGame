package utils;

import entities.Goblin;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;
public class LoadSave {

    public static final String PLAYER_ATLAS = "spritesheet.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background_lee_chain_test.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.jpg";
    public static final String PLAYING_BACKGROUND_IMG = "playing_bg_img.png";
    public static final String BIG_TREES = "big_trees.png";
    public static final String SMALL_TREES = "small_trees.png";
    public static final String GOBLIN_SPRITE = "goblin_sprite.png";
    public static final String STATUS_BAR = "health_stamina_bar.png";
    public static final String GAME_OVER_OVERLAY = "game_over_overlay.png";
    public static final String CHOICE_ARROW = "choice_arrow.png";
    public static final String LEVEL_COMPLETED_OVERLAY = "completed_sprite.png";

    public static final String POTION_ATLAS = "potion_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "spike_trap.png";
    public static final String OPTIONS_MENU = "options_background.png";



    public static BufferedImage GetSpriteAtlas(String filename) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + filename);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return img;
    }

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch(URISyntaxException e) {

        }

        File[] files = file.listFiles();
        File[] sortedFiles = new File[files.length];

        for(int i = 0; i < sortedFiles.length; i++) {
            for(int j = 0; j < files.length; j++) {
                if(files[j].getName().equals((i + 1) + ".png")) {
                    sortedFiles[i] = files[j];
                }
            }
        }

        BufferedImage[] imgs = new BufferedImage[sortedFiles.length];

        for(int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(sortedFiles[i]);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return imgs;
    }

}
