package src.systems.graphic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import src.components.Camera;
import src.components.Position;
import src.components.Velocity;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 15:01
 */
public class DebugDrawVelocitySystem extends EntityProcessingSystem {

    private Graphics graphics;
    @Mapper
    ComponentMapper<Position> positionComponentMapper;
    @Mapper
    ComponentMapper<Velocity> velocityComponentMapper;

    private Camera camera;

    public DebugDrawVelocitySystem(GameContainer gameContainer, Camera camera) {
        super(Aspect.getAspectForAll(Position.class, Velocity.class));
        this.graphics = gameContainer.getGraphics();
        this.camera = camera;
    }

    @Override
    protected void process(Entity entity) {

        Position position = positionComponentMapper.get(entity);
        Velocity velocity = velocityComponentMapper.get(entity);

        graphics.setColor(Color.green);
        graphics.drawLine(position.getX() - camera.cameraPosition.getX(),
                camera.screenHeight - position.getY() - camera.cameraPosition.getY(),
                (position.getX() - camera.cameraPosition.getX()) + velocity.x / 10,
                camera.cameraPosition.getY() + camera.screenHeight - (position.y + velocity.y / 10));
    }
}
