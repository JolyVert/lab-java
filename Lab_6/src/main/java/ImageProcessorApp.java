import controller.MainController;
import service.ImageService;
import service.LoggerService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ImageProcessorApp extends Application {

    private MainController controller;
    private final ImageService imageService = new ImageService();

    private ImageView originalImageView;
    private ImageView processedImageView;
    private Button btnLoad, btnSave, btnScale, btnRotateLeft, btnRotateRight, btnExecute;
    private ComboBox<String> opComboBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoggerService.log("INFO", "Uruchomienie aplikacji.");
        this.controller = new MainController(primaryStage, imageService);

        primaryStage.setTitle("Platformy Programistyczne - Laboratorium 6");

        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        Label titleLabel = new Label("ImageProcessor v1.0");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label logoLabel = new Label("Politechnika Wrocławska");
        logoLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: #777777;");
        headerBox.getChildren().addAll(titleLabel, logoLabel);

        originalImageView = new ImageView();
        processedImageView = new ImageView();
        configureImageView(originalImageView);
        configureImageView(processedImageView);

        ScrollPane scrollOrig = new ScrollPane(new StackPane(originalImageView));
        ScrollPane scrollProc = new ScrollPane(new StackPane(processedImageView));
        styleScrollPane(scrollOrig);
        styleScrollPane(scrollProc);

        VBox boxOriginal = new VBox(5, new Label("Obraz oryginalny:"), scrollOrig);
        VBox boxProcessed = new VBox(5, new Label("Obraz po zmianach:"), scrollProc);
        boxOriginal.setAlignment(Pos.CENTER);
        boxProcessed.setAlignment(Pos.CENTER);
        HBox.setHgrow(boxOriginal, Priority.ALWAYS);
        HBox.setHgrow(boxProcessed, Priority.ALWAYS);

        SplitPane splitPane = new SplitPane(boxOriginal, boxProcessed);
        splitPane.setDividerPositions(0.5);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        HBox controlPanel = new HBox(10);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(15));
        controlPanel.setStyle("-fx-background-color: #f5f5f5;");

        btnLoad = new Button("Wczytaj plik");
        btnSave = new Button("Zapisz");
        btnScale = new Button("Skaluj");
        btnRotateLeft = new Button("Obróć 90° ←");
        btnRotateRight = new Button("Obróć 90° →");
        btnExecute = new Button("Wykonaj");

        opComboBox = new ComboBox<>(FXCollections.observableArrayList("Negatyw", "Progowanie", "Konturowanie"));
        opComboBox.setPromptText("Wybierz operację");

        controlPanel.getChildren().addAll(btnLoad, btnSave, btnScale, btnRotateLeft, btnRotateRight, opComboBox, btnExecute);
        setProcessingButtonsDisable(true);

        HBox footerBox = new HBox(new Label("Autor: Anton Shkumat | mgr inż. M. Jaroszczuk"));
        footerBox.setAlignment(Pos.CENTER_RIGHT);
        footerBox.setPadding(new Insets(5, 15, 5, 15));
        footerBox.setStyle("-fx-background-color: #dddddd; -fx-font-size: 11px;");

        btnLoad.setOnAction(e -> controller.handleLoadImage(
                () -> {
                    originalImageView.setImage(controller.getOriginalImage());
                    processedImageView.setImage(controller.getCurrentImage());
                    setProcessingButtonsDisable(false);
                },
                () -> {
                    originalImageView.setImage(null);
                    processedImageView.setImage(null);
                }
        ));

        btnExecute.setOnAction(e -> controller.handleExecuteOperation(
                opComboBox.getValue(),
                () -> processedImageView.setImage(controller.getCurrentImage())
        ));

        btnScale.setOnAction(e -> controller.handleScaleImage(
                () -> processedImageView.setImage(controller.getCurrentImage())
        ));

        btnRotateLeft.setOnAction(e -> controller.handleRotate(false,
                () -> processedImageView.setImage(controller.getCurrentImage())
        ));

        btnRotateRight.setOnAction(e -> controller.handleRotate(true,
                () -> processedImageView.setImage(controller.getCurrentImage())
        ));

        btnSave.setOnAction(e -> controller.handleSaveImage());

        VBox root = new VBox(headerBox, splitPane, controlPanel, footerBox);
        primaryStage.setScene(new Scene(root, 1000, 650));
        primaryStage.show();
    }

    @Override
    public void stop() {
        LoggerService.log("INFO", "Zamknięcie aplikacji.");
        imageService.shutdown();
    }

    private void setProcessingButtonsDisable(boolean disable) {
        btnSave.setDisable(disable);
        btnScale.setDisable(disable);
        btnRotateLeft.setDisable(disable);
        btnRotateRight.setDisable(disable);
        btnExecute.setDisable(disable);
    }

    private void configureImageView(ImageView iv) {
        iv.setPreserveRatio(true);
        iv.setFitWidth(400);
        iv.setFitHeight(400);
    }

    private void styleScrollPane(ScrollPane sp) {
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        sp.setPrefSize(420, 420);
    }
}
