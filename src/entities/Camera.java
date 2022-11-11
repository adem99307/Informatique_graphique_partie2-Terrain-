package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;

	private Vector3f position = new Vector3f(0,5,0);
	private float pitch = 20;
	private float yaw ;
	private float roll;

	private Terassement player;

	public Camera(Terassement player){
		this.player = player;
	}


	public void move(){
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();

		float horizontalD = calculateHdistance();
		float verticalD = calculateVdistance();

		calculateCameraPosition(horizontalD,verticalD);

		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}


	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}


	private void calculateCameraPosition(float h, float v){
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (h * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (h * Math.cos(Math.toRadians(theta)));

		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + v;

	}



	private  float calculateHdistance(){
		return (float) (distanceFromPlayer*Math.cos(Math.toRadians(pitch)));
	}

	private  float calculateVdistance(){
		return (float) (distanceFromPlayer*Math.sin(Math.toRadians(pitch)));
	}


	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel()*0.1f;

		if ((distanceFromPlayer) >= 15 && (distanceFromPlayer ) <= 65){
			distanceFromPlayer -= zoomLevel;
		} else if (zoomLevel < 0 && distanceFromPlayer <15){
			distanceFromPlayer -= zoomLevel;
		}else if (zoomLevel > 0 && distanceFromPlayer >65){
			distanceFromPlayer -= zoomLevel;
		}
	}

	private void calculatePitch(){
		if (Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY()*0.1f;
			pitch -= pitchChange;
		}
	}

	private void calculateAngleAroundPlayer(){
		if (Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX()*0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	

}
