import java.util.List;

/**
 * Created by 14260 on 2019/1/6.
 */
public class LtlSimply {






        public static List<Node> OPT(List<Node> nodes) {

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node.name == 'G') {
                Node nw = new Node();
                nw.sort = node.sort + "__1";
                nw.name = '#'; //false
                node.name = 'V';
                nodes.get(i).left = nw;
                nodes.add(nw);

            } else if (node.name == 'F') {
                Node nw = new Node();
                nw.sort = node.sort + "__1";
                nw.name = '%'; //true
                node.name = 'U';
                nodes.get(i).left = nw;
                nodes.add(nw);
            } else if (node.name == '~') {

                //~X  ~G
                if(node.right.name=='X' || node.right.name=='G'){
                    int preidx = LtlParse.getPreIdx(nodes, node.sort);
                    int rightidx= LtlParse.getPreIdx(nodes, node.right.sort);
                    nodes.get(rightidx).right = node;

                    Node pre = nodes.get(preidx);
                    if(pre.right!=null && pre.right.sort.equals(node.sort)) nodes.get(preidx).right =  nodes.get(rightidx);
                    if(pre.left!=null && pre.left.sort.equals(node.sort)) nodes.get(preidx).left =  nodes.get(rightidx);

                    nodes.get(i).right = nodes.get(rightidx).right;

                //~U
                }else  if(node.right.name=='U' ) {
                    int preidx = LtlParse.getPreIdx(nodes, node.sort);
                    int rightidx = LtlParse.getPreIdx(nodes, node.right.sort);
                    Node pre = nodes.get(preidx);
                    nodes.get(rightidx).name = 'V';
                    nodes.get(i).right = nodes.get(rightidx).right;
                    Node nd = new Node();
                    nd.sort = node.right.sort+"__1";
                    nd.name = '~';
                    nd.right =  nodes.get(rightidx).left;

                    nodes.get(rightidx).right = node;
                    nodes.get(rightidx).right = nd;

                    if(pre.right!=null && pre.right.sort.equals(node.sort)) nodes.get(preidx).right = nodes.get(rightidx);
                    if(pre.left !=null && pre.left.sort.equals(node.sort)) nodes.get(preidx).left = nodes.get(rightidx);
                    nodes.add(nd);

                //~V
                }else  if(node.right.name=='V') {
                    int preidx = LtlParse.getPreIdx(nodes, node.sort);
                    int rightidx = LtlParse.getPreIdx(nodes, node.right.sort);
                    Node pre = nodes.get(preidx);
                    nodes.get(rightidx).name = 'U';
                    nodes.get(i).right = nodes.get(rightidx).right;
                    Node nd = new Node();
                    nd.sort = node.right.sort+"__1";
                    nd.name = '~';
                    nd.right =  nodes.get(rightidx).left;

                    nodes.get(rightidx).right = node;
                    nodes.get(rightidx).right = nd;

                    if(pre.right!=null && pre.right.sort.equals(node.sort)) nodes.get(preidx).right = nodes.get(rightidx);
                    if(pre.left!=null && pre.left.sort.equals(node.sort)) nodes.get(preidx).left = nodes.get(rightidx);
                    nodes.add(nd);
                //~N
                }else  if(node.right.name=='N') {
                    int preidx = LtlParse.getPreIdx(nodes, node.sort);
                    int rightidx = LtlParse.getPreIdx(nodes, node.right.sort);
                    Node pre = nodes.get(preidx);
                    nodes.get(rightidx).name = 'W';
                    nodes.get(i).right = nodes.get(rightidx).right;
                    Node nd = new Node();
                    nd.sort = node.right.sort+"__1";
                    nd.name = '~';
                    nd.right =  nodes.get(rightidx).left;

                    nodes.get(rightidx).right = node;
                    nodes.get(rightidx).right = nd;

                    if(pre.right!=null && pre.right.sort.equals(node.sort)) nodes.get(preidx).right = nodes.get(rightidx);
                    if(pre.left!=null && pre.left.sort.equals(node.sort)) nodes.get(preidx).left = nodes.get(rightidx);
                    nodes.add(nd);
                }//~W
                else  if(node.right.name=='W') {
                    int preidx = LtlParse.getPreIdx(nodes, node.sort);
                    int rightidx = LtlParse.getPreIdx(nodes, node.right.sort);
                    Node pre = nodes.get(preidx);
                    nodes.get(rightidx).name = 'N';
                    nodes.get(i).right = nodes.get(rightidx).right;
                    Node nd = new Node();
                    nd.sort = node.right.sort+"__1";
                    nd.name = '~';
                    nd.right =  nodes.get(rightidx).left;

                    nodes.get(rightidx).right = node;
                    nodes.get(rightidx).right = nd;

                    if(pre.right!=null && pre.right.sort.equals(node.sort)) nodes.get(preidx).right = nodes.get(rightidx);
                    if(pre.left!=null && pre.left.sort.equals(node.sort)) nodes.get(preidx).left = nodes.get(rightidx);
                    nodes.add(nd);
                }
                //~ ~
                else  if(node.right.name=='~') {
                    int preidx = LtlParse.getPreIdx(nodes, node.sort);
                    Node pre = nodes.get(preidx);

                    if(pre.right!=null && pre.right.sort.equals(node.sort)) nodes.get(preidx).right = node.right.right;
                    if(pre.left!=null && pre.left.sort.equals(node.sort)) nodes.get(preidx).left = node.right.right;

                    nodes.remove(node);
                    nodes.remove(node.right);
                }
                else if (node.right.name == '#') {
                    int preidx = LtlParse.getPreIdx(nodes, node.sort);
                    int rightidx = LtlParse.getPreIdx(nodes, node.right.sort);
                    Node pre  = nodes.get(preidx);
                    nodes.get(rightidx).name = '%';
                    if(pre.right!=null && pre.right.sort.equals(node.sort)) nodes.get(preidx).right = nodes.get(rightidx);
                    if(pre.left!=null && pre.left.sort.equals(node.sort)) nodes.get(preidx).left = nodes.get(rightidx);
                    nodes.remove(node);

                }else if (node.right.name == '%') {
                    int preidx = LtlParse.getPreIdx(nodes, node.sort);
                    int rightidx = LtlParse.getPreIdx(nodes, node.right.sort);
                    Node pre  = nodes.get(preidx);
                    nodes.get(rightidx).name = '#';
                    if(pre.right!=null && pre.right.sort.equals(node.sort)) nodes.get(preidx).right = nodes.get(rightidx);
                    if(pre.left!=null && pre.left.sort.equals(node.sort)) nodes.get(preidx).left = nodes.get(rightidx);
                    nodes.remove(node);

                }
            }
        }
        return nodes;
    }

}
