import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Hello world!
 *
 */
public class LtlParse {

    static String ops = "XFG~UNWV";
    static String ops_1 = "XFG~";
    static String ops_2 = "UNWV";

    static List<Node> states = new ArrayList<Node>();
    static List<Node> fs = new ArrayList<Node>();
    static HashMap<Character, Integer> maps = new HashMap<Character, Integer>();

    static {
        maps.put('X', 200); // next
        maps.put('F', 200); // future
        maps.put('G', 200); // globally
        maps.put('~', 200); // not
        maps.put('U', 100); // util
        maps.put('N', 100); // intersection
        maps.put('V', 100); // Release
        maps.put('W', 100); // Union
    }


    /**
     * 将中缀表达式转化为前缀表达式 X next ~ not U util G Global F Future N intersection W union
     *
     * @param input 要被转化的中缀表达式
     */
    public static List<Node> toPrefixExpression(String input) {
        int len = input.length();//中缀表达式的长度
        char c, tempChar;
        Stack<Character> s1 = new Stack<Character>();
        Stack<Character> expression = new Stack<Character>();
        //从右至左扫描表达式
        for (int i = len - 1; i >= 0; --i) {
            c = input.charAt(i);
            if (Character.isLetter(c) && ops.indexOf(c) == -1) {
                expression.push(c);
            } else if (ops.indexOf(c) != -1) {
                while (!s1.isEmpty()
                        && s1.peek() != ')'
                        && maps.get(c) <= maps.get(s1.peek())) {
                    expression.push(s1.pop());
                }
                s1.push(c);
            } else if (c == ')') {
                s1.push(c);
            } else if (c == '(') {
                while ((tempChar = s1.pop()) != ')') {
                    expression.push(tempChar);
                    if (s1.isEmpty()) {
                        throw new IllegalArgumentException("bracket dosen't match, missing right bracket ')'.");
                    }
                }
            } else if (c == ' ') {
                // 如果表达式里包含空格则不处理空格
            } else {
                throw new IllegalArgumentException("wrong character '" + c + "'");
            }
        }

        while (!s1.isEmpty()) {
            tempChar = s1.pop();
            expression.push(tempChar);
        }
        String str = "";
        List<Node> nodes = new ArrayList<Node>();

        int i = 1;
        Stack<Node> cs = new Stack<Node>();
        Stack<Character> expressions = new Stack<Character>();
        while (!expression.isEmpty()) {
            Character cc = expression.pop();
            expressions.push(cc);
            str += cc + " ";
        }

        int no = 0;
        // To Nodes
        while (!expressions.isEmpty()) {

            Character cc = expressions.pop();
            if (ops_1.indexOf(cc) != -1) {
                Node node = new Node();
                node.name = cc;
                node.sort = (++no) + "";

                if (cs.size() != 0) {
                    Node pop = cs.pop();
                    node.right = pop;
                    nodes.add(pop);
                } else if (nodes.size() != 0) {
                    node.right = nodes.get(nodes.size() - 1);
                }
                nodes.add(node);
            } else if (ops_2.indexOf(cc) != -1) {
                Node node = new Node();
                node.name = cc;
                node.sort = (++no) + "";

                if (nodes.size() != 0 && cs.size() != 0) {
                    boolean ok = false;
                    for (int j = nodes.size()-1; j >=0; j--) {
                        if (ops.indexOf(nodes.get(j).name) != -1) {
                            node.right = nodes.get(j);
                            ok = true;
                        }
                    }
                    if (!ok) node.right = nodes.get(nodes.size() - 1);
                    Node pop = cs.pop();
                    node.left = pop;
                    nodes.add(pop);
                } else if (nodes.size() != 0 && cs.size() == 0) {
                    int n = 1;
                    for (int j = nodes.size()-1; j >=0; j--) {
                        if (ops.indexOf(nodes.get(j).name) != -1) {
                            if (n == 1) {
                                node.left = nodes.get(j);
                                n++;
                            } else if (n == 2) {
                                node.right = nodes.get(j);
                                n++;
                            }
                        }
                    }
                } else if (nodes.size() == 0 && cs.size() != 0) {
                    Node pop = cs.pop();
                    node.left = pop;
                    nodes.add(pop);
                    Node pop1 = cs.pop();
                    node.right = pop1;
                    nodes.add(pop1);
                }
                nodes.add(node);
            } else {
                Node node = new Node();
                node.name = cc;
                node.sort = (++no) + "";
                cs.push(node);
            }
        }
        System.out.println(str);
        return nodes;

    }

    /**
     * 前记节点
     *
     * @param nodes
     * @param child_sort
     * @return
     */
    public static int getPreIdx(List<Node> nodes, String child_sort) {
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            if ((n.left != null && n.left.sort.equals(child_sort)) || (n.right != null && n.right.sort.equals(child_sort))) {
                return i;
            }
        }
        return -1;
    }

    public static int getByIdx(List<Node> nodes, String child_sort) {
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            if (n.sort.equals(child_sort)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 转化为新的语法树
     *
     * @param nodes
     * @return
     */
    public static List<Node> parse(List<Node> nodes) {

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (!node.state && (node.name == 'V' || node.name == 'U')) {
                int preidx = getPreIdx(nodes, node.sort);
                if (preidx != -1) {
                    Node pre = nodes.get(preidx);
                    if(!nodes.get(preidx).state) {
                        Node nw = new Node();
                        nw.sort = node.sort + "_1"; //代表true
                        nw.state = true;
                        nw.right = node;
                        if (pre.right!=null && pre.right.sort == node.sort){
                            nodes.get(preidx).right = nw;

                        }
                        if (pre.left!=null && pre.left.sort == node.sort){
                            nodes.get(preidx).left = nw;
                        }

                        nodes.add(nw);
                       if(node.name == 'U' && !states.contains(nw)) states.add(nw);
                    }
                } else {
                    Node nw = new Node();
                    nw.sort = node.sort + "_1"; //代表true
                    nw.state = true;
                    nw.right = node;
                    nodes.add(nw);
                    if(node.name == 'U' && !states.contains(nw)) states.add(nw);
                }
                if(!node.right.state && ("XUV").indexOf(node.right.name)!=-1) {

                    Node nw1 = new Node();
                    nw1.sort = node.sort + "_2"; //代表true
                    nw1.state = true;
                    nw1.right = node.right;
                    nodes.get(i).right = nw1;
                    nodes.add(nw1);
                }
                if(!node.left.state && ("XUV").indexOf(node.left.name)!=-1 ) {

                    Node nw2 = new Node();
                    nw2.sort = node.sort + "_3"; //代表true
                    nw2.state = true;
                    nw2.right = node.left;
                    nodes.get(i).left = nw2;
                    nodes.add(nw2);
                }

            } else if (!node.state && (node.name == 'X') ) {
                if(!node.right.state) {
                    Node nw = new Node();
                    nw.sort = node.sort + "_1"; //代表true
                    nw.state = true;
                    nw.right = node.right;
                    node.right = nw;
                    nodes.add(nw);
//                    states.add(nw);
                }else if(!states.contains(node.right)) states.add(node.right);
            }
        }
        return nodes;
    }


    static List<Node>  snodes = new ArrayList<Node>();
    static  int nm = 0;

    public static Node R(List<Node> nodes, Node node) {
        if (node.state && !node.sort.equals("false") &&  !node.sort.equals("true") ) {
            return R(nodes, node.right);
        }

        switch (node.name) {
            case 'U':
                return Rfug(nodes,node);
            case 'V':
                return Rfvg(nodes,node);
            case 'N':
                return Rfng(nodes,node);
            case 'W':
                return Rfwg(nodes,node);
            case 'X':
                return Rx_f(nodes,node);
            case '~':
                return R_f(nodes,node);
            case '#':
                Node nd1 = new Node();
                nd1.sort = "false";
                nd1.state = true;
                snodes.add(nd1);
                return nd1 ;
            case '%':
                Node nd2 = new Node();
                nd2.sort = "true";
                nd2.state = true;
                snodes.add(nd2);
                return nd2 ;
            default:
                if(!node.state) {
                    Node nd = new Node();
                    nd.sort = node.name + "";
                    nd.state = true;
                    snodes.add(nd);
                    return nd;
                }else{
                    Node nd = new Node();
                    nd.sort = node.sort;
                    nd.state = true;
                    snodes.add(nd);
                    return nd;
                }
        }
    }


    public static Node Rfug(List<Node> nodes, Node node) {
        int preIdx = getPreIdx(nodes, node.sort);
        Node pre = nodes.get(preIdx);
        Node nd = new Node();
        nd.name = '+';
        nd.sort = (++nm)+"";
        nd.left = R(nodes, node.right);

        Node nd4 = new Node();
        nd4.sort = "R_X_" +pre.sort;
        nd4.state = true;

        Node nd2 = new Node();
        nd2.name = '*';
        nd2.sort = (++nm)+"";
        nd2.left = R(nodes, node.left);;
        nd2.right = nd4;

        Node nd3 = new Node();
        nd3.sort = "a_" + pre.sort;
        nd3.state = true;

        Node nd1 = new Node();
        nd1.name = '*';
        nd1.sort = (++nm)+"";
        nd1.left =  nd3;
        nd1.right = nd2;
        nd.right = nd1;

        snodes.add(nd2);
        snodes.add(nd3);
        snodes.add(nd1);
        snodes.add(nd);
//      String Ustr = R(nodes, node.right) + "+" + "a_" + Upre.sort + "*(" + R(nodes, node.left) + ")*R_X_" + Upre.sort;
        return nd;
    }

    public static Node Rfvg(List<Node> nodes, Node node) {
        int preIdx = getPreIdx(nodes, node.sort);
        Node pre = nodes.get(preIdx);
        Node nd = new Node();
        nd.name = '+';
        nd.sort = (++nm)+"";


        Node nd2 = new Node();
        nd2.name = '*';
        nd2.sort = (++nm)+"";
        nd2.left = R(nodes, node.left);
        nd2.right = R(nodes, node.right);

        Node nd1 = new Node();
        nd1.sort = "R_X_" + pre.sort;
        nd1.state = true;

        Node nd3 = new Node();
        nd3.name = '*';
        nd3.sort = (++nm)+"";
        nd3.left =  R(nodes, node.right);
        nd3.right = nd1;

        nd.left =  nd2;
        nd.right = nd3;

        snodes.add(nd2);
        snodes.add(nd3);
        snodes.add(nd1);
        snodes.add(nd);
//      String Vstr = "("+R(nodes, node.left) + ")*(" + R(nodes, node.right) + ")+(" + R(nodes, node.right) + ")*R_X_" + Vpre.sort;
        return nd;
    }

    public static Node Rfng(List<Node> nodes, Node node) {
        Node nd = new Node();
        nd.name = '*';
        nd.sort = (++nm)+"";
        nd.left = R(nodes, node.left);
        nd.right = R(nodes, node.right);
        snodes.add(nd);
//                String Nstr =  R(nodes, node.left) + "*" + R(nodes, node.right);
        return nd;
    }

    public static Node R_f(List<Node> nodes, Node node) {
        Node nd = new Node();
        nd.sort = "~" + R(nodes, node.right);
        nd.state = true;
        snodes.add(nd);
//      String str_= "~" + R(nodes, node.right);
        return nd;
    }

    public static Node Rx_f(List<Node> nodes, Node node) {
        Node nd = new Node();
        nd.sort = "R_X_" + node.right.sort;
        nd.state = true;
        nd.right = R(nodes, node.right);
        snodes.add(nd);
        return nd;
    }

    public static Node Rfwg(List<Node> nodes, Node node) {
        Node nd = new Node();
        nd.name = '+';
        nd.sort = (++nm)+"";
        nd.left = R(nodes, node.left);
        nd.right = R(nodes, node.right);
        snodes.add(nd);
//      String Wstr =  R(nodes, node.left) + "+" + R(nodes, node.right);
        return nd;
    }

    public static List<Tuple> OpCpt(Node node){
        if(!node.state && node.name=='+'){
            return LtlToBuchi.or(OpCpt(node.left),OpCpt(node.right));
        }else if(!node.state && node.name=='*'){
            return LtlToBuchi.and(OpCpt(node.left),OpCpt(node.right));
        }else if(node.state && node.sort.startsWith("a_")){
            return LtlToBuchi.AToTuple(node.sort);
        }else if(node.state && node.sort.startsWith("R_X_")){
            return LtlToBuchi.XToTuple(node.sort);
        }else if(node.state){
            System.out.println(node.sort);
            return LtlToBuchi.QToTuple(node.sort);
        }else {
            List<Tuple> tuples = new ArrayList<Tuple>();
            return tuples;
        }
    }


    public static void getStates(BufferedWriter bw ,List<String> st,List<Tuple> tps) throws IOException {

        if (tps.size() > 0) {
            for (Tuple t : tps) {

                String str = "(" ;

                if (st.size() == 0){
                    str += "\"emptyset\",";
                } else {
                    str += "{";
                    int n = 0;
                    for (String c : st) {
                        if (n == 0) str += c;
                        else str += (" | " + c);
                        n++;
                    }
                    str += "};";
                }

                str+= (t.x.size()==0 ? "true":t.x.get(0) )+ ";";

                if (states.size() == 0 || states.size()==t.can_nacc.size()) {
                    str += "\"emptyset\",";
                } else {
                    str += "{";
                    int n = 0;
                    for (Node c : states) {
                        if (!t.can_nacc.contains(c.sort)) {
                            if (n == 0) str += c.sort;
                            else str += (" | " + c.sort);
                            n++;
                        }
                    }
                    str += "};";
                }

                if (t.next_acc.size() == 0) {
                    str += "\"emptyset\"";
                } else {
                    str += "{";
                    int n = 0;
                    for (String tt : t.next_acc) {
                        if (n == 0) str += tt;
                        else str += (" | " + tt);
                        n++;
                    }
                    str += "}";
                }

                str += ")";
                bw.write(str);
                bw.newLine();
            }
        }
    }

}