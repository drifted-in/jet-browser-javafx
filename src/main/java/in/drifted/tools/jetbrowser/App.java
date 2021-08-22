/*
 * Copyright (c) 2021 Jan Tošovský <jan.tosovsky.cz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package in.drifted.tools.jetbrowser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

    private static Path path;

    public static void main(String[] args) {
        path = Paths.get(args[0]);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
         
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
            primaryStage.setMaximized(true);
            primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("/icon.png")));

            Renderer renderer = new Renderer(primaryStage, path);
            Scene scene = new Scene(renderer.getRoot());
            primaryStage.setScene(scene);
            primaryStage.show();
           
            renderer.fitHeight();
            renderer.getRoot().requestFocus();
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
