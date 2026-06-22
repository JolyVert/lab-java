package controller;


import service.ImageService;
import service.LoggerService;
import ui.Toast;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainController {

    private final Stage primaryStage;
    private final ImageService imageService;

    private Image originalImage;
    private Image currentImage;
    private boolean operationsPerformed = false;

    public MainController(Stage primaryStage, ImageService imageService) {
        this.primaryStage = primaryStage;
        this.imageService = imageService;
    }

    public void handleLoadImage(Runnable onSuccess, Runnable onReset) {
        LoggerService.log("INFO", "Użytkownik kliknął 'Wczytaj plik'.");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik obrazu JPG");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Obrazy JPG (*.jpg)", "*.jpg"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            String name = selectedFile.getName().toLowerCase();
            if (!name.endsWith(".jpg")) {
                Toast.show(primaryStage, "Niedozwolony format pliku");
                LoggerService.log("ERROR", "Próba wczytania nieprawidłowego formatu: " + selectedFile.getName());
                return;
            }

            try {
                onReset.run();
                System.gc();

                Image img = new Image(selectedFile.toURI().toString());
                if (img.isError()) throw new Exception("Błąd ładowania struktury obrazu");

                originalImage = img;
                currentImage = img;
                operationsPerformed = false;

                onSuccess.run();
                Toast.show(primaryStage, "Pomyślnie załadowano plik");
                LoggerService.log("INFO", "Pomyślnie załadowano plik: " + selectedFile.getAbsolutePath());
            } catch (Exception ex) {
                Toast.show(primaryStage, "Nie udało się załadować pliku");
                LoggerService.log("ERROR", "Błąd ładowania pliku: " + ex.getMessage());
            }
        }
    }

    public void handleExecuteOperation(String operation, Runnable updateView) {
        if (operation == null) {
            Toast.show(primaryStage, "Nie wybrano operacji do wykonania");
            LoggerService.log("INFO", "Kliknięto 'Wykonaj' bez wyboru operacji.");
            return;
        }

        LoggerService.log("INFO", "Wykonanie operacji: " + operation);

        try {
            if ("Negatyw".equals(operation) || "Konturowanie".equals(operation)) {
                currentImage = imageService.applyParallelOperation(currentImage, operation, null);
                operationsPerformed = true;
                updateView.run();
                Toast.show(primaryStage, operation + " został wygenerowany pomyślnie!");
            } else if ("Progowanie".equals(operation)) {
                showThresholdModal(updateView);
            }
        } catch (Exception e) {
            Toast.show(primaryStage, "Nie udało się wykonać " + operation.toLowerCase() + ".");
            LoggerService.log("ERROR", "Błąd wielowątkowości: " + e.getMessage());
        }
    }

    private void showThresholdModal(Runnable updateView) {
        Stage modal = new Stage(StageStyle.UTILITY);
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(primaryStage);
        modal.setTitle("Progowanie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        TextField tfThreshold = new TextField("128");
        configureNumericField(tfThreshold, 255);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button btnOk = new Button("Wykonaj progowanie");
        Button btnCancel = new Button("Anuluj");

        btnCancel.setOnAction(e -> modal.close());
        btnOk.setOnAction(e -> {
            if (tfThreshold.getText().trim().isEmpty()) {
                errorLabel.setText("Pole jest wymagane");
                return;
            }
            int th = Integer.parseInt(tfThreshold.getText());
            modal.close();
            try {
                currentImage = imageService.applyParallelOperation(currentImage, "Progowanie", th);
                operationsPerformed = true;
                updateView.run();
                Toast.show(primaryStage, "Progowanie zostało przeprowadzone pomyślnie!");
            } catch (Exception ex) {
                Toast.show(primaryStage, "Nie udało się wykonać progowania.");
            }
        });

        layout.getChildren().addAll(new Label("Wpisz wartość progu (0-255):"), tfThreshold, errorLabel, new HBox(10, btnOk, btnCancel));
        modal.setScene(new Scene(layout, 300, 160));
        modal.showAndWait();
    }

    public void handleScaleImage(Runnable updateView) {
        Stage modal = new Stage(StageStyle.UTILITY);
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(primaryStage);
        modal.setTitle("Skalowanie obrazu");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        TextField tfW = new TextField(String.valueOf((int) currentImage.getWidth()));
        TextField tfH = new TextField(String.valueOf((int) currentImage.getHeight()));
        configureNumericField(tfW, 3000);
        configureNumericField(tfH, 3000);

        Label errW = new Label(); errW.setStyle("-fx-text-fill: red;");
        Label errH = new Label(); errH.setStyle("-fx-text-fill: red;");

        Button btnApply = new Button("Zmień rozmiar");
        Button btnRestore = new Button("Przywróć oryginalne wymiary");

        btnRestore.setOnAction(e -> {
            tfW.setText(String.valueOf((int) originalImage.getWidth()));
            tfH.setText(String.valueOf((int) originalImage.getHeight()));
        });

        btnApply.setOnAction(e -> {
            if (tfW.getText().isEmpty()) { errW.setText("Pole jest wymagane"); return; }
            if (tfH.getText().isEmpty()) { errH.setText("Pole jest wymagane"); return; }

            int targetW = Integer.parseInt(tfW.getText());
            int targetH = Integer.parseInt(tfH.getText());

            currentImage = imageService.scale(currentImage, targetW, targetH);
            operationsPerformed = true;
            updateView.run();
            modal.close();
            LoggerService.log("INFO", "Przeskalowano obraz do: " + targetW + "x" + targetH);
        });

        layout.getChildren().addAll(new Label("Szerokość:"), tfW, errW, new Label("Wysokość:"), tfH, errH, btnRestore, btnApply);
        modal.setScene(new Scene(layout, 300, 320));
        modal.showAndWait();
    }

    public void handleRotate(boolean clockwise, Runnable updateView) {
        currentImage = imageService.rotate(currentImage, clockwise);
        operationsPerformed = true;
        updateView.run();
    }

    public void handleSaveImage() {
        Stage modal = new Stage(StageStyle.UTILITY);
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(primaryStage);
        modal.setTitle("Zapisz plik");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label alertLabel = new Label();
        if (!operationsPerformed) {
            alertLabel.setText("Na pliku nie zostały wykonane żadne operacje!");
            alertLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        }

        TextField tfName = new TextField();
        tfName.textProperty().addListener((ov, o, n) -> { if (n.length() > 100) tfName.setText(o); });

        Label errorLabel = new Label(); errorLabel.setStyle("-fx-text-fill: red;");

        Button btnSave = new Button("Zapisz");
        btnSave.setOnAction(e -> {
            String name = tfName.getText().trim();
            if (name.length() < 3) { errorLabel.setText("Wpisz co najmniej 3 znaki"); return; }

            File destDir = new File(System.getProperty("user.home"), "Pictures");
            if (!destDir.exists()) destDir = new File(System.getProperty("user.home"), "Obrazy");

            File outFile = new File(destDir, name + ".jpg");
            if (outFile.exists()) {
                Toast.show(primaryStage, "Plik " + name + ".jpg już istnieje w systemie. Podaj inną nazwę pliku!");
                return;
            }

            try {
                BufferedImage bimg = SwingFXUtils.fromFXImage(currentImage, null);
                ImageIO.write(bimg, "jpg", outFile);
                Toast.show(primaryStage, "Zapisano obraz w pliku " + name + ".jpg");
                LoggerService.log("INFO", "Zapisano plik: " + outFile.getAbsolutePath());
                modal.close();
            } catch (IOException ex) {
                Toast.show(primaryStage, "Nie udało się zapisać pliku " + name + ".jpg");
            }
        });

        layout.getChildren().addAll(alertLabel, new Label("Nazwa pliku:"), tfName, errorLabel, btnSave);
        modal.setScene(new Scene(layout, 350, 220));
        modal.showAndWait();
    }

    private void configureNumericField(TextField tf, int max) {
        tf.textProperty().addListener((ov, old, newVal) -> {
            if (!newVal.matches("\\d*")) tf.setText(newVal.replaceAll("[^\\d]", ""));
            else if (!tf.getText().isEmpty() && Integer.parseInt(tf.getText()) > max) tf.setText(old);
        });
    }

    public Image getOriginalImage() { return originalImage; }
    public Image getCurrentImage() { return currentImage; }
}
