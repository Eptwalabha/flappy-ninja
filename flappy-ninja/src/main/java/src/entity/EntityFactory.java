package src.entity;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import src.components.*;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:33
 */
public class EntityFactory {

    public static Entity createNinja(World world) {

        Entity ninja = world.createEntity();

        ninja.addComponent(new Position(100, 250));
        ninja.addComponent(new Gravity(1400));
        ninja.addComponent(new Friction(.5f));
        ninja.addComponent(new Velocity((float)(Math.random() * 0), 500));
        ninja.addComponent(new EntityShape(new Circle(0f, 0f, 10f), Color.blue));
        ninja.addComponent(new InputComponent());

        return ninja;
    }

    public static Entity createBox(World world, float posX, float posY, float width, float height, Color color) {

        Entity box = world.createEntity();
        Position positionBox = new Position(posX, posY);
        box.addComponent(new EntityShape(new Rectangle(posX, posY, width, height), color));
        box.addComponent(positionBox);

        return box;
    }

    public static void createPipe(World world, float space) {

        space = (space > 300) ? 300 : space;
        space = (space < 75) ? 75 : space;
        float spacePosition = (float) Math.random() * (500 - 2 * 80 - space);
        float pipeWidth = 40 ;
        float speed = 300;

        Entity upperPipe = createBox(world, 600, 500, pipeWidth, 80 + spacePosition, Color.red);
        makeThatEntityMoving(upperPipe, -speed, 0);
        makeThatEntitySolid(upperPipe);
        addThatEntityToAGroup(world, upperPipe, "deleteWhenOutOfScreen");
        upperPipe.addToWorld();

        Entity lowerPipe = createBox(world, 600, 500 - (80 + spacePosition + space), pipeWidth, 500 - (80 + spacePosition + space), Color.red);
        makeThatEntityMoving(lowerPipe, -speed, 0);
        makeThatEntitySolid(lowerPipe);
        addThatEntityToAGroup(world, lowerPipe, "deleteWhenOutOfScreen");
        lowerPipe.addToWorld();

        Entity bottomOfLowerPipe = createBox(world, 596, 500 - (80 + spacePosition) + 20, pipeWidth + 8, 20, Color.red);
        makeThatEntityMoving(bottomOfLowerPipe, -speed, 0);
        makeThatEntitySolid(bottomOfLowerPipe);
        addThatEntityToAGroup(world, bottomOfLowerPipe, "deleteWhenOutOfScreen");
        bottomOfLowerPipe.addToWorld();

        Entity topOfBottomPipe = createBox(world, 596, 500 - (80 + spacePosition + space), pipeWidth + 8, 20, Color.red);
        makeThatEntityMoving(topOfBottomPipe, -speed, 0);
        makeThatEntitySolid(topOfBottomPipe);
        addThatEntityToAGroup(world, topOfBottomPipe, "deleteWhenOutOfScreen");
        topOfBottomPipe.addToWorld();

        Entity triggerPoint = createBox(world, 600 + pipeWidth, 500 - 80 - spacePosition, 10, space, Color.blue);
        makeThatEntityMoving(triggerPoint, -speed, 0);
        addThatEntityToAGroup(world, triggerPoint, "addPoint");
        triggerPoint.addToWorld();

    }

    public static void makeThatEntityMoving(Entity entity, float speedX, float speedY) {
        entity.addComponent(new Velocity(speedX, speedY));
    }

    public static void makeThatEntitySolid(Entity entity) {
        entity.addComponent(new CollisionWithPlayer());
    }

    public static void addThatEntityToAGroup(World world, Entity entity, String groupName) {
        world.getManager(GroupManager.class).add(entity, groupName);
    }

    public static void createRecordBoard(World world, float time) {
        Entity board = createBox(world, 300 * time / 1000 - 20 + 100, 80, 40, 30, Color.orange);
        makeThatEntityMoving(board, -300, 0);
        makeThatEntitySolid(board);
        addThatEntityToAGroup(world, board, "deleteWhenOutOfScreen");

        Entity stick = createBox(world, 300 * time / 1000 - 2 + 100, 50, 4, 30, Color.orange);
        makeThatEntityMoving(stick, -300, 0);
        makeThatEntitySolid(stick);
        addThatEntityToAGroup(world, stick, "deleteWhenOutOfScreen");
        board.addToWorld();
        stick.addToWorld();
    }
}
