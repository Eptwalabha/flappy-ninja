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
    public boolean fill;

    public EntityShape(Shape shape) {
        this(shape, true);
    }

    public EntityShape(Shape shape, boolean fill) {
        this.shape = shape;
        this.fill = fill;
    }

}
