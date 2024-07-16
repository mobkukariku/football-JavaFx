package com.soccer;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SoccerController {
    @FXML
    private Pane gamePane;
    @FXML
    private Circle ball;
    @FXML
    private Circle leftPlayer, rightPlayer;
    @FXML
    private Label leftScoreLabel;
    @FXML
    private Label rightScoreLabel;
    @FXML
    private BorderPane menuPane;
    @FXML
    private BorderPane gameOverPane;
    @FXML
    private ImageView logoImageView;
    @FXML
    private BorderPane pausePane;
    @FXML
    private Label gameTimerLabel;
    @FXML
    private Label winnerLabel;

    AnimationTimer gameLoop;

    private boolean paused = false;

    private double ballSpeedX = 2.0;
    private double ballSpeedY = 3.0;

    private int leftScore = 0;
    private int rightScore = 0;

    private final int playersSpeed = 20;
    private int gameTimeSeconds = 10; // Время игры в секундах (5 минут)
    private Timeline gameTimer; // Таймер для отсчета времени игры
    

    @FXML
    public void initialize() {
        showMenu();
        loadIconImage();
    }

    private void initializeGame() {
        ball.setTranslateX(400);
        ball.setTranslateY(300);
        leftPlayer.setTranslateX(50);
        leftPlayer.setTranslateY(250);
        rightPlayer.setTranslateX(740);
        rightPlayer.setTranslateY(250);
        updateScores();
    }

    private void updateScores() {
        leftScoreLabel.setText(String.valueOf(leftScore));
        rightScoreLabel.setText(String.valueOf(rightScore));
    }
    

    private void setupGameLoop() {
        initializeGameTimer(); // Инициализируем таймер игры
        gameTimer.play(); // Запускаем таймер

        gameLoop= new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!paused) {
                    updateBallPosition();
                    checkCollisions();
                }
            }
        };
        gameLoop.start();
    }

    private void updateBallPosition() {
        ball.setTranslateX(ball.getTranslateX() + ballSpeedX);
        ball.setTranslateY(ball.getTranslateY() + ballSpeedY);
    }

    private void checkCollisions() {
        if (ball.getTranslateX() <= 0 || ball.getTranslateX() >= gamePane.getWidth() - ball.getRadius() * 2) {
            ballSpeedX = -ballSpeedX;
        }
        if (ball.getTranslateY() - 45 <= 0 || ball.getTranslateY() >= gamePane.getHeight() - 37 - ball.getRadius() * 2) {
            ballSpeedY = -ballSpeedY;
        }

        if (ball.getBoundsInParent().intersects(leftPlayer.getBoundsInParent()) ||
                ball.getBoundsInParent().intersects(rightPlayer.getBoundsInParent())) {
            ballSpeedX = -ballSpeedX;
        }

        if (ball.getTranslateX() <= 0 && (ball.getTranslateY() > 200 && ball.getTranslateY() < 400)) {
            rightScore++;
            resetBall();
        } else if (ball.getTranslateX() >= gamePane.getWidth() - ball.getRadius() * 2 && (ball.getTranslateY() > 200 && ball.getTranslateY() < 400)) {
            leftScore++;
            resetBall();
        }
    }

    private void resetBall() {
        ball.setTranslateX(400);
        ball.setTranslateY(300);
        ballSpeedX = 3.0;
        ballSpeedY = 3.0;
        updateScores();
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();

        switch (code) {
            case W:
                if (leftPlayer.getTranslateY() > 0) {
                    leftPlayer.setTranslateY(leftPlayer.getTranslateY() - playersSpeed);
                }
                break;
            case S:
                if (leftPlayer.getTranslateY() < gamePane.getHeight() - leftPlayer.getRadius() * 2) {
                    leftPlayer.setTranslateY(leftPlayer.getTranslateY() + playersSpeed);
                }
                break;
            case A:
                if (leftPlayer.getTranslateX() > 0) {
                    leftPlayer.setTranslateX(leftPlayer.getTranslateX() - playersSpeed);
                }
                break;
            case D:
                if (leftPlayer.getTranslateX() < gamePane.getWidth() / 2 - leftPlayer.getRadius() * 2) {
                    leftPlayer.setTranslateX(leftPlayer.getTranslateX() + playersSpeed);
                }
                break;
            case P:
                paused=!paused;
                pausePane.setVisible(paused);
                break;
            case UP:
                if (rightPlayer.getTranslateY() > 0) {
                    rightPlayer.setTranslateY(rightPlayer.getTranslateY() - playersSpeed);
                }
                break;
            case DOWN:
                if (rightPlayer.getTranslateY() < gamePane.getHeight() - rightPlayer.getRadius() * 2) {
                    rightPlayer.setTranslateY(rightPlayer.getTranslateY() + playersSpeed);
                }
                break;
            case LEFT:
                if (rightPlayer.getTranslateX() > gamePane.getWidth() / 2) {
                    rightPlayer.setTranslateX(rightPlayer.getTranslateX() - playersSpeed);
                }
                break;
            case RIGHT:
                if (rightPlayer.getTranslateX() < gamePane.getWidth() - rightPlayer.getRadius() * 2) {
                    rightPlayer.setTranslateX(rightPlayer.getTranslateX() + playersSpeed);
                }
                break;
        }
    }

    @FXML
    private void handleStartGame() {
        menuPane.setVisible(false);
        initializeGame();
        setupGameLoop();
        gamePane.requestFocus();
    }

    @FXML
    private void handleExitGame() {
        System.exit(0);
    }
    @FXML
    private void handleResumeGame() {
        paused = false;
        pausePane.setVisible(false);
    }

    private void showMenu() {
        menuPane.setVisible(true);
    }
    
    

    private void loadIconImage() {
        Image image = new Image(getClass().getResourceAsStream("/images/menuLogo.png"));
        logoImageView.setImage(image);
    }
      private void initializeGameTimer() {
        gameTimer = new Timeline();
        gameTimer.setCycleCount(Timeline.INDEFINITE); // Запускаем бесконечный цикл таймера

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            gameTimeSeconds--; // Уменьшаем время игры на 1 секунду
            updateGameTimerLabel(); // Обновляем отображение времени
            if (gameTimeSeconds <= 0) {
                handleGameOver(); // Обрабатываем завершение игры при достижении времени
            }
        });

        gameTimer.getKeyFrames().add(keyFrame);
    }

    // Обновление метки с временем игры
    private void updateGameTimerLabel() {
        int minutes = gameTimeSeconds / 60;
        int seconds = gameTimeSeconds % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        gameTimerLabel.setText(timeFormatted); // Обновляем метку с отображением времени
    }

    // Обработка завершения игры
    private void handleGameOver() {
        gameLoop.stop(); // Останавливаем игровой цикл
        gameTimer.stop(); // Останавливаем таймер

        gameOverPane.setVisible(true);

        if(leftScore>rightScore){
            winnerLabel.setText("LeftSide is win with Score: " + leftScore);
        }else if(leftScore<rightScore){
            winnerLabel.setText("Right Side is win with Score: " + rightScore);
        }else{
            winnerLabel.setText("Draw");
        }
    }

    @FXML
    private void handleHome(){
        menuPane.setVisible(true);
        gameOverPane.setVisible(false);
    }
}
