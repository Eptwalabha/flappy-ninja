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

        Position positionNinja = new Position(100, 250, origin);
        ninja.addComponent(positionNinja);
        ninja.addComponent(new Gravity(1400));
        ninja.addComponent(new Friction(.5f));
        ninja.addComponent(new Velocity(speed, 500));
        ninja.addComponent(new Slick2DShape(new Circle(0f, 0f, 10f)));
        ninja.addComponent(new EntityColor(Color.blue));
        ninja.addComponent(new EntityShape(positionNinja, 20, 20));
        ninja.addComponent(new EntityDepth(0));
        ninja.addComponent(new InputComponent());
        ninja.addComponent(new Score());

        world.getManager(GroupManager.class).add(ninja, Constant.Collision.PLAYER);
        return ninja;
    }

    public static Entity createBox(World world, Position origin, float posX, float posY, float width, float height) {

        Entity box = world.createEntity();
        Position positionBox = new Position(posX, posY, origin);
        box.addComponent(new Slick2DShape(new Rectangle(posX, posY, width, height)));
        box.addComponent(new EntityShape(positionBox, width, height));
        box.addComponent(positionBox);

        return box;
    }

    public static void createPipe(World world, Camera camera, float space) {

        GroupManager groupManager = world.getManager(GroupManager.class);
        space = (space > 300) ? 300 : space;
        space = (space < 75) ? 75 : space;
        float spacePosition = (float) Math.random() * (camera.screenHeight - 2 * 80 - space) + 80;

        float pipeWidth = 40;
        float endPipeHeight = 20;

        float cameraPositionY = camera.cameraPosition.getY();
        float positionYTopPipe = cameraPositionY + camera.screenHeight;
        float positionXTopPipe = camera.cameraPosition.getX() + camera.screenWidth;

        float positionYLowerPipe = cameraPositionY + spacePosition;
        float positionYTopEndPipe = cameraPositionY + space + endPipeHeight + spacePosition;
        float topPipeHeight = camera.screenHeight - (space + spacePosition);

        Position origin = camera.cameraPosition.origin;

        Entity upperPipe = createBox(world, origin, positionXTopPipe, positionYTopPipe + endPipeHeight, pipeWidth, topPipeHeight);
        addColorToEntity(upperPipe, Color.red);
        addDepthToEntity(upperPipe, 4);
        deleteThatEntityWhenOutOfBound(upperPipe, camera.cameraPosition, -100, -100, camera.screenWidth + 2 * 100, camera.screenHeight + 2 * 100);
        upperPipe.addToWorld();
        groupManager.add(upperPipe, Constant.Collision.ENVIRONMENT);

        Entity lowerPipe = createBox(world, origin, positionXTopPipe, positionYLowerPipe - endPipeHeight, pipeWidth, positionYLowerPipe);
        addColorToEntity(lowerPipe, Color.red);
        addDepthToEntity(lowerPipe, 4);
        deleteThatEntityWhenOutOfBound(lowerPipe, camera.cameraPosition, -100, -100, camera.screenWidth + 2 * 100, camera.screenHeight + 2 * 100);
        lowerPipe.addToWorld();
        groupManager.add(lowerPipe, Constant.Collision.ENVIRONMENT);

        Entity bottomOfUpperPipe = createBox(world, origin, positionXTopPipe - 5, positionYTopEndPipe, pipeWidth + 10, endPipeHeight);
        addColorToEntity(bottomOfUpperPipe, Color.red);
        addDepthToEntity(bottomOfUpperPipe, 3);
        deleteThatEntityWhenOutOfBound(bottomOfUpperPipe, camera.cameraPosition, -100, -100, camera.screenWidth + 2 * 100, camera.screenHeight + 2 * 100);
        bottomOfUpperPipe.addToWorld();
        groupManager.add(bottomOfUpperPipe, Constant.Collision.ENVIRONMENT);

        Entity topOfBottomPipe = createBox(world, origin, positionXTopPipe - 5, positionYLowerPipe, pipeWidth + 10, endPipeHeight);
        addColorToEntity(topOfBottomPipe, Color.red);
        addDepthToEntity(topOfBottomPipe, 3);
        deleteThatEntityWhenOutOfBound(topOfBottomPipe, camera.cameraPosition, -100, -100, camera.screenWidth + 2 * 100, camera.screenHeight + 2 * 100);
        topOfBottomPipe.addToWorld();
        groupManager.add(topOfBottomPipe, Constant.Collision.ENVIRONMENT);

        Entity triggerPoint = createBox(world, origin, positionXTopPipe + pipeWidth / 2 + 10, positionYLowerPipe + space, 10, space);
        addColorToEntity(triggerPoint, Color.blue);
        addDepthToEntity(triggerPoint, 3);
        deleteThatEntityWhenOutOfBound(triggerPoint, camera.cameraPosition, -100, -100, camera.screenWidth + 3 * 100, camera.screenHeight + 2 * 100);
        triggerPoint.addComponent(new Value(1));
        triggerPoint.getComponent(Slick2DShape.class).fill = false;
        triggerPoint.addToWorld();
        groupManager.add(triggerPoint, Constant.Collision.POINT);

    }

    public static void createRecordBoard(World world, Position origin, float distance) {
        Entity stick = createBox(world, origin, distance - 2, 50, 4, 30);
        addColorToEntity(stick, Color.gray);
        addDepthToEntity(stick, 2);
        stick.addToWorld();

        Entity board = createBox(world, origin, distance - 20, 80, 40, 30);
        addColorToEntity(board, Color.green);
        addDepthToEntity(board, 2);
        board.addToWorld();
    }

    public static Entity createCamera(World world, Position origin) {
        return createCamera(world, origin,  500, 500);
    }

    public static Entity createCamera(World world, Position origin, float screenWidth, float screenHeight) {

        Entity camera = world.createEntity();

        Position positionCamera = new Position();

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
        Entity floorTile = createBox(world, camera.cameraPosition.origin, tileIndex * tileWidth + alreadySpawned * tileWidth, camera.cameraPosition.origin.getY() + 2 * tileWidth / 3f, tileWidth, 50);
//        addColorToEntity(floorTile, Color.orange);
        addDepthToEntity(floorTile, 1);
        floorTile.getComponent(Slick2DShape.class).fill = false;
        float cameraMargin = 2 * tileWidth;
        deleteThatEntityWhenOutOfBound(floorTile, camera.cameraPosition, -cameraMargin, -cameraMargin, 2 * cameraMargin + camera.screenWidth, 2 * cameraMargin + camera.screenHeight);
        floorTile.addToWorld();
    }

    private static void deleteThatEntityWhenOutOfBound(Entity entity, Position origin, float offsetX, float offsetY, float width, float height) {
        Limit limit = new Limit(offsetX, offsetY, width, height);
        limit.origin = origin;
        entity.addComponent(limit);
    }

    public static void addColorToEntity(Entity entity, Color color) {
        entity.addComponent(new EntityColor(color));
    }

    public static void addDepthToEntity(Entity entity, int depth) {
        entity.addComponent(new EntityDepth(depth));
    }
}
