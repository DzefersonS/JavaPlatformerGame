package visuals;

import java.awt.image.BufferedImage;

public class AnimationContainer {
    private BufferedImage[] animation;
    int frameCount;
    int[][] dimensions;
    int[][] offsets;
    public AnimationContainer(int n) {
        animation = new BufferedImage[n];
        frameCount = n;
        dimensions = new int[n][2];
        offsets = new int[n][2];
    }

    public void setBufferedImage(BufferedImage img, int frame, int w, int h, int offsetX, int offsetY) {
        animation[frame] = img;
        dimensions[frame][0] = w;
        dimensions[frame][1] = h;
        offsets[frame][0] = offsetX;
        offsets[frame][1] = offsetY;
    }

    public int getW(int frame) {
        return dimensions[frame][0];
    }

    public int getH(int frame) {
        return dimensions[frame][1];
    }

    public int getXOffset(int frame) {
        return offsets[frame][0];
    }

    public int getYOffset(int frame) {
        return offsets[frame][1];
    }

    public int getFrameCount() {
        return frameCount;
    }

    public BufferedImage getFrame(int frame) {
        return animation[frame];
    }


}
