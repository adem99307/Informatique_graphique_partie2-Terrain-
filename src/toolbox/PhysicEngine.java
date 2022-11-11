package toolbox;

import com.badlogic.gdx.math.Vector2;
import org.mariuszgromada.math.mxparser.Function;

public class PhysicEngine {

    //some physic engine uses the same method t ocalculate the speed and the gravity of the ball on the curve
    static final float STEPSIZE = 0.01f;
    static final float GRAVITY = 9.81f;
    static float friction;

    public static float getENDINGSPEED() {
        return ENDINGSPEED;
    }

    static final float ENDINGSPEED = 0.01f;
    static final float BALLWEIGHT = 1;
    static final float DRAGCOEFFICIENT = 0.4f;
    //assumed temperature 20C, sealevel [kg/m^3]
    static final float AIRDENSITY = 1.2041f;


    public static float getSpeed(Vector2 velocity){
        return (float) Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2));
    }


    public static Vector2 curveCalculator(Vector2 ballPos){
        Function mapFunction = new Function("f(x,y) = sin(x) + cos(y) + 2");
        Vector2 xVec = new Vector2(ballPos.x+ ENDINGSPEED,ballPos.y);
        float nX = (float) ( mapFunction.calculate( xVec.x, xVec.y ) - mapFunction.calculate( ballPos.x, ballPos.y ) )/ ENDINGSPEED;
        Vector2 yVec = new Vector2(ballPos.x,ballPos.y + ENDINGSPEED);
        float nY = (float) (mapFunction.calculate(yVec.x, yVec.y ) - mapFunction.calculate( ballPos.x, ballPos.y ) )/ ENDINGSPEED;
        return new Vector2( nX, nY );
    }

    public static Vector2 dragForce() {
        Vector2 drag = new Vector2();
        double crossSectionArea = 0.4267 * 0.4267 * Math.PI;
        Vector2 windDirection = new Vector2(0f,0f);
        drag.x = (float) (DRAGCOEFFICIENT * AIRDENSITY * crossSectionArea * getSpeed(windDirection) * windDirection.x);
        drag.y = (float) (DRAGCOEFFICIENT * AIRDENSITY * crossSectionArea * getSpeed(windDirection) * windDirection.y);
        return drag;
    }

    public static Vector2 dragAcceleration() {
        Vector2 acceleration = dragForce();
        acceleration.x = -(acceleration.x / BALLWEIGHT);
        acceleration.y = -(acceleration.y / BALLWEIGHT);
        return acceleration;
    }
}
