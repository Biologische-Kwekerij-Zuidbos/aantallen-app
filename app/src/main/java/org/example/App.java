/* (C)2024 */
package org.example;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// See https://fxdocs.github.io/docs/html5/#_the_mvvm_pattern
public class App extends Application {

    private static final String WINDOW_TITLE = "Aantallen verwerken";

    private Parent createContent(final Stage stage) {
        return new View(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent(stage), 300, 200));
        stage.setTitle(WINDOW_TITLE);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
