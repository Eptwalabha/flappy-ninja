package src.systems;

import com.artemis.*;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import org.newdawn.slick.Color;
import src.components.CollisionWithPlayer;
import src.components.Death;
import src.components.EntityShape;
import src.components.Velocity;
import src.systems.interfaces.CollisionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:48
 */
public class CollisionSystem extends EntitySystem {

    @Mapper
    ComponentMapper<EntityShape> entityShapeComponentMapper;

    private Entity player;
    private List<CollisionListener> collisionListeners;

    public CollisionSystem() {
        super(Aspect.getAspectForAll(EntityShape.class, CollisionWithPlayer.class));
        clearAllCollisionListener();
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    private boolean checkCollision(Entity entity, EntityShape playerShape) {

        EntityShape entityShape = entityShapeComponentMapper.get(entity);

        if (entityShape.shape.intersects(playerShape.shape)) {
            playerShape.color = Color.pink;

            if (player.getComponent(ComponentType.getTypeFor(Death.class)) == null) {
                triggerEventCollision();
                player.addComponent(new Death());
                Velocity velocity = player.getComponent(Velocity.class);
                if (velocity != null)
                    velocity.x += 200;
                player.changedInWorld();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {

        EntityShape playerShape = player.getComponent(EntityShape.class);

        for (Entity entity : entities) {
            if (checkCollision(entity, playerShape))
                break;
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    public void clearAllCollisionListener() {
        collisionListeners = new ArrayList<CollisionListener>();
    }

    public void addNewCollisionListener(CollisionListener collisionListener) {
        collisionListeners.add(collisionListener);
    }

    private void triggerEventCollision() {
        for (CollisionListener collisionListener : collisionListeners)
            collisionListener.hasCollide(player);
    }
}
