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

    public Shape shape;
    public Color color;

    public EntityShape(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

}
