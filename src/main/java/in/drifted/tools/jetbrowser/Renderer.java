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
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Renderer {

    private static final AnchorPane ROOT = new AnchorPane();
    private static final PanAndZoomPane PAN_AND_ZOOM = new PanAndZoomPane();
    private static final ImageView IMAGE_VIEW = new ImageView();
    private static final List<String> PATH_LIST = new ArrayList<>();

    private final Stage primaryStage;
    private final Path zipFilePath;

    private int currentImageIndex = 0;
    private String currentImageFileName = "";

    public Renderer(Stage primaryStage, Path zipFilePath) throws IOException {

        this.primaryStage = primaryStage;
        this.zipFilePath = zipFilePath;

        Group group = new Group();
        group.getChildren().add(IMAGE_VIEW);
        PAN_AND_ZOOM.getChildren().add(group);
        PAN_AND_ZOOM.toBack();

        SceneGestures sceneGestures = new SceneGestures(PAN_AND_ZOOM);

        ROOT.addEventFilter(KeyEvent.KEY_PRESSED, getOnKeyPressedEventHandler());
        ROOT.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        ROOT.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        ROOT.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        ROOT.getChildren().add(PAN_AND_ZOOM);

        try (ZipFile zipFile = new ZipFile(zipFilePath.toFile())) {
            zipFile.stream()
                    .filter(entry -> entry.getName().toLowerCase().endsWith(".jpg"))
                    .map(ZipEntry::getName)
                    .forEach(PATH_LIST::add);
        }

        updateImage();
        IMAGE_VIEW.fitHeightProperty();
    }

    public void fitHeight() {
        PAN_AND_ZOOM.fitHeight(primaryStage.getScene().getHeight());
    }

    public void fitWidth() {
        PAN_AND_ZOOM.fitWidth(primaryStage.getScene().getWidth());
    }

    private void updateImage() throws IOException {

        String path = PATH_LIST.get(currentImageIndex);

        try (FileSystem fileSystem = FileSystems.newFileSystem(zipFilePath, null)) {
            Path imagePath = fileSystem.getPath(path);
            try (InputStream inputStream = Files.newInputStream(imagePath)) {
                Image image = new Image(inputStream);
                currentImageFileName = getFileName(path);
                primaryStage.setTitle(currentImageFileName);
                IMAGE_VIEW.setImage(image);
            }
        }
    }

    public AnchorPane getRoot() {
        return ROOT;
    }

    public EventHandler<KeyEvent> getOnKeyPressedEventHandler() {
        return onKeyPressedEventHandler;
    }

    private EventHandler<KeyEvent> onKeyPressedEventHandler = new EventHandler<KeyEvent>() {

        @Override
        public void handle(KeyEvent event) {

            if (event.getCode() == KeyCode.RIGHT
                    || event.getCode() == KeyCode.LEFT
                    || event.getCode() == KeyCode.HOME
                    || event.getCode() == KeyCode.END) {

                int newImageIndex = currentImageIndex;

                if (event.getCode() == KeyCode.HOME) {
                    newImageIndex = 0;

                } else if (event.getCode() == KeyCode.END) {
                    newImageIndex = PATH_LIST.size() - 1;

                } else {
                    int delta = event.isControlDown() ? 20 : event.isShiftDown() ? 5 : 1;

                    if (event.getCode() == KeyCode.LEFT) {
                        newImageIndex = Math.max(currentImageIndex - delta, 0);

                    } else if (event.getCode() == KeyCode.RIGHT) {
                        newImageIndex = Math.min(currentImageIndex + delta, PATH_LIST.size() - 1);
                    }
                }

                if (newImageIndex != currentImageIndex) {
                    currentImageIndex = newImageIndex;
                    try {
                        updateImage();

                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

            } else if (event.getCode() == KeyCode.W) {
                fitWidth();
            } else if (event.getCode() == KeyCode.H) {
                fitHeight();
            } else if (event.getCode() == KeyCode.R) {
                PAN_AND_ZOOM.resetZoom();
            } else if (event.getCode() == KeyCode.C) {
                ClipboardContent content = new ClipboardContent();
                content.putString(currentImageFileName);
                Clipboard.getSystemClipboard().setContent(content);
            }

            event.consume();
        }
    };

    private String getFileName(String path) {

        String fileName = path;

        if (path.contains("/")) {
            int index = path.lastIndexOf("/");
            fileName = fileName.substring(index + 1);
        }

        return fileName;
    }
}
