package objects;

import main.Game;

public class Spike extends GameObject{
    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);

        initHitbox(32, 14);
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 8);
        hitbox.y += yDrawOffset;
    }

    public void update() {
        updateAniTick();
    }
}
