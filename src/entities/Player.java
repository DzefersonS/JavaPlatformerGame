package entities;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.GRAVITY;
import static utils.Constants.PlayerConstants.*;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.HelpMethods.*;

public class Player extends Entity{
    private BufferedImage[][] animations;
    private boolean left, right, jump;
    private boolean moving = false, attacking = false;
    private int[][] lvlData;
    private float xDrawOffset = 63 * Game.SCALE;
    private float yDrawOffset = 55 * Game.SCALE;

    //Jumping / gravity
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean wasLeft;

    //Status
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int) (77 * Game.SCALE);
    private int statusBarHeight = (int) (24 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (48 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (27 * Game.SCALE);
    private int healthbarYStart = (int) (2 * Game.SCALE);

    private int healthWidth = healthBarWidth;
    private int staminaWidth = healthWidth;

    private boolean attackChecked = false;
    private Playing playing;
    private boolean tookDamage;
    private int iFrames = 0, comboTick;

    private int maxStamina;
    private float currentStamina;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        this.maxHealth = 100;
        maxStamina = 100;
        currentStamina = maxStamina;
        this.currentHealth = maxHealth;
        this.walkSpeed = 1.0f * Game.SCALE;
        initHitbox(31, 32); //31 and 50 are the sizes lol
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (40 * Game.SCALE), (int) (32 * Game.SCALE));
    }

    public void update() {
        updateHealthBar();
        updateStaminaBar();

        if(currentHealth <= 0) {
            playing.getGame().getAudioPlayer().stopSong();
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_DEATH);
            playing.setPlayerDead(true);
//            playing.setGameOver(true);
//            return;
        }
        updateAttackBox();

        updatePos();
        if(moving) {
            checkPotionTouched();
            checkSpikesTouched();
        }
        if(attacking) {
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 2) {
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox() {
        if(right) {
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 2);
        } else if(left){
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 2);
        }
        attackBox.y = hitbox.y;
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updateStaminaBar() {
        currentStamina+=0.25f;
        if(currentStamina > 100) {
            currentStamina = 100;
        }
        staminaWidth = (int) ((currentStamina / (float) maxStamina) * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        if(wasLeft) {
            g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + width, (int) (hitbox.y - yDrawOffset+4*Game.SCALE), -width, (int) (height-22*Game.SCALE), null);
        } else {
            g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset+4*Game.SCALE), width, (int) (height-22*Game.SCALE), null);
        }
//        g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset+4*Game.SCALE), width, (int) (height-22*Game.SCALE), null);
//        drawHitbox(g, lvlOffset);
//        drawAttackBox(g, lvlOffset);

        drawUI(g);
    }


    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, Math.round(statusBarWidth*2.5f), Math.round(statusBarHeight*2.5f), null);
        Color health = new Color(255, 0, 68);
        g.setColor(health);
        g.fillRect(Math.round(healthBarXStart*2.5f) + statusBarX, Math.round((healthbarYStart*2.5f) + statusBarY + Game.SCALE*2.5f*0), Math.round((healthWidth-Game.SCALE*0)*2.5f), Math.round(Game.SCALE*2.5f));
        g.fillRect(Math.round(healthBarXStart*2.5f) + statusBarX, Math.round((healthbarYStart*2.5f) + statusBarY + Game.SCALE*2.5f*1), Math.round((healthWidth-Game.SCALE*1)*2.5f), Math.round(Game.SCALE*2.5f));
        g.fillRect(Math.round(healthBarXStart*2.5f) + statusBarX, Math.round((healthbarYStart*2.5f) + statusBarY + Game.SCALE*2.5f*2), Math.round((healthWidth-Game.SCALE*2)*2.5f), Math.round(Game.SCALE*2.5f));
        g.setColor(new Color(158, 40, 53));
        g.fillRect(Math.round(healthBarXStart*2.5f) + statusBarX, Math.round((healthbarYStart*2.5f) + statusBarY + Game.SCALE*2.5f*3), Math.round((healthWidth-Game.SCALE*3)*2.5f), Math.round(Game.SCALE*2.5f));

        g.setColor(new Color(0, 255, 22));
        g.fillRect(Math.round(healthBarXStart*2.5f) + statusBarX, Math.round((healthbarYStart*2.5f) + statusBarY + Game.SCALE*2.5f*7), Math.round((staminaWidth-Game.SCALE*7)*2.5f), Math.round(Game.SCALE*2.5f));
        g.fillRect(Math.round(healthBarXStart*2.5f) + statusBarX, Math.round((healthbarYStart*2.5f) + statusBarY + Game.SCALE*2.5f*8), Math.round((staminaWidth-Game.SCALE*8)*2.5f), Math.round(Game.SCALE*2.5f));

        g.setColor(new Color(0, 150, 13));
        g.fillRect(Math.round(healthBarXStart*2.5f) + statusBarX, Math.round((healthbarYStart*2.5f) + statusBarY + Game.SCALE*2.5f*9), Math.round((staminaWidth-Game.SCALE*9)*2.5f), Math.round(Game.SCALE*2.5f));

    }



    private void updateAnimationTick() {
        aniTick++;
        if(iFrames > 0) {
            iFrames--;
        }
        if(comboTick > 0) {
            comboTick--;
        }
        if(aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if(aniIndex>= GetSpriteAmount(state)) {
                if(currentHealth<=0) {
                    playing.setGameOver(true);
                    return;
                } else if(tookDamage) {
                    tookDamage = false;
                } else if(attacking) {
                    comboTick = 48 * ANI_SPEED;
                }
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = state;
        if (moving) {
            state = RUN;
        } else {
            state = IDLE;
        }

        if(inAir) {
            if(airSpeed < 0) {
                state = JUMP;
            } else {
                state = JUMP_FALL;
            }
        }

        if(attacking) {
            if(comboTick != 0) {
                comboTick = 0;
                state = ATTACK_1;
            } else {
                state = ATTACK_1;
            }
        }

        if(tookDamage) {
            state = HIT;
        }

        if(currentHealth<=0) {
            state = DEATH;
        }

        if(startAni!=state) {
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

        if(!inAir) {
            if((!left && !right) || (right && left)) {
                return;
            }
        }

        float xSpeed=0;

        if(left) {
            xSpeed -= walkSpeed;
            wasLeft = true;
        }

        if(right) {
            xSpeed += walkSpeed;
            wasLeft = false;
        }

        if(!inAir) {
            if(!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if(inAir) {
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y+=airSpeed;
                airSpeed+=GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed>0) {
                    resetInAir();
                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_JUMP_LAND);
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
        }
        if(currentStamina - 40 >= 0) {
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_JUMP);
            inAir = true;
            airSpeed = jumpSpeed;
            currentStamina-=40;
        }
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        }
        else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int val) {
        if(iFrames == 0) {
            currentHealth += val;
            if(val<0) {
                tookDamage = true;
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_DAMAGED);
                iFrames = 3 * ANI_SPEED;
            }

            if(currentHealth <= 0) {
                currentHealth = 0;
                //gameOver();
            } else if(currentHealth > maxHealth) {
                currentHealth = maxHealth;
            }
        }

    }

    private void loadAnimations() {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[9][8];

        for(int j = 0; j < animations.length; j++) {
            for(int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i*160, j*111, 160, 111);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);


    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
        if(!tookDamage)
            if(currentStamina - 25 >= 0) {
                this.attacking = attacking;
                currentStamina-=25;
            }
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;
        currentStamina = maxStamina;

        hitbox.x = x;
        hitbox.y = y;

        if(!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }
}
