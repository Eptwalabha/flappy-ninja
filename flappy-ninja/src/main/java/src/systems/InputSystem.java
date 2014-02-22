package src.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import src.components.Death;
import src.components.Gravity;
import src.components.Velocity;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:48
 */
public class InputSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Velocity> velocityComponentMapper;

    Input input;

    public InputSystem(GameContainer gameContainer) {
        super(Aspect.getAspectForAll(Velocity.class, Gravity.class).exclude(Death.class));
        input = gameContainer.getInput();
    }

    @Override
    protected void process(Entity entity) {

        if (input.isKeyPressed(Input.KEY_SPACE) || input.isMousePressed(0)) {
            Velocity velocity = velocityComponentMapper.get(entity);
            velocity.y += 600;
        }

    }
}
