package utils;

import main.Game;

import java.awt.*;

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
        if(x < 0 || x >= Game.GAME_WIDTH) {
            return true;
        }
        if(y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];

        if(value >= 48 || value < 0 || value != 11) {
            return true;
        }

        return false;

    }

    public static float GetEntityXPosNextToWall(Rectangle hitbox, float xSpeed) {
        int currentTile = hitbox.x / Game.TILES_SIZE;
        if(xSpeed>0) {
            //right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = Game.TILES_SIZE - hitbox.width;
            return tileXPos + xOffset - 1;
        } else {
            //left
            return currentTile * Game.TILES_SIZE;
        }

    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle hitbox, float airSpeed) {
        int currentTile = hitbox.y / Game.TILES_SIZE;
        System.out.println("HITBOX Y = " + hitbox.y);
        if(airSpeed > 0) {
            //Falling
            int tileYPos = currentTile * Game.TILES_SIZE;
            System.out.println("TILE Y POS = " + tileYPos);
            int yOffset = Game.TILES_SIZE - hitbox.height;
            System.out.println("YOFFSET = " + yOffset);
            System.out.println("AAAAA");
            return tileYPos - yOffset + (int) (19.5 * Game.SCALE);
        } else {
            //Jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle hitbox, int[][] lvlData) {
        if(!IsSolid(hitbox.x, hitbox.y+hitbox.height+1, lvlData)) {
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height+1, lvlData)) {
                return false;
            }
        }

        return true;
    }

}
