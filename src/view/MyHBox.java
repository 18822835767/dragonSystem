package view;

import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 自定义布局，为了保持stage受到拉升时，布局内的各节点比例保持不变.
 * fxml所有的根布局就是这个MyHBox。
 * */
public class MyHBox extends HBox {
    @Override
    protected void layoutChildren() {
        if(getChildren().size()==0)
            return ;
        double w = getWidth();
        double h = getHeight();

        VBox vBox =(VBox) getChildren().get(0);
        vBox.resizeRelocate(0,0,w*0.3,h);

        getChildren().get(1).resizeRelocate(w*0.3,0,w*0.7,h);

        getChildren().get(2).resizeRelocate(w*0.3,0,w*0.7,h);

    }
}
