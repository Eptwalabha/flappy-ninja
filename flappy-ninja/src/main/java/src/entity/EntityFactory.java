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

    public static Entity createNinja(World world, SpriteGUI spriteGUI) {

        Entity ninja = world.createEntity();

        ninja.addComponent(new Position(100, 250));
        ninja.addComponent(new Gravity(1400));
        ninja.addComponent(new Friction(.5f));
        ninja.addComponent(new Velocity((float)(Math.random() * 0), 500));
        ninja.addComponent(new EntityShape(new Circle(0f, 0f, 10f), Color.blue));
        ninja.addComponent(new InputComponent());
        ninja.addComponent(new Texture(spriteGUI.getSpriteAt(0, 0, 20), 0));

        return ninja;
    }

    public static Entity createBox(World world, float posX, float posY, float width, float height, Color color) {

        Entity box = world.createEntity();
        Position positionBox = new Position(posX, posY);
        box.addComponent(new EntityShape(new Rectangle(posX, posY, width, height), color));
        box.addComponent(positionBox);

        return box;
    }

    public static void createPipe(World world, SpriteGUI spriteGUI, float space) {

        space = (space > 300) ? 300 : space;
        space = (space < 75) ? 75 : space;
        float spacePosition = (float) Math.random() * (500 - 2 * 80 - space);
        float pipeWidth = 40 ;
        float speed = 300;

        Entity upperPipe = createBox(world, 600, 500, pipeWidth, 80 + spacePosition, Color.red);
        makeThatEntityMoving(upperPipe, -speed, 0);
        makeThatEntitySolid(upperPipe);
        upperPipe.addComponent(new Texture(spriteGUI.getSpriteAt(1, 0, (int) pipeWidth, (int) (80 + spacePosition)), 2));
        upperPipe.addToWorld();

        Entity lowerPipe = createBox(world, 600, 500 - (80 + spacePosition + space), pipeWidth, 500 - (80 + spacePosition + space), Color.red);
        makeThatEntityMoving(lowerPipe, -speed, 0);
        makeThatEntitySolid(lowerPipe);
        lowerPipe.addComponent(new Texture(spriteGUI.getSpriteAt(1, 0, (int) pipeWidth, (int) (500 - (80 + spacePosition + space))), 2));
        lowerPipe.addToWorld();

        Entity bottomOfLowerPipe = createBox(world, 596, 500 - (80 + spacePosition) + 20, pipeWidth + 8, 20, Color.red);
        makeThatEntityMoving(bottomOfLowerPipe, -speed, 0);
        makeThatEntitySolid(bottomOfLowerPipe);
        bottomOfLowerPipe.addComponent(new Texture(spriteGUI.getSpriteAt(2, 0, (int) pipeWidth + 8, 20), 2));
        bottomOfLowerPipe.addToWorld();

        Entity topOfBottomPipe = createBox(world, 596, 500 - (80 + spacePosition + space), pipeWidth + 8, 20, Color.red);
        makeThatEntityMoving(topOfBottomPipe, -speed, 0);
        makeThatEntitySolid(topOfBottomPipe);
        topOfBottomPipe.addComponent(new Texture(spriteGUI.getSpriteAt(2, 0, (int) pipeWidth + 8, 20), 2));
        topOfBottomPipe.addToWorld();

        Entity triggerPoint = createBox(world, 600 + pipeWidth, 500 - 80 - spacePosition, 10, space, Color.blue);
        makeThatEntityMoving(triggerPoint, -speed, 0);
        triggerPoint.addToWorld();

    }

    public static void makeThatEntityMoving(Entity entity, float speedX, float speedY) {
        entity.addComponent(new Velocity(speedX, speedY));
    }

    public static void makeThatEntitySolid(Entity entity) {
        entity.addComponent(new CollisionWithPlayer());
    }

    public static void createRecordBoard(World world, SpriteGUI spriteGUI, float time) {
        Entity board = createBox(world, 300 * time / 1000 - 20 + 100, 80, 40, 30, Color.orange);
        makeThatEntityMoving(board, -300, 0);
        makeThatEntitySolid(board);
        board.addComponent(new Texture(spriteGUI.getSpriteAt(3, 0, 40, 30), 0));

        Entity stick = createBox(world, 300 * time / 1000 - 2 + 100, 50, 4, 30, Color.orange);
        makeThatEntityMoving(stick, -300, 0);
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
        Position positionCamera = new Position(0, 0);
        positionCamera.origin = origin;
        camera.addComponent(positionCamera);
        camera.addComponent(new Camera(positionCamera, screenWidth, screenHeight));
        return camera;
    }
}
