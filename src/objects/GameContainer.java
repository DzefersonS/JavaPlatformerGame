package objects;

import main.Game;

import static utils.Constants.ObjectConstants.*;

public class GameContainer extends GameObject {
    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox();
    }

    private void createHitbox() {
        if(objectType == BOX) {
            initHitbox(16, 23);
            xDrawOffset = (int)(8 * Game.SCALE);
            yDrawOffset = (int)(9 * Game.SCALE);
        } else {
            initHitbox(16, 22);
            xDrawOffset = (int)(8 * Game.SCALE);
            yDrawOffset = (int)(10 * Game.SCALE);

        }

        hitbox.y += yDrawOffset + (int)(Game.SCALE);
        hitbox.x += xDrawOffset / 2;
    }

    public void update() {
        if(doAnimation) {
            updateAniTick();
        }
    }
}
