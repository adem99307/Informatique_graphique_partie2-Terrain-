package entities;

import com.badlogic.gdx.math.Vector2;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Affichage;
import HeightFields.HeightField;

public class Terassement extends Node {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 17;

    private static Vector2 veloVector;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;

    private boolean inAir = false;

    public Terassement(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void aplatir(HeightField heightField){
        checkInputs();

        super.increaseRotation(0, currentTurnSpeed* Affichage.getFrameTimeSeconds(),0);

        float distance = currentSpeed* Affichage.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dy = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));

        super.increasePosition(dx,0,dy);

        upwardSpeed += GRAVITY* Affichage.getFrameTimeSeconds();
        super.increasePosition(0, upwardSpeed* Affichage.getFrameTimeSeconds(), 0);

        float terrainHeight = heightField.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);

        //TODO collisionshit is here
        if (super.getPosition().y < terrainHeight){
            upwardSpeed = 0;
            inAir = false;
            super.getPosition().y = terrainHeight;
        }
    }
    private void sauter(){
        if (!inAir) {
            this.upwardSpeed = JUMP_POWER;
            this.inAir = true;
        }
    }

    private void checkInputs(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = -RUN_SPEED;
        }else{
            this.currentSpeed = 0;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed = -TURN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed = TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            sauter();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){

            System.out.println("currentSpeed = " + currentSpeed);
        }
    }


    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public static Vector2 getVeloVector() {
        return veloVector;
    }

    public static void setVeloVector(Vector2 veloVector) {
        Terassement.veloVector = veloVector;
    }
    public void setPosition(Vector3f vector){
        super.setPosition(vector);
    }
}
