package src.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import src.components.Friction;
import src.components.Gravity;
import src.components.Position;
import src.components.Velocity;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:48
 */
public class BoundCollisionSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Velocity> velocityComponentMapper;
    @Mapper
    ComponentMapper<Position> positionComponentMapper;
    @Mapper
    ComponentMapper<Friction> frictionComponentMapper;

    public BoundCollisionSystem() {
        super(Aspect.getAspectForAll(Velocity.class, Gravity.class));
    }

    @Override
    protected void process(Entity entity) {

        Velocity velocity = velocityComponentMapper.get(entity);
        Position position = positionComponentMapper.getSafe(entity);
        Friction friction = frictionComponentMapper.getSafe(entity);

        if (position.y < (29 + 20) && velocity.y < 0) {
            position.y = (29 + 20);
            float restitutionRatio = 1f;
            if (friction != null)
                restitutionRatio = friction.restituctionRatio;
            velocity.y *= -1f * restitutionRatio;
            velocity.x *= restitutionRatio;
        }

    }
}
