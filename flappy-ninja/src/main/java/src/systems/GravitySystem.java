package src.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import src.components.Gravity;
import src.components.Velocity;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:48
 */
public class GravitySystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Velocity> velocityComponentMapper;
    @Mapper
    ComponentMapper<Gravity> gravityComponentMapper;


    public GravitySystem() {
        super(Aspect.getAspectForAll(Velocity.class, Gravity.class));
    }

    @Override
    protected void process(Entity entity) {

        Velocity velocity = velocityComponentMapper.get(entity);
        Gravity gravity = gravityComponentMapper.get(entity);
        velocity.x -= gravity.x * world.getDelta() / 1000;
        velocity.y -= gravity.y * world.getDelta() / 1000;
    }
}
