package src;

/**
 * User: Eptwalabha
 * Date: 05/02/14
 * Time: 20:23
 */
public class Constant {

    public class BasicGameStateID {
        public static final int GAME = 0;
        public static final int PAUSE = 1;
    }

    public class Sprite {
        public static final int PLAYER = 0;
        public static final int PIPE = 1;
        public static final int FLOOR = 2;

    }

    public class Collision {
        public static final String PLAYER = "collision_player";
        public static final String ENVIRONMENT = "collision_environment";
        public static final String POINT = "collision_point";
    }
}
