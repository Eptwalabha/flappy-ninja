package src.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import src.components.ToDelete;

/**
 * User: eptwalabha
 * Date: 02/03/14
 * Time: 20:14
 */
public class DeleteEntitySystem extends EntityProcessingSystem {

    public DeleteEntitySystem() {
        super(Aspect.getAspectForAll(ToDelete.class));
    }

    @Override
    protected void process(Entity entity) {
        entity.deleteFromWorld();
    }
}
