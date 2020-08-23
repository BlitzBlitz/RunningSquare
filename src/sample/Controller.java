package sample;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Optional;

public class Controller {

    @FXML
    Circle circle;

    @FXML
    Rectangle wall;

    @FXML
    Label pointLabel;

    private TranslateTransition moveWall = new TranslateTransition();
    private int points;
    private int playerMovement = 50;
    private double speed = 2;

    public void initialize(){
        Platform.runLater(()->{
           circle.getScene().setOnKeyPressed(this::handleMoves);
           run();
        });
        checkCollisions();
        points = 0;
        pointLabel.setText(Long.toString(points));
    }

    public void handleMoves(KeyEvent keyEvent){
        if(keyEvent.getCode().toString().equals("LEFT") && circle.getLayoutX() >= circle.getRadius()){
            circle.setLayoutX(circle.getLayoutX()-playerMovement);
        }else if(keyEvent.getCode().toString().equals("RIGHT") && circle.getLayoutX() <= circle.getScene().getWidth() - circle.getRadius()*2){
            circle.setLayoutX(circle.getLayoutX()+playerMovement);
        }else if(keyEvent.getCode().toString().equals("UP") && circle.getLayoutY() >= circle.getRadius()){
            circle.setLayoutY(circle.getLayoutY() - playerMovement);
        }else if(keyEvent.getCode().toString().equals("DOWN") && circle.getLayoutY() <= circle.getScene().getHeight() - circle.getRadius()*2){
            circle.setLayoutY(circle.getLayoutY() + playerMovement);
        }
    }


    public void run(){
        moveWall.setDuration(Duration.seconds(speed));
        moveWall.setFromY(circle.getScene().getHeight());
        moveWall.setToY(wall.getWidth()*-2);

        moveWall.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double movement = Math.random() * circle.getScene().getWidth();
                wall.setWidth(movement);
                wall.setX(movement/2);
                points++;
                pointLabel.setText(Long.toString(points));
                moveWall.playFromStart();
            }
        });
        moveWall.setNode(wall);
        moveWall.play();

    }

    public void checkCollisions(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!(wall.getBoundsInParent().intersects(circle.getBoundsInParent()))){
                    try {
                        Thread.sleep(15);
                    }catch (Exception e){

                    }
                }
                if((wall.getBoundsInParent().intersects(circle.getBoundsInParent()))){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            moveWall.stop();
                            if(points > GameData.getInstance().getTopScore()){
                                GameData.getInstance().setTopScore(points);
                                displayTopScore();
                            }
                            Alert gameover = new Alert(Alert.AlertType.CONFIRMATION);
                            gameover.setHeaderText("Gameover!");
                            gameover.setContentText("Score: " + points + "\nDo you want to play agian?");
                            Optional<ButtonType> result = gameover.showAndWait();
                            if(result.get().equals(ButtonType.OK)){
                                moveWall.setFromY(circle.getScene().getHeight());
                                circle.setLayoutY(circle.getScene().getHeight()/3);
                                circle.setLayoutX(circle.getScene().getWidth()/3);
                                initialize();
                            }else {
                                Platform.exit();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void displayTopScore(){
        Alert newRecord = new Alert(Alert.AlertType.INFORMATION);
        newRecord.setHeaderText("New Record!!!");
        newRecord.setContentText("Top Score: " + points);
        newRecord.showAndWait();
    }
}
