package entities;

import java.awt.*;

public abstract class Entity { //abstract - negalima sukurti Entity objekto

    protected float x,y; //Tik subklases gali naudoti X ir Y
    protected int width, height;
    protected Rectangle hitbox;
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        initHitbox();
    }

    protected void drawHitbox(Graphics g) {
        //For debugging hitbox
        g.setColor(Color.RED);
        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    private void initHitbox() {
        hitbox = new Rectangle((int) x, (int) y, width, height);
    }

    protected void updateHitbox() {
        hitbox.x = (int) x;
        hitbox.y = (int) y;
    }

    public void setHitboxDimensions(int width, int height) {
        hitbox.width = width;
        hitbox.height = height;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

}
