package entities;

import static utils.Constants.PlayerConstants.*;
import main.Game;
import visuals.AnimationContainer;
import visuals.AnimationImporter;

import java.awt.*;

import static utils.Constants.Directions.*;
import static utils.HelpMethods.*;

public class Player extends Entity{
    private AnimationContainer[] animations;
    private int aniTick, aniIndex=0, aniSpeed = 15;
    private int playerAction = IDLE;
    private boolean left, up, right, down, jump;
    private boolean moving = false, attacking = false;
    private float playerSpeed = 2.0f;
    private int[][] lvlData;

    //Jumping / gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
    }

    public void update() {
        updatePos();
        updateHitbox();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        int width = animations[playerAction].getW(aniIndex)*(int)Game.SCALE;
        int height = animations[playerAction].getH(aniIndex)*(int)Game.SCALE;
        g.drawImage(animations[playerAction].getFrame(aniIndex), (int) x+(animations[playerAction].getXOffset(aniIndex)*(int) Game.SCALE), (int) y+(animations[playerAction].getYOffset(aniIndex)*(int) Game.SCALE), width, height, null);
        drawHitbox(g);
    }



    private void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if(aniIndex>= animations[playerAction].getFrameCount()) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            playerAction = RUN;
        } else {
            playerAction = IDLE;
        }

        if(inAir) {
            if(airSpeed < 0) {
                playerAction = JUMP;
            } else {
                playerAction = JUMP_FALL;
            }
        }

        if(attacking) {
            playerAction = ATTACK_1;
        }

        if(startAni!=playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;

        if(jump)
            jump();

        if(!left && !right && !inAir)
            return;

        float xSpeed=0;

        if(left)
            xSpeed -= playerSpeed;

        if(right)
            xSpeed += playerSpeed;

        if(!inAir) {
            if(!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if(inAir) {
            if(CanMoveHere(x, y + airSpeed, width, height, lvlData)) {
                y+=airSpeed;
                hitbox.y=(int)y;
                airSpeed+=gravity;
                updateXPos(xSpeed);
            } else {
                y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                hitbox.y=(int)y;
                if(airSpeed>0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }

        } else {
            updateXPos(xSpeed);
        }

        moving = true;

    }

    private void jump() {
        if(inAir) {
            return;
        } else {
            inAir = true;
            airSpeed = jumpSpeed;
        }
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(x + xSpeed, y, width, height, lvlData)) {
            this.x += xSpeed;
            hitbox.x = (int)x;
        }
        else {
            this.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            hitbox.x = (int)x;
        }
    }

    private void loadAnimations() {
        AnimationImporter importAnimations = new AnimationImporter();
        animations = importAnimations.initializePlayerAtlasContainers();
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
