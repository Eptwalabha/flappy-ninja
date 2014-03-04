package src.components;

import com.artemis.Component;
import org.newdawn.slick.geom.Shape;

/**
 * User: eptwalabha
 * Date: 04/03/14
 * Time: 23:56
 */
public class Slick2DShape extends Component {

    public Shape shape;
    public boolean fill;

    public Slick2DShape(Shape shape) {
        this(shape, true);
    }

    public Slick2DShape(Shape shape, boolean fill) {
        this.shape = shape;
        this.fill = fill;
    }
}
