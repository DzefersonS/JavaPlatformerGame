package visuals;

import utils.LoadSave;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.exit;
import static utils.Constants.PlayerConstants.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class AnimationImporter {
    File aniData;
    private AnimationContainer[] animations;
    public AnimationImporter() {
    }



    private void importImg(String filepath) {

    }

    public AnimationContainer[] initializePlayerAtlasContainers() {
        int temp, x, y, w, h, offsetX, offsetY;
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new AnimationContainer[25];
        try {
            aniData = new File("res/spriteData.txt");
            Scanner readAniData = new Scanner(aniData);
            for(int i = 0; i < 17; ++i) {
                temp = readAniData.nextInt();
                System.out.println("temp = " + temp);
                animations[i] = new AnimationContainer(temp);
                for(int j = 0; j < temp; ++j) {
                    x = readAniData.nextInt();
                    y = readAniData.nextInt();
                    w = readAniData.nextInt();
                    h = readAniData.nextInt();
                    offsetX = readAniData.nextInt();
                    offsetY = readAniData.nextInt();

                    animations[i].setBufferedImage(img.getSubimage(x, y, w, h), j, w, h, offsetX, offsetY);
                }
            }
            readAniData.close();
        } catch(FileNotFoundException e) {
            System.out.println("Couldn't find sprite file.");
            e.printStackTrace();
            exit(1);
        }
        return animations;
    }

}
