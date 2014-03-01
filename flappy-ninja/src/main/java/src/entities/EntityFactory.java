package src.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import src.Constant;
import src.components.*;
import src.components.Camera;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:33
 */
public class EntityFactory {

    public static Entity createNinja(World world, Position origin, float speed) {

        Entity ninja = world.createEntity();

        ninja.addComponent(new Position(100, 250, origin));
        ninja.addComponent(new Gravity(1400));
        ninja.addComponent(new Friction(.5f));
        ninja.addComponent(new Velocity(speed, 500));
        ninja.addComponent(new EntityShape(new Circle(0f, 0f, 10f), Color.blue, 0));
        ninja.addComponent(new InputComponent());
        ninja.addComponent(new Score());

        world.getManager(GroupManager.class).add(ninja, Constant.Collision.PLAYER);
        return ninja;
    }

    public static Entity createBox(World world, Position origin, float posX, float posY, float width, float height, Color color, int depth) {

        Entity box = world.createEntity();
        Position positionBox = new Position(posX, posY, origin);
        box.addComponent(new EntityShape(new Rectangle(posX, posY, width, height), color, depth));
        box.addComponent(positionBox);

        return box;
    }

    public static void createPipe(World world, Camera camera, float space) {

        GroupManager groupManager = world.getManager(GroupManager.class);
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
                pipeWidth, 80 + spacePosition, Color.red, 4);
        deleteThatEntityWhenOutOfBound(upperPipe, camera.cameraPosition,
                -100, -100, camera.screenWidth + 2 * 100, camera.screenHeight + 2 * 100);
        upperPipe.addToWorld();
        groupManager.add(upperPipe, Constant.Collision.ENVIRONMENT);

        Entity lowerPipe = createBox(world, origin,
                spawnPositionX, spawnPositionY - (80 + spacePosition + space),
                pipeWidth, 500 - (80 + spacePosition + space), Color.red, 4);
        deleteThatEntityWhenOutOfBound(lowerPipe, camera.cameraPosition,
                -100, -100, camera.screenWidth + 2 * 100, camera.screenHeight + 2 * 100);
        lowerPipe.addToWorld();
        groupManager.add(lowerPipe, Constant.Collision.ENVIRONMENT);

        Entity bottomOfLowerPipe = createBox(world, origin,
                spawnPositionX - 4, spawnPositionY - (80 + spacePosition) + 20,
                pipeWidth + 8, 20, Color.red, 3);
        deleteThatEntityWhenOutOfBound(bottomOfLowerPipe, camera.cameraPosition,
                -100, -100, camera.screenWidth + 2 * 100, camera.screenHeight + 2 * 100);
        bottomOfLowerPipe.addToWorld();
        groupManager.add(bottomOfLowerPipe, Constant.Collision.ENVIRONMENT);

        Entity topOfBottomPipe = createBox(world, origin,
                spawnPositionX - 4, spawnPositionY - (80 + spacePosition + space),
                pipeWidth + 8, 20, Color.red, 3);
        deleteThatEntityWhenOutOfBound(topOfBottomPipe, camera.cameraPosition,
                -100, -100, camera.screenWidth + 2 * 100, camera.screenHeight + 2 * 100);
        topOfBottomPipe.addToWorld();
        groupManager.add(topOfBottomPipe, Constant.Collision.ENVIRONMENT);

        Entity triggerPoint = createBox(world, origin,
                spawnPositionX + pipeWidth, spawnPositionY - 80 - spacePosition,
                10, space, Color.blue, 3);
        deleteThatEntityWhenOutOfBound(triggerPoint, camera.cameraPosition,
                -100, -100, camera.screenWidth + 3 * 100, camera.screenHeight + 2 * 100);
        triggerPoint.addComponent(new Value(1));
        triggerPoint.addToWorld();
        groupManager.add(triggerPoint, Constant.Collision.POINT);

    }

    public static void createRecordBoard(World world, Position origin, float time) {
        Entity stick = createBox(world, origin, 300 * time / 1000 - 2 + 100, 50, 4, 30, Color.gray, 2);
        stick.addToWorld();
        Entity board = createBox(world, origin, 300 * time / 1000 - 20 + 100, 80, 40, 30, Color.green, 2);
        board.addToWorld();
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

        int numberOfTilesToSpawn = (int) Math.floor((camera.cameraPosition.getX() + camera.screenWidth + 2 * tileWidth) / tileWidth) - alreadySpawned;

        for (int i = 0; i < numberOfTilesToSpawn; i++)
            createFloorTile(world, camera, tileWidth, i, alreadySpawned);

        return numberOfTilesToSpawn;
    }

    private static void createFloorTile(World world, Camera camera, float tileWidth, int tileIndex, int alreadySpawned) {
        Entity floorTile = createBox(world, camera.cameraPosition.origin, tileIndex * tileWidth + alreadySpawned * tileWidth, camera.cameraPosition.origin.getY() + 2 * tileWidth / 3f, tileWidth, 50, Color.orange, 1);
        float cameraMargin = 2 * tileWidth;
        deleteThatEntityWhenOutOfBound(floorTile, camera.cameraPosition,
                -cameraMargin, -cameraMargin,
                2 * cameraMargin + camera.screenWidth, 2 * cameraMargin + camera.screenHeight);
        floorTile.addToWorld();
    }

    private static void deleteThatEntityWhenOutOfBound(Entity entity, Position origin, float offsetX, float offsetY, float width, float height) {
        Limit limit = new Limit(offsetX, offsetY, width, height);
        limit.origin = origin;
        entity.addComponent(limit);
    }
}
