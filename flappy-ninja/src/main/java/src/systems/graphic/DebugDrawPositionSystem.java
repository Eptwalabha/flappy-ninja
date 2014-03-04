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
public class DebugDrawPositionSystem extends EntityProcessingSystem {

    private Graphics graphics;
    @Mapper
    ComponentMapper<Position> positionComponentMapper;

    private Camera camera;

    public DebugDrawPositionSystem(GameContainer gameContainer, Camera camera) {
        super(Aspect.getAspectForAll(Position.class));
        this.graphics = gameContainer.getGraphics();
        this.camera = camera;
    }

    @Override
    protected void begin() {

        graphics.setColor(Color.green);
        graphics.setLineWidth(2);
    }

    @Override
    protected void process(Entity entity) {

        Position position = positionComponentMapper.get(entity);

        graphics.drawLine(position.getX() - camera.cameraPosition.getX() - 5,
                camera.screenHeight - position.getY() - camera.cameraPosition.getY(),
                (position.getX() - camera.cameraPosition.getX()) + 5,
                camera.screenHeight - position.getY() - camera.cameraPosition.getY());

        graphics.drawLine(position.getX() - camera.cameraPosition.getX(),
                camera.screenHeight - position.getY() - camera.cameraPosition.getY() - 5,
                (position.getX() - camera.cameraPosition.getX()),
                camera.screenHeight - position.getY() - camera.cameraPosition.getY() + 5);
    }

    @Override
    protected void end() {

        graphics.setLineWidth(1);
    }
}
