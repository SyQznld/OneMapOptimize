package appler.com.example.module_layer.layerTree.tree;

/**
 * Created by KaelLi on 2018/11/26.
 */
public interface TreeStateChangeListener {
    void onOpen(TreeItem treeItem, int position);

    void onClose(TreeItem treeItem, int position);
}
