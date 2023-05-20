package utils;

import entities.Goblin;
import main.Game;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.GOBLIN;
import static utils.Constants.ObjectConstants.*;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {

        if(!IsSolid(x,y,lvlData))
            if(!IsSolid(x+width,y+height,lvlData))
                if(!IsSolid(x+width,y,lvlData))
                    if(!IsSolid(x,y+height,lvlData))
                        return true;

        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if(x < 0 || x >= maxWidth) {
            return true;
        }
        if(y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);

    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] levelData) {
        int value = levelData[yTile][xTile];

        if(value >= 48 || value < 0 || value != 11) {
            return true;
        }

        return false;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = Math.round((hitbox.x / Game.TILES_SIZE));
        if(xSpeed>0) {
            //right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = Math.round((Game.TILES_SIZE - hitbox.width));
            return tileXPos + xOffset - 1;
        } else {
            //left
            return currentTile * Game.TILES_SIZE;
        }

    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = Math.round((hitbox.y / Game.TILES_SIZE));
        if(airSpeed > 0) {
            //Falling
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = Math.round((Game.TILES_SIZE - hitbox.height));
            return tileYPos + yOffset - 1;
        } else {
            //Jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if(!IsSolid(hitbox.x, hitbox.y+hitbox.height+1, lvlData)) {
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height+1, lvlData)) {
                return false;
            }
        }

        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if(xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] levelData) {
        for(int i = 0; i < xEnd - xStart; ++i) {
            if (IsTileSolid(xStart + i, y, levelData)) {
                return false;
            }
            if(!IsTileSolid(xStart+i,y+1, levelData)) {
                return false;
            }
        }

        return true;
    }

    public static boolean IsClearSight(int[][] levelData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = Math.round(firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = Math.round(secondHitbox.x / Game.TILES_SIZE);

        if(firstXTile > secondXTile) {
            return IsAllTileWalkable(secondXTile, firstXTile, yTile, levelData);
        } else {
            return IsAllTileWalkable(firstXTile, secondXTile, yTile, levelData);
        }
    }

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        for(int i = 0; i < img.getHeight(); ++i) {
            for(int j = 0; j < img.getWidth(); ++j) {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getRed();
                if(val >= 48) {
                    val = 0;
                }
                lvlData[i][j] = val;
            }
        }

        return lvlData;
    }

    public static ArrayList<Goblin> GetGoblins(BufferedImage img, Game game) {

        ArrayList<Goblin> list = new ArrayList<>();

        for(int i = 0; i < img.getHeight(); ++i) {
            for(int j = 0; j < img.getWidth(); ++j) {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getGreen();
                if(val == GOBLIN) {
                    list.add(new Goblin(j * Game.TILES_SIZE, i * Game.TILES_SIZE, game));
                }
            }
        }

        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage img) {

        for(int i = 0; i < img.getHeight(); ++i) {
            for(int j = 0; j < img.getWidth(); ++j) {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getGreen();
                if(val == 100) {
                    return new Point(j * Game.TILES_SIZE, i * Game.TILES_SIZE);
                }
            }
        }

        return new Point(1, 1);

    }

    public static ArrayList<Potion> GetPotions(BufferedImage img) {

        ArrayList<Potion> list = new ArrayList<>();

        for(int i = 0; i < img.getHeight(); ++i) {
            for(int j = 0; j < img.getWidth(); ++j) {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getBlue();
                if(val == POTION) {
                    list.add(new Potion(j * Game.TILES_SIZE, i * Game.TILES_SIZE, POTION));
                }
            }
        }

        return list;
    }

    public static ArrayList<GameContainer> GetContainers(BufferedImage img) {

        ArrayList<GameContainer> list = new ArrayList<>();

        for(int i = 0; i < img.getHeight(); ++i) {
            for(int j = 0; j < img.getWidth(); ++j) {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getBlue();
                if(val == BARREL || val == BOX) {
                    list.add(new GameContainer(j * Game.TILES_SIZE, i * Game.TILES_SIZE, val));
                }
            }
        }

        return list;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage img) {
        ArrayList<Spike> list = new ArrayList<>();

        for(int i = 0; i < img.getHeight(); ++i) {
            for(int j = 0; j < img.getWidth(); ++j) {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getBlue();
                if(val == SPIKE) {
                    list.add(new Spike(j * Game.TILES_SIZE, i * Game.TILES_SIZE, val));
                }
            }
        }

        return list;
    }
}
