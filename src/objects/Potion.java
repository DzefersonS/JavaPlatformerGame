package objects;

import main.Game;

public class Potion extends GameObject {

    private float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initHitbox(12, 17);
        xDrawOffset = (int)(2 * Game.SCALE);
        yDrawOffset = (int)(0 * Game.SCALE);

        maxHoverOffset = (int)(10 * Game.SCALE);
    }

    public void update() {
        updateAniTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.075f * Game.SCALE * hoverDir);

        if(hoverOffset >= maxHoverOffset) {
            hoverDir = -1;
        } else if(hoverOffset < 0) {
            hoverDir = 1;
        }

        hitbox.y = y + hoverOffset;
    }
}
