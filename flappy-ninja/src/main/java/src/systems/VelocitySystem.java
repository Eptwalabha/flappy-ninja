package src.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import src.components.Position;
import src.components.Velocity;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:48
 */
public class VelocitySystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Velocity> velocityComponentMapper;
    @Mapper
    ComponentMapper<Position> positionComponentMapper;


    public VelocitySystem() {
        super(Aspect.getAspectForAll(Velocity.class, Position.class));
    }

    @Override
    protected void process(Entity entity) {

        Velocity velocity = velocityComponentMapper.get(entity);
        Position position = positionComponentMapper.get(entity);

        velocity.x = (velocity.x > 300) ? 300 : velocity.x;
        velocity.y = (velocity.y > 600) ? 600 : velocity.y;

        position.x += velocity.x * world.getDelta() / 1000f;
        position.y += velocity.y * world.getDelta() / 1000f;

    }
}
