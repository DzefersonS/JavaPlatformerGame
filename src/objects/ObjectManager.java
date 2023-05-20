package objects;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[] potionImg, spikeImg;
    private BufferedImage[][] containerImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void checkSpikesTouched(Player p) {
        for(Spike s : spikes) {
            if(s.getHitbox().intersects(p.getHitbox())) {
                p.changeHealth(-100);
                return;
            }
        }
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for(Potion p : potions) {
            if(p.isActive()) {
                if(hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        }

    }
    public void applyEffectToPlayer(Potion p) {
        if(p.getObjectType() == POTION) {
            playing.getPlayer().changeHealth(POTION_VALUE);
        }

    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for(GameContainer gc : containers) {
            if(gc.isActive() && !gc.doAnimation) {
                if(gc.getHitbox().intersects(attackbox)) {
                    gc.setDoAnimation(true);
                    int type = 0;
                    if(gc.getObjectType() == BARREL) {
                        type = 0;
                    }
                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width/2), (int)(gc.getHitbox().y - gc.getHitbox().height / 4), type));
                    return;
                }
            }
        }
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImg = new BufferedImage[13];

        for(int i = 0; i < potionImg.length; i++) {
            potionImg[i] = potionSprite.getSubimage(i * POTION_SIZE_DEFAULT, 0, POTION_SIZE_DEFAULT, POTION_SIZE_DEFAULT);
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][4];

        for(int j = 0; j < containerImgs.length; j++) {
            for(int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = containerSprite.getSubimage(i * CONTAINER_SIZE_DEFAULT, j * CONTAINER_SIZE_DEFAULT, CONTAINER_SIZE_DEFAULT, CONTAINER_SIZE_DEFAULT);
            }
        }

        BufferedImage spikeSprite = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
        spikeImg = new BufferedImage[10];

        for(int i = 0; i < spikeImg.length; i++) {
            spikeImg[i] = spikeSprite.getSubimage(i * SPIKE_SIZE_DEFAULT, 0, SPIKE_SIZE_DEFAULT, SPIKE_SIZE_DEFAULT);
        }
    }

    public void update() {
        for(Potion p : potions) {
            if(p.isActive()) {
                p.update();
            }
        }

        for(GameContainer gc : containers) {
            if(gc.isActive()) {
                gc.update();
            }
        }

        for(Spike s : spikes) {
            s.update();
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for(Spike s : spikes) {
            g.drawImage(spikeImg[s.getAniIndex()], (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y + s.getyDrawOffset()), SPIKE_SIZE, SPIKE_SIZE, null);
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for(Potion p : potions) {
            if(p.isActive()) {
                g.drawImage(potionImg[p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), POTION_SIZE, POTION_SIZE, null);
            }
        }
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for(GameContainer gc : containers) {
            if(gc.isActive()) {
                int type = 0;
                if(gc.getObjectType() == BARREL) {
                    type = 1;
                }
                g.drawImage(containerImgs[type][gc.getAniIndex()], (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), (int) (gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_SIZE, CONTAINER_SIZE, null);
            }
        }
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for(Potion p : potions) {
            p.reset();
        }
        for(GameContainer gc : containers) {
            gc.reset();
        }

        for(Spike s : spikes) {
            s.reset();
        }
    }
}
