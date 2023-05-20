package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.Constants.Enviroment.*;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;

    private boolean gameOver = false;
    private boolean playerDead = false;
    private boolean lvlCompleted = false;

    private BufferedImage backgroundImg, bigTree, smallTree;
    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
        bigTree = LoadSave.GetSpriteAtlas(LoadSave.BIG_TREES);
        smallTree = LoadSave.GetSpriteAtlas(LoadSave.SMALL_TREES);

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getMaxLvlOffsetX();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);


        player = new Player(200, 200, (int) (160*Game.SCALE), (int) (111*Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if(paused) {
            pauseOverlay.update();
        } else if(lvlCompleted) {
            levelCompletedOverlay.update();
        } else if(!gameOver) {
            levelManager.update();
            objectManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }

    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if(diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        } else if(diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        if(xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if(xLvlOffset<0) {
            xLvlOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawTrees(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);

        if(paused) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if(gameOver) {
            gameOverOverlay.draw(g);
        } else if(lvlCompleted) {
            levelCompletedOverlay.draw(g);
        }

    }

    private void drawTrees(Graphics g) {
        for(int i = 0; i < 3; ++i) {
            g.drawImage(smallTree, i * TREE_WIDTH - (int)(xLvlOffset * 0.7), (int)(100 * Game.SCALE), TREE_WIDTH, TREE_HEIGHT, null);
        }

        for(int i = 0; i < 3; ++i) {
            g.drawImage(bigTree, i * TREE_WIDTH - (int)(xLvlOffset * 0.3), (int)(100 * Game.SCALE), TREE_WIDTH, TREE_HEIGHT, null);
        }

    }

    public void resetAll() {
        //TODO: Reset all bullshit
        gameOver = false;
        playerDead = false;
        paused = false;
        lvlCompleted = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setPlayerDead(boolean playerDead) {
        this.playerDead = playerDead;
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    public void setMaxLvlOffsetX(int maxLvlOffsetX) {
        this.maxLvlOffsetX = maxLvlOffsetX;
    }

    public void unpauseGame() {
        paused = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver && !playerDead) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver && !playerDead) {
            if(paused) {
                pauseOverlay.mousePressed(e);
            } else if(lvlCompleted) {
                levelCompletedOverlay.mousePressed(e);
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver && !playerDead) {
            if(paused) {
                pauseOverlay.mouseReleased(e);
            } else if(lvlCompleted) {
                levelCompletedOverlay.mouseReleased(e);
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver && !playerDead) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            } else if(lvlCompleted) {
                levelCompletedOverlay.mouseMoved(e);
            }
        }
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
    }

    public void mouseDragged(MouseEvent e) {
        if(!gameOver && !playerDead) {
            if(paused) {
                pauseOverlay.mouseDragged(e);
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver) {
            gameOverOverlay.keyPressed(e);
        } else if(!playerDead) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_F:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver && !playerDead) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_F:
                    player.setJump(false);
                    break;
            }
        }

    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }
}
