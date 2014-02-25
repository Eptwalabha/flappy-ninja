package src.entity;

import com.artemis.Entity;
import com.artemis.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import src.components.*;
import src.components.Camera;
import src.utils.SpriteGUI;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:33
 */
public class EntityFactory {

    public static Entity createNinja(World world, Position origin, float speed, SpriteGUI spriteGUI) {

        Entity ninja = world.createEntity();

        ninja.addComponent(new Position(100, 250, origin));
        ninja.addComponent(new Gravity(1400));
        ninja.addComponent(new Friction(.5f));
        ninja.addComponent(new Velocity(speed, 500));
        ninja.addComponent(new EntityShape(new Circle(0f, 0f, 10f), Color.blue));
        ninja.addComponent(new InputComponent());
        ninja.addComponent(new Texture(spriteGUI.getSpriteAt(0, 0, 20), 0));

        return ninja;
    }

    public static Entity createBox(World world, Position origin, float posX, float posY, float width, float height, Color color) {

        Entity box = world.createEntity();
        Position positionBox = new Position(posX, posY, origin);
        box.addComponent(new EntityShape(new Rectangle(posX, posY, width, height), color));
        box.addComponent(positionBox);

        return box;
    }

    public static void createPipe(World world, Camera camera, SpriteGUI spriteGUI, float space) {

        space = (space > 300) ? 300 : space;
        space = (space < 75) ? 75 : space;
        float spacePosition = (float) Math.random() * (camera.screenHeight - 2 * 80 - space);
        float pipeWidth = 40 ;

        float cameraPositionX = camera.cameraPosition.getX();
        float cameraPositionY = camera.cameraPosition.getY();
        float cameraWidth = camera.screenWidth;
        float cameraHeight = camera.screenHeight;
        float spawnPositionX = cameraPositionX + cameraWidth + 100;
        float spawnPositionY = cameraPositionY + cameraHeight;
        Position origin = camera.cameraPosition.origin;

        Entity upperPipe = createBox(world, origin,
                spawnPositionX, spawnPositionY,
                pipeWidth, 80 + spacePosition, Color.red);

        makeThatEntitySolid(upperPipe);
        upperPipe.addComponent(new Texture(spriteGUI.getSpriteAt(1, 0, (int) pipeWidth, (int) (80 + spacePosition)), 2));
        upperPipe.addToWorld();

        Entity lowerPipe = createBox(world, origin,
                spawnPositionX, spawnPositionY - (80 + spacePosition + space),
                pipeWidth, 500 - (80 + spacePosition + space), Color.red);

        makeThatEntitySolid(lowerPipe);
        lowerPipe.addComponent(new Texture(spriteGUI.getSpriteAt(1, 0, (int) pipeWidth, (int) (500 - (80 + spacePosition + space))), 2));
        lowerPipe.addToWorld();

        Entity bottomOfLowerPipe = createBox(world, origin,
                spawnPositionX - 4, spawnPositionY - (80 + spacePosition) + 20,
                pipeWidth + 8, 20, Color.red);

        makeThatEntitySolid(bottomOfLowerPipe);
        bottomOfLowerPipe.addComponent(new Texture(spriteGUI.getSpriteAt(2, 0, (int) pipeWidth + 8, 20), 2));
        bottomOfLowerPipe.addToWorld();

        Entity topOfBottomPipe = createBox(world, origin,
                spawnPositionX - 4, spawnPositionY - (80 + spacePosition + space),
                pipeWidth + 8, 20, Color.red);

        makeThatEntitySolid(topOfBottomPipe);
        topOfBottomPipe.addComponent(new Texture(spriteGUI.getSpriteAt(2, 0, (int) pipeWidth + 8, 20), 2));
        topOfBottomPipe.addToWorld();

        Entity triggerPoint = createBox(world, origin,
                spawnPositionX + pipeWidth, spawnPositionY - 80 - spacePosition,
                10, space, Color.blue);
        triggerPoint.addToWorld();

    }

    public static void makeThatEntitySolid(Entity entity) {
        entity.addComponent(new CollisionWithPlayer());
    }

    public static void createRecordBoard(World world, Position origin, SpriteGUI spriteGUI, float time) {
        Entity board = createBox(world, origin, 300 * time / 1000 - 20 + 100, 80, 40, 30, Color.orange);
        makeThatEntitySolid(board);
        board.addComponent(new Texture(spriteGUI.getSpriteAt(3, 0, 40, 30), 0));

        Entity stick = createBox(world, origin, 300 * time / 1000 - 2 + 100, 50, 4, 30, Color.orange);
        makeThatEntitySolid(stick);
        stick.addComponent(new Texture(spriteGUI.getSpriteAt(4, 0, 4, 30), 1));
        board.addToWorld();
        stick.addToWorld();
    }

    public static Entity createCamera(World world, Position origin) {
        return createCamera(world, origin,  500, 500);
    }

    public static Entity createCamera(World world, Position origin, float screenWidth, float screenHeight) {

        Entity camera = world.createEntity();

        Position positionCamera = null;

        if (origin != null)
            positionCamera = new Position(origin.getX(), origin.getY());

        positionCamera.origin = origin;
        camera.addComponent(positionCamera);
        camera.addComponent(new Camera(positionCamera, screenWidth, screenHeight));
        return camera;
    }

    public static int createFloor(World world, Camera camera, float tileWidth, int alreadySpawned) {

        float cameraScreenWidth = camera.screenWidth;
        float positionCameraX = camera.cameraPosition.getX();

        int numberOfTilesToSpawn = (int) Math.floor((positionCameraX + cameraScreenWidth + 2 * tileWidth) / tileWidth) - alreadySpawned;

        for (int i = 0; i < numberOfTilesToSpawn; i++) {

            Entity floorTile = createBox(world, camera.cameraPosition.origin, i * tileWidth + alreadySpawned * tileWidth, 50, tileWidth, 50, Color.orange);
            makeThatEntitySolid(floorTile);
            Limit limit = new Limit(-2 * tileWidth, -2 * tileWidth, 4 * tileWidth + cameraScreenWidth, 4 * tileWidth + camera.screenHeight);
            limit.origin = camera.cameraPosition;
            floorTile.addComponent(limit);
            floorTile.addToWorld();
        }

        return numberOfTilesToSpawn;
    }
}
