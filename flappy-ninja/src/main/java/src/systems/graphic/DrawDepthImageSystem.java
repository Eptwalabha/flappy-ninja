package src.systems.graphic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import src.components.Camera;
import src.components.Position;
import src.components.Texture;
import src.components.Transformation;

import java.util.HashMap;
import java.util.List;

/**
 * User: eptwalabha
 * Date: 23/02/14
 * Time: 00:20
 */
public class DrawDepthImageSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Texture> textureComponentMapper;
    @Mapper
    ComponentMapper<Position> positionComponentMapper;

    private Camera camera;

    public DrawDepthImageSystem(Camera camera) {
        super(Aspect.getAspectForAll(Texture.class, Position.class));
        this.camera = camera;
    }

    private void drawEntity(Entity entity) {

        Texture texture = textureComponentMapper.get(entity);
        Position position = positionComponentMapper.get(entity);

        Image image = texture.image;

        image.draw(position.getX() - camera.cameraPosition.getX(), camera.screenHeight - (position.getY() - camera.cameraPosition.getY()));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {

        int maxDepth = 0;

        for (Entity entity : entities) {
            int entityDepth = textureComponentMapper.get(entity).depth;
            if (entityDepth > maxDepth)
                maxDepth = entityDepth;
        }

        for (int depth = maxDepth; depth >= 0; depth--) {
            for (Entity entity : entities) {
                if (textureComponentMapper.get(entity).depth == depth)
                    drawEntity(entity);
            }
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
