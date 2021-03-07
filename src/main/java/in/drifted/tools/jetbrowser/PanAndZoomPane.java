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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Transform;

public class PanAndZoomPane extends Pane {

    public static final double DEFAULT_DELTA = 2.0d;

    private final DoubleProperty scaleProperty;
    private Point2D mousePosition;
    private Point2D panePosition;

    public PanAndZoomPane(Pane rootPane) {
        scaleProperty = new SimpleDoubleProperty(1.0);
        scaleXProperty().bind(scaleProperty);
        scaleYProperty().bind(scaleProperty);
        rootPane.addEventFilter(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        rootPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        rootPane.addEventFilter(ScrollEvent.ANY, onScrollEventHandler);
    }

    public void fitHeight(double height) {
        fit(height / getLayoutBounds().getMaxY());
    }
        
    public void fitWidth(double width) {
        fit(width / getLayoutBounds().getMaxX());
    }

    private void fit(double scale) {

        double f = scale - scaleProperty.get();
        double dx = getTranslateX() - getBoundsInParent().getMinX() - getBoundsInParent().getWidth() / 2;
        double dy = getTranslateY() - getBoundsInParent().getMinY() - getBoundsInParent().getHeight() / 2;

        double newX = f * dx + getBoundsInParent().getMinX();
        double newY = f * dy + getBoundsInParent().getMinY();

        setPivot(newX, newY, scale);
    }

    public void resetZoom() {
        setPivot(getTranslateX(), getTranslateY(), 1.0d);
    }

    public void setPivot(double x, double y, double scale) {
        translateXProperty().setValue(getTranslateX() - x);
        translateYProperty().setValue(getTranslateY() - y);
        scaleProperty.setValue(scale);
    }

    public DoubleProperty getScaleProperty() {
        return scaleProperty;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<>() {

        @Override
        public void handle(MouseEvent event) {

            mousePosition = new Point2D(event.getX(), event.getY());
            panePosition = new Point2D(getTranslateX(), getTranslateY());
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<>() {

        @Override
        public void handle(MouseEvent event) {

            setTranslateX(panePosition.getX() + event.getX() - mousePosition.getX());
            setTranslateY(panePosition.getY() + event.getY() - mousePosition.getY());

            event.consume();
        }
    };

    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<>() {

        @Override
        public void handle(ScrollEvent event) {

            double scale = getScaleProperty().get();
            double oldScale = scale;

            if (event.getDeltaY() < 0) {
                scale /= PanAndZoomPane.DEFAULT_DELTA;
            } else {
                scale *= PanAndZoomPane.DEFAULT_DELTA;
            }

            Bounds bounds = getBoundsInParent();

            double f = (scale / oldScale) - 1;
            double dx = (event.getX() - (bounds.getWidth() / 2 + bounds.getMinX()));
            double dy = (event.getY() - (bounds.getHeight() / 2 + bounds.getMinY()));

            setPivot(f * dx, f * dy, scale);

            event.consume();
        }
    };

}
