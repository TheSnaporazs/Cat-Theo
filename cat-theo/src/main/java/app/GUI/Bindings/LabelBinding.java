package app.GUI.Bindings;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.shape.Line;

public class LabelBinding extends DoubleBinding {

    private final Line line;
    private final VEC computation;

    public LabelBinding(Line line, VEC computation)
    {
        bind(line.startXProperty(), line.endXProperty() , line.startYProperty(), line.endYProperty());
        this.line = line;
        this.computation = computation;
    }



    @Override
    protected double computeValue()
    {
        switch (computation)
        {
            case Ax -> {
                return line.getStartX() + 0.5*(line.getEndX() - line.getStartX());
            }

            case Ay -> {
                return line.getStartY() + 0.5*(line.getEndY() - line.getStartY());
            }
            default -> {
                return 0;
            }
        }
    }
}
