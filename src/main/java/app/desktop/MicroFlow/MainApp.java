package app.desktop.MicroFlow;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.security.cert.PolicyNode;


import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
/**
 * App Main class
 */
public class MainApp extends Application {

    private Pane pane;
    private double startX, startY;
    private Rectangle selectedBlock;
    private Map<String, Rectangle> blocks = new HashMap<>();
    private Map<Rectangle, Text> blockLabels = new HashMap<>();
    private Set<Line> lines = new HashSet<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Application");

        // Создаем меню
        MenuBar menuBar = new MenuBar();
        pane = new Pane();

        // Меню File
        Menu fileMenu = new Menu("File");
        MenuItem newFile = new MenuItem("New");
        MenuItem openFile = new MenuItem("Open");
        MenuItem saveFile = new MenuItem("Save");
        fileMenu.getItems().addAll(newFile, openFile, saveFile);

        // Меню Edit
        Menu editMenu = new Menu("Edit");
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        editMenu.getItems().addAll(cut, copy, paste);

        // Меню Tools
        Menu toolsMenu = new Menu("Tools");

        // Меню Git
        Menu gitMenu = new Menu("Git");

        // Меню Window
        Menu windowMenu = new Menu("Window");

        // Меню Help
        Menu helpMenu = new Menu("Help");

        // Добавляем все меню в бар
        menuBar.getMenus().addAll(fileMenu, editMenu, toolsMenu, gitMenu, windowMenu, helpMenu);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);

//        Scene scene = new Scene(root, 800, 600);
//        primaryStage.setScene(scene);
//        primaryStage.show();
        

        // Добавление кнопки для создания нового блока
        Button addButton = new Button("Добавить блок");
        addButton.setLayoutX(10);
        addButton.setLayoutY(10);
        addButton.setOnAction(e -> addBlock(100, 100));

        pane.getChildren().add(addButton);
        root.setCenter(pane);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Редактор блок-схем");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addBlock(double x, double y) {
        Rectangle block = new Rectangle(x, y, 100, 50);
        block.setFill(Color.LIGHTGRAY);
        block.setStroke(Color.BLACK);

        Text label = new Text("");
        label.xProperty().bind(block.xProperty().add(block.getWidth() / 2 - 20));
        label.yProperty().bind(block.yProperty().add(block.getHeight() / 2 + 5));

        block.setOnMousePressed(this::onBlockPressed);
        block.setOnMouseDragged(this::onBlockDragged);
        block.setOnMouseReleased(this::onBlockReleased);
        block.setOnMouseClicked(this::onBlockClicked);

        pane.getChildren().addAll(block, label);
        blockLabels.put(block, label);
    }

    private void onBlockPressed(MouseEvent event) {
        Rectangle block = (Rectangle) event.getSource();
        startX = event.getSceneX();
        startY = event.getSceneY();
        selectedBlock = block;
    }

    private void onBlockDragged(MouseEvent event) {
        if (selectedBlock != null) {
            double offsetX = event.getSceneX() - startX;
            double offsetY = event.getSceneY() - startY;

            selectedBlock.setX(selectedBlock.getX() + offsetX);
            selectedBlock.setY(selectedBlock.getY() + offsetY);

            startX = event.getSceneX();
            startY = event.getSceneY();
        }
    }

    private void onBlockReleased(MouseEvent event) {
        selectedBlock = null;
    }

    private void onBlockClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            Rectangle block = (Rectangle) event.getSource();
            showSettingsDialog(block);
        }
    }

    private void showSettingsDialog(Rectangle block) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Настройки блока");

        Label nameLabel = new Label("Имя блока:");
        TextField nameField = new TextField();

        Label connectLabel = new Label("Связать с блоком:");
        ComboBox<String> connectComboBox = new ComboBox<>();
        connectComboBox.getItems().addAll(blocks.keySet());
        connectComboBox.getItems().removeIf(name -> blocks.get(name) == block); // Удаляем текущий блок из списка

        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> {
            String blockName = nameField.getText();
            String connectTo = connectComboBox.getValue();

            if (!blockName.isEmpty() && !blocks.containsKey(blockName)) {
                blocks.put(blockName, block);
                blockLabels.get(block).setText(blockName);
            } else if (blocks.containsKey(blockName)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Имя блока уже существует");
                alert.setContentText("Пожалуйста, выберите уникальное имя для блока.");
                alert.showAndWait();
                return;
            }

            if (connectTo != null && blocks.containsKey(connectTo)) {
                Rectangle targetBlock = blocks.get(connectTo);
                connectBlocks(block, targetBlock);
            }

            dialog.close();
        });

        VBox dialogVBox = new VBox(10, nameLabel, nameField, connectLabel, connectComboBox, saveButton);
        dialogVBox.setPadding(new Insets(10));

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void connectBlocks(Rectangle block1, Rectangle block2) {
        Line line = new Line();
        line.startXProperty().bind(block1.xProperty().add(block1.getWidth() / 2));
        line.startYProperty().bind(block1.yProperty().add(block1.getHeight() / 2));
        line.endXProperty().bind(block2.xProperty().add(block2.getWidth() / 2));
        line.endYProperty().bind(block2.yProperty().add(block2.getHeight() / 2));
        pane.getChildren().add(line);
        lines.add(line);

        // Перемещаем блоки на передний план, чтобы они отображались над линиями
        block1.toFront();
        block2.toFront();
        blockLabels.get(block1).toFront();
        blockLabels.get(block2).toFront();
    }

    public static void main(String[] args) {
        launch(args);
    }
}