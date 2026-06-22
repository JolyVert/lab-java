package ui;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Toast {
    public static void show(Stage owner, String message) {
        Stage toastStage = new Stage();
        toastStage.initOwner(owner);
        toastStage.initModality(Modality.NONE);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(message);
        label.setStyle("-fx-background-color: rgba(40, 40, 40, 0.85); -fx-text-fill: white; -fx-padding: 10px 20px; -fx-background-radius: 20px; -fx-font-size: 13px;");

        StackPane root = new StackPane(label);
        root.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.show();

        toastStage.setX(owner.getX() + owner.getWidth() / 2 - toastStage.getWidth() / 2);
        toastStage.setY(owner.getY() + owner.getHeight() - 130);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2.0), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(1.5));
        fadeOut.setOnFinished(e -> toastStage.close());
        fadeOut.play();
    }
}
