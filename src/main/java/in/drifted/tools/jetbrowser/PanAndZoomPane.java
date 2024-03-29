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
import javafx.scene.layout.Pane;

public class PanAndZoomPane extends Pane {

    public static final double DEFAULT_DELTA = 1.2;

    private final Pane rootPane;
    private Point2D mousePosition;
    private Point2D panePosition;

    public PanAndZoomPane(Pane rootPane) {
        this.rootPane = rootPane;
        rootPane.addEventFilter(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        rootPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        rootPane.addEventFilter(ScrollEvent.ANY, onScrollEventHandler);
    }

    public void fitHeight() {
        fit(rootPane.getHeight() / getLayoutBounds().getMaxY());
        translateXProperty().setValue(getTranslateX() + (rootPane.getWidth() - getBoundsInParent().getWidth()) / 2);
    }

    public void fitWidth() {
        fit(rootPane.getWidth() / getLayoutBounds().getMaxX());
    }

    private void fit(double scale) {

        double f = scale - scaleXProperty().get();
        double dx = getTranslateX() - getBoundsInParent().getMinX() - getBoundsInParent().getWidth() / 2;
        double dy = getTranslateY() - getBoundsInParent().getMinY() - getBoundsInParent().getHeight() / 2;

        double centerX = f * dx + getBoundsInParent().getMinX();
        double centerY = f * dy + getBoundsInParent().getMinY();

        setPivot(scale, centerX, centerY);
    }

    public void zoomPlus() {
        zoom(true, rootPane.getWidth() / 2, rootPane.getHeight() / 2);
    }

    public void zoomMinus() {
        zoom(false, rootPane.getWidth() / 2, rootPane.getHeight() / 2);
    }

    private void zoom(boolean plus, double centerX, double centerY) {

        double scale = scaleXProperty().get();
        double oldScale = scale;

        if (plus) {
            scale *= PanAndZoomPane.DEFAULT_DELTA;
        } else {
            scale /= PanAndZoomPane.DEFAULT_DELTA;
        }

        Bounds bounds = getBoundsInParent();

        double f = (scale / oldScale) - 1;
        double dx = centerX - (bounds.getWidth() / 2 + bounds.getMinX());
        double dy = centerY - (bounds.getHeight() / 2 + bounds.getMinY());

        setPivot(scale, f * dx, f * dy);
    }

    public void resetZoom() {
        setPivot(1.0, getTranslateX(), getTranslateY());
    }

    public void setPivot(double scale, double centerX, double centerY) {
        translateXProperty().setValue(getTranslateX() - centerX);
        translateYProperty().setValue(getTranslateY() - centerY);
        scaleXProperty().setValue(scale);
        scaleYProperty().setValue(scale);
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        mousePosition = new Point2D(event.getX(), event.getY());
        panePosition = new Point2D(getTranslateX(), getTranslateY());
        event.consume();
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        setTranslateX(panePosition.getX() + event.getX() - mousePosition.getX());
        setTranslateY(panePosition.getY() + event.getY() - mousePosition.getY());
        event.consume();
    };

    private EventHandler<ScrollEvent> onScrollEventHandler = event -> {
        zoom(event.getDeltaY() > 0, event.getX(), event.getY());
        event.consume();
    };

}
