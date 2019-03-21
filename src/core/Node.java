/**
 * Created by 14260 on 2019/1/6.
 */
public class Node {
    String sort;    //节点可关键字
    Character name; //节点的名称
    Node left;      //左孩子
    Node right;     //右有孩子
    boolean state;  //标志是否为状态节点

    static Node getPre(Node head, String child_sort){
        if(head!=null) {
            Node n = head;
            if ((n.left != null && n.left.sort.equals(child_sort)) || (n.right != null && n.right.sort.equals(child_sort))) {
                return n;
            } else {
                Node nd1 = getPre(head.left, child_sort);
                if (nd1 != null) return nd1;
                Node nd2 = getPre(head.right, child_sort);
                if (nd1 != null) return nd2;
            }
        }
        return null;
    }

    public static Node getBySort(Node head, String child_sort) {
        if(head!=null) {
            Node n = head;
            if ((n.left != null && n.left.sort.equals(child_sort))) {
                return n;
            } else {
                Node nd1 = getPre(head.left, child_sort);
                if (nd1 != null) return nd1;
                Node nd2 = getPre(head.right, child_sort);
                if (nd1 != null) return nd2;
            }
        }
        return null;
    }
}
