package src.systems.graphic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import src.components.*;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 15:01
 */
public class DebugDrawEntityShapeSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Position> positionComponentMapper;
    @Mapper
    ComponentMapper<EntityShape> entityShapeComponentMapper;
    @Mapper
    ComponentMapper<EntityColor> entityColorComponentMapper;
    @Mapper
    ComponentMapper<EntityDepth> entityDepthComponentMapper;

    private Graphics graphics;
    private Camera camera;

    public DebugDrawEntityShapeSystem(GameContainer gameContainer, Camera camera) {
        super(Aspect.getAspectForAll(Position.class, EntityShape.class));
        this.graphics = gameContainer.getGraphics();
        this.camera = camera;
    }

    public void drawEntity(Entity entity) {

        Position position = positionComponentMapper.get(entity);
        EntityShape shape = entityShapeComponentMapper.get(entity);
        EntityColor color = entityColorComponentMapper.getSafe(entity);

        shape.shape.setLocation(position.getX() - camera.cameraPosition.getX(), camera.screenHeight - position.getY() - camera.cameraPosition.getY());
        graphics.setColor(Color.white);

        if (color != null)
            graphics.setColor(color.color);

        if (!shape.fill)
            graphics.draw(shape.shape);
        else
            graphics.fill(shape.shape);
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {

        int maxDepth = getDeepestDepthFromEntities(entities);

        for (int depth = maxDepth; depth >= 0; depth--)
            drawAllEntitiesOfThisDepth(entities, depth);
    }

    private int getDeepestDepthFromEntities(ImmutableBag<Entity> entities) {

        int maxDepth = 0;
        for (Entity entity : entities) {

            EntityDepth entityDepth = entityDepthComponentMapper.getSafe(entity);
            if (entityDepth == null)
                continue;

            if (entityDepth.depth > maxDepth)
                maxDepth = entityDepth.depth;
        }
        return maxDepth;
    }

    private void drawAllEntitiesOfThisDepth(ImmutableBag<Entity> entities, int depth) {
        for (Entity entity : entities)
            if (doesEntityHaveTheSameDepth(entity, depth))
                drawEntity(entity);
    }

    private boolean doesEntityHaveTheSameDepth(Entity entity, int depth) {
        EntityDepth entityDepth = entityDepthComponentMapper.getSafe(entity);
        return entityDepth == null && depth == 0 || entityDepth != null && entityDepth.depth == depth;
    }


    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
