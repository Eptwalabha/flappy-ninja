package src.components;

import com.artemis.Component;
import org.newdawn.slick.geom.Shape;

/**
 * User: eptwalabha
 * Date: 21/02/14
 * Time: 15:57
 */
public class Collide extends Component {

    public Shape shape;

    public Collide(Shape shape) {
        this.shape = shape;
    }
}
