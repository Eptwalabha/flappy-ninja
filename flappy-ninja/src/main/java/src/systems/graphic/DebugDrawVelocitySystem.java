package src.systems.graphic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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

    public DebugDrawVelocitySystem(GameContainer gameContainer) {
        super(Aspect.getAspectForAll(Position.class, Velocity.class));
        this.graphics = gameContainer.getGraphics();
    }

    @Override
    protected void process(Entity entity) {

        Position position = positionComponentMapper.get(entity);
        Velocity velocity = velocityComponentMapper.get(entity);

        graphics.setColor(Color.green);
        graphics.drawLine(position.x, 500 - position.y, position.x + velocity.x / 10, 500 - (position.y + velocity.y / 10));
    }
}
