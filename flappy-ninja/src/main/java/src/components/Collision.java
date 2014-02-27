package src.components;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * User: eptwalabha
 * Date: 26/02/14
 * Time: 23:47
 */
public class Collision extends Component {

    public Entity collidingEntity;

    public Collision(Entity collidingEntity) {
        this.collidingEntity = collidingEntity;
    }
}
