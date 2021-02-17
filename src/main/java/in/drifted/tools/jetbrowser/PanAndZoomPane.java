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
import javafx.scene.layout.Pane;

public class PanAndZoomPane extends Pane {

    public static final double DEFAULT_DELTA = 2.0d;

    private final DoubleProperty scaleProperty;
    private final DoubleProperty deltaYProperty;

    public PanAndZoomPane(double scale, double deltaY) {
        scaleProperty = new SimpleDoubleProperty(scale);
        deltaYProperty = new SimpleDoubleProperty(deltaY);
        scaleXProperty().bind(scaleProperty);
        scaleYProperty().bind(scaleProperty);
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

    public DoubleProperty getDeltaYProperty() {
        return deltaYProperty;
    }

}
