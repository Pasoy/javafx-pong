package application;

import java.text.DecimalFormat;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.media.AudioClip;

public class PongController {
	@FXML
	private Rectangle background;
	@FXML
	private Rectangle topWall;
	@FXML
	private Rectangle leftWall;
	@FXML
	private Rectangle bottomWall;
	@FXML
	private Rectangle rightWall;
	@FXML
	private Rectangle ball;
	@FXML
	private Line line;
	@FXML
	private Text leftScore;
	@FXML
	private Text rightScore;
	@FXML
	private Rectangle leftRacket;
	@FXML
	private Rectangle rightRacket;
	@FXML
	private Text gameover;

	DoubleProperty leftRacketY = new SimpleDoubleProperty();
	DoubleProperty rightRacketY = new SimpleDoubleProperty();
	BooleanProperty wPressed = new SimpleBooleanProperty();
	BooleanProperty sPressed = new SimpleBooleanProperty();
	BooleanProperty upPressed = new SimpleBooleanProperty();
	BooleanProperty downPressed = new SimpleBooleanProperty();
	DoubleProperty ballX = new SimpleDoubleProperty();
	DoubleProperty ballY = new SimpleDoubleProperty();
	DoubleProperty ballDirX = new SimpleDoubleProperty();
	DoubleProperty ballDirY = new SimpleDoubleProperty();
	DoubleProperty ballSpeed = new SimpleDoubleProperty();
	IntegerProperty lScore = new SimpleIntegerProperty();
	IntegerProperty rScore = new SimpleIntegerProperty();

	Timeline anim = new Timeline();
	KeyFrame frame;

	AudioClip hit, lost;

	public class KeyPressedHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			KeyCode k = event.getCode();
			if (k == KeyCode.W)
				wPressed.set(true);
			if (k == KeyCode.S)
				sPressed.set(true);
			if (k == KeyCode.UP)
				upPressed.set(true);
			if (k == KeyCode.DOWN)
				downPressed.set(true);
		}

	}

	public class KeyReleasedHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			KeyCode k = event.getCode();
			if (k == KeyCode.W)
				wPressed.set(false);
			if (k == KeyCode.S)
				sPressed.set(false);
			if (k == KeyCode.UP)
				upPressed.set(false);
			if (k == KeyCode.DOWN)
				downPressed.set(false);
		}

	}

	public void setKeyHandlers(Scene scene) {
		scene.setOnKeyPressed(new KeyPressedHandler());
		scene.setOnKeyReleased(new KeyReleasedHandler());
	}

	@FXML
	void initialize() {

		leftRacket.translateYProperty().bindBidirectional(leftRacketY);
		rightRacket.translateYProperty().bindBidirectional(rightRacketY);

		ball.translateXProperty().bindBidirectional(ballX);
		ball.translateYProperty().bindBidirectional(ballY);

		leftScore.textProperty().bindBidirectional(lScore, new DecimalFormat());
		rightScore.textProperty().bindBidirectional(rScore, new DecimalFormat());

		gameover.setVisible(false);

		frame = new KeyFrame(new Duration(10.0), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (gameover.visibleProperty().get())
					return;
				if (wPressed.get()) {
					if (leftRacketY.get() > -(background.getHeight() - leftRacket.getHeight()) / 2)
						leftRacketY.set(leftRacketY.get() - 5);
				}
				if (sPressed.get()) {
					if (leftRacketY.get() < (background.getHeight() - leftRacket.getHeight()) / 2)
						leftRacketY.set(leftRacketY.get() + 5);
				}
				if (upPressed.get()) {
					if (rightRacketY.get() > -(background.getHeight() - leftRacket.getHeight()) / 2)
						rightRacketY.set(rightRacketY.get() - 5);
				}
				if (downPressed.get()) {
					if (rightRacketY.get() < (background.getHeight() - leftRacket.getHeight()) / 2)
						rightRacketY.set(rightRacketY.get() + 5);
				}

				if (intersects(ball, rightRacket) || intersects(ball, leftRacket)) {
					ballDirX.set(ballDirX.get() * -1);
					// hit.play();
				}
				if (intersects(ball, topWall) || intersects(ball, bottomWall)) {
					ballDirY.set(ballDirY.get() * -1);
					// hit.play();
				}
				if (intersects(ball, leftWall)) {
					rScore.set(rScore.get() + 1);
					if (rScore.get() == 3)
						end();
					ballX.set(0);
					ballY.set(0);
					ballDirX.set(Math.random() * 0.5 + 0.5);
					ballDirY.set(Math.random() - 0.5);
					// lost.play();
				}
				if (intersects(ball, rightWall)) {
					lScore.set(lScore.get() + 1);
					if (lScore.get() == 3)
						end();
					ballX.set(0);
					ballY.set(0);
					ballDirX.set(-1 * (Math.random() * 0.5 + 0.5));
					ballDirY.set(Math.random() - 0.5);
					// lost.play();
				}

				ballX.set(ballX.get() + ballSpeed.get() * ballDirX.get());
				ballY.set(ballY.get() + ballSpeed.get() * ballDirY.get());

			}
		});

		anim.getKeyFrames().addAll(frame);
		anim.setCycleCount(Timeline.INDEFINITE);
		init();
		anim.play();

		hit = new AudioClip(getClass().getResource("/PongBallHit.wav").toExternalForm());
		lost = new AudioClip(getClass().getResource("/PongBallLost.wav").toExternalForm());
	}

	private boolean intersects(Node a, Node b) {
		return a.intersects(a.sceneToLocal(b.localToScene(b.getBoundsInLocal())));
	}

	private void init() {
		leftRacketY.set(0);
		rightRacketY.set(0);
		ballX.set(0);
		ballY.set(0);

		int sign = Math.random() > 0.5 ? 1 : -1;
		ballDirX.set(sign * (Math.random() * 0.5 + 0.5));
		ballDirY.set(Math.random() - 0.5);
		ballSpeed.set(3);

		lScore.set(0);
		rScore.set(0);
	}

	private void end() {
		gameover.setVisible(true);
	}
}
