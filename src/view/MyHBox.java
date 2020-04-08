package view;

import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//自定义布局，为了保持stage受到拉升时，布局内的各节点比例保持不变
public class MyHBox extends HBox {
    @Override
    protected void layoutChildren() {
        if(getChildren().size()==0)
            return ;
        double w = getWidth();
        double h = getHeight();

        VBox vBox =(VBox) getChildren().get(0);
        vBox.resizeRelocate(0,0,w*0.3,h);

        TreeTableView treeTableView1 = (TreeTableView) getChildren().get(1);
        treeTableView1.resizeRelocate(w*0.3,0,w*0.7,h);

        TreeTableView treeTableView2 = (TreeTableView) getChildren().get(2);
        treeTableView2.resizeRelocate(w*0.3,0,w*0.7,h);


    }
}
