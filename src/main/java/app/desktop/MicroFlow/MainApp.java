package app.desktop.MicroFlow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Application");

        // Создаем меню
        MenuBar menuBar = new MenuBar();

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

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}