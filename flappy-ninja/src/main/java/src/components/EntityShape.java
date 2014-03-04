package src.components;

import com.artemis.Component;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

/**
 * User: eptwalabha
 * Date: 21/02/14
 * Time: 00:22
 */
public class EntityShape extends Component {

    public Position position;
    public float width;
    public float height;

    public EntityShape(Position position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(EntityShape entityShape) {

        float xA = position.getX();
        float yA = position.getY();
        float xB = entityShape.position.getX();
        float yB = entityShape.position.getY();

        return  (xA + width >= xB) && (xA <= xB + entityShape.width) &&
                (yA - height <= yB) && (yA >= yB - entityShape.height);
    }
}
