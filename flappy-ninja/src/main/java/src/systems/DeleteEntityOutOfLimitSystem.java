package src.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import src.components.Limit;
import src.components.Position;
import src.components.ToDelete;

/**
 * User: eptwalabha
 * Date: 22/02/14
 * Time: 15:22
 */
public class DeleteEntityOutOfLimitSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Position> positionComponentMapper;
    @Mapper
    ComponentMapper<Limit> limitComponentMapper;

    public DeleteEntityOutOfLimitSystem() {
        super (Aspect.getAspectForAll(Position.class, Limit.class).exclude(ToDelete.class));
    }

    @Override
    protected void process(Entity entity) {

        Position position = positionComponentMapper.get(entity);
        Limit limit = limitComponentMapper.get(entity);

        if (!limit.isPositionInsideLimit(position)) {
            entity.addComponent(new ToDelete());
            entity.changedInWorld();
        }
    }
}
