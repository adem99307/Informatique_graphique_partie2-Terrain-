package toolbox;

import com.badlogic.gdx.math.Vector2;

//Euler method physic engine and extends that class because it has useful method
public class EulerMethod extends PhysicEngine{

    //calculates the new positon of the ball thanks to the euler method
    public static Vector2 newPosition(Vector2 currentPos, Vector2 velocity){

        float x = currentPos.x + (STEPSIZE*velocity.x);
        float y = currentPos.y + (STEPSIZE*velocity.y);

        Vector2 newPosition = new Vector2(x,y);

        return newPosition;
    }

    //calculates the new velocity of the ball thanks to the euler method
    public static Vector2 newVelocity(Vector2 currentPos, Vector2 velocity){

        //first calculates the gravity of the ball on the curve
        Vector2 accDiff = curveCalculator(currentPos);

        //then the speed from the velocity
        float speed = getSpeed(velocity);


        //calculates acceleration of the ball thanks to the gracity, the friction, the previous velocity and the speed
        Vector2 acceleration = new Vector2(-GRAVITY * ((accDiff.x) + friction * (velocity.x / speed))
                + PhysicEngine.dragAcceleration().x, -GRAVITY * ((accDiff.y) + friction * (velocity.y / speed))
                + PhysicEngine.dragAcceleration().y);

        //and now we have the new velocity of the ball
        float x = velocity.x + (STEPSIZE*acceleration.x/BALLWEIGHT);
        float y = velocity.y + (STEPSIZE*acceleration.y/BALLWEIGHT);


        Vector2 newVel = new Vector2(x,y);
        return newVel;
    }
}