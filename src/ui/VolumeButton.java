package ui;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton {
    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;
    private float floatValue = 0f;

    public VolumeButton(int x, int y, int w, int h) {
        super(x + w/2, y, VOLUME_WIDTH, h);
        bounds.x -= VOLUME_WIDTH/2;
        buttonX = x + w/2;
        this.x = x;
        this.w = w;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + w - VOLUME_WIDTH / 2;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for(int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i*VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if(mousePressed) {
            index = 2;
        }
    }

    public void draw(Graphics g) {

        g.drawImage(slider, x+(int)(2.5 * Game.SCALE), y, w-(int)(5 * Game.SCALE), h, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, h, null);

    }

    public void changeX(int x) {
        if(x < minX) {
            buttonX = minX;
        } else if (x > maxX) {
            buttonX = maxX;
        } else {
            buttonX = x;
        }
        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value / range;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public float getFloatValue() {
        return floatValue;
    }
}
