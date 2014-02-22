package src.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import src.components.Position;

/**
 * User: eptwalabha
 * Date: 21/02/14
 * Time: 19:12
 */
public class DeleteEntityOutOfScreen extends EntitySystem {

    @Mapper
    ComponentMapper<Position> positionComponentMapper;

    public DeleteEntityOutOfScreen() {
        super(Aspect.getAspectForAll(Position.class));
    }

    private void process(Entity entity) {
        Position position = positionComponentMapper.get(entity);
        if (position.x < -100) {
            world.disable(entity);
            entity.deleteFromWorld();
        }
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {

        entities = world.getManager(GroupManager.class).getEntities("deleteWhenOutOfScreen");
        for (Entity entity : entities)
            process(entity);

    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
