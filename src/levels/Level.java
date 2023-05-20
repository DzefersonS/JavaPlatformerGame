package levels;

import entities.Goblin;
import main.Game;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utils.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.HelpMethods.*;

public class Level {

    private BufferedImage image;
    private int[][] lvlData;
    private ArrayList<Goblin> goblins;
    private ArrayList<Spike> spikes;
    private ArrayList<GameContainer> containers;
    private ArrayList<Potion> potions;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;
    private Game game;

    public Level(BufferedImage image, Game game) {
        this.image = image;
        this.game = game;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        calculacteLvlOffsets();
        calculatePlayerSpawnPoint();
    }

    private void createSpikes() {
        spikes = HelpMethods.GetSpikes(image);
    }

    private void createContainers() {
        containers = HelpMethods.GetContainers(image);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(image);
    }

    private void calculatePlayerSpawnPoint() {
        playerSpawn = GetPlayerSpawn(image);
    }

    private void calculacteLvlOffsets() {
        lvlTilesWide = image.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
    }

    private void createEnemies() {
        goblins = GetGoblins(image, game);
    }

    private void createLevelData() {
        lvlData = GetLevelData(image);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getMaxLvlOffsetX() {
        return maxLvlOffsetX;
    }

    public ArrayList<Goblin> getGoblins() {
        return goblins;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }
}
