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

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class SceneGestures {

    private final PanAndZoomPane panAndZoomPane;
    private Point2D mousePosition;
    private Point2D panePosition;
    
    public SceneGestures(PanAndZoomPane panAndZoomPane) {
        this.panAndZoomPane = panAndZoomPane;
    }
        
    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<>() {

        @Override
        public void handle(MouseEvent event) {

            mousePosition = new Point2D(event.getX(), event.getY());
            panePosition = new Point2D(panAndZoomPane.getTranslateX(), panAndZoomPane.getTranslateY());
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<>() {

        @Override
        public void handle(MouseEvent event) {

            panAndZoomPane.setTranslateX(panePosition.getX() + event.getX() - mousePosition.getX());
            panAndZoomPane.setTranslateY(panePosition.getY() + event.getY() - mousePosition.getY());

            event.consume();
        }
    };

    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<>() {

        @Override
        public void handle(ScrollEvent event) {

            double delta = PanAndZoomPane.DEFAULT_DELTA;
            double scale = panAndZoomPane.getScaleProperty().get();
            double oldScale = scale;

            panAndZoomPane.getDeltaYProperty().set(event.getDeltaY());

            if (panAndZoomPane.getDeltaYProperty().get() < 0) {
                scale /= delta;
            } else {
                scale *= delta;
            }

            Bounds bounds = panAndZoomPane.getBoundsInParent();

            double f = (scale / oldScale) - 1;
            double dx = (event.getX() - (bounds.getWidth() / 2 + bounds.getMinX()));
            double dy = (event.getY() - (bounds.getHeight() / 2 + bounds.getMinY()));

            panAndZoomPane.setPivot(f * dx, f * dy, scale);

            event.consume();
        }
    };

}
