import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 14260 on 2019/1/6.
 */
public class Main {

    public static void main(String[] args) throws IOException {
//      String input_expression = "~ p U (X q)" ;
//      String input_expression = "~ p U r U X q ";
//      String input_expression = "p U r U q ";
//        String input_expression = "p U (q U s)";
        String input_expression = args[0];
        System.out.println("待测试的中缀表达式为：" + input_expression);
        //获得LTL的语法分析树
        List<Node> nodes1 = LtlParse.toPrefixExpression(input_expression);
        Node head = nodes1.get(nodes1.size()-1);

//        System.out.println(Node.getPre(head,"2").sort);

//        优化树为 [ U,V,N,W,~,X ]
//        Node head = nodes1.get(nodes1.size()-1);

        List<Node> nodes = LtlSimply.OPT(nodes1);

        System.out.println("11");

        System.out.println(head.sort);
        head = nodes.get(LtlParse.getByIdx(nodes,head.sort));

        //添加状态节点的语法分析书

        List<Node> nodes2 = LtlParse.parse(nodes);

        head = nodes.get(LtlParse.getByIdx(nodes2,head.sort+"_1"));

        Node node = head;
        //获取Bool式的语法分析树
        System.out.println(node.sort);
        ArrayList<String> ss = new ArrayList<String>();
        Node r = LtlParse.R(nodes2, node);
//        lds.add(r);
        setLds(node);

        ss.add(node.sort);
//      sts.add(ss);

        //计算出Tuple三元组
//      List<Tuple> tps =  LtlParse.OpCpt(r);
        cpt(nodes2,ss,r);
        System.out.println();
        //输入状态文件
        BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output/transition.txt")));
        for (List<String> tps: tpss.keySet()) {
            LtlParse.getStates(bw1,tps, tpss.get(tps));
        }
        bw1.close();

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output/acc.txt")));
        if(LtlParse.states.size()==0) bw.write("\"emptyset\"");
        for (Node s :LtlParse.states) {
            bw.write(s.sort);
            bw.newLine();
        }
        bw.close();

    }


    static List<List<String>> sts = new ArrayList<List<String>>();
    static List<Node> lds= new ArrayList<Node>();

    static void setLds(Node node){
        if(node.left!=null){
            lds.add(node.left);
            setLds(node.left);
        }
        if(node.right!=null){
            lds.add(node.right);
            setLds(node.right);
        }
    }


    static boolean check(List<String> ls){

        for (List<String> s : sts){
            if(s.size()==ls.size()){
                boolean ok = true;
                for( int i = 0 ; i < s.size();i++){
                    if(!s.get(i).equals(ls.get(i))){
                        ok = false;
                    }
                }
                if(ok){
                    return true;
                }
            }
        }
        return false;
    }

    static Node  getNode(List<Node> nodes2,String sort){
        for (Node s : nodes2) {
            if ( s.state && s.sort.equals(sort)){
                return s;
            }
        }
        return null;

    }

    static Map<List<String>,List<Tuple>> tpss = new HashMap<List<String>,List<Tuple>>();
    static void cpt(List<Node> nodes2,List<String> st,Node r){
        if(!check(st)) sts.add(st);
        List<Tuple> tps =  LtlParse.OpCpt(r);
        tpss.put(st,tps);
        for (Tuple t : tps){
           boolean ok = false;
           for( String s :t.next_acc){
               if(check(t.next_acc)){
                   ok = true;
               }
           }
           if(!ok ){
               Node ndz= createNode(nodes2,t.next_acc);
//               ArrayList<Node> nls = new ArrayList<Node>();
               if(ndz!=null) cpt(nodes2,t.next_acc,ndz);
           }
        }

    }

    private static Node createNode(List<Node> nodes2,List<String> next_acc) {

        List<Node> nds = new ArrayList<Node>();
        for ( String nac : next_acc) {
            Node node = getNode(nodes2,nac);
            if(node!=null){
                nds.add(node);
            }
        }
        if(nds.size()>0) {
            List<Node> ndss = new ArrayList<Node>();
            for (Node nd :nds) {
                ndss.add(LtlParse.R(nodes2,nd));
            }
            return simpl(ndss);
        }else
            return null;
    }

    static Node simpl(List<Node> nds){
        if(nds.size()<2){
            return nds.get(0);
        }
        else{
            Node n = new Node();
            n.sort = nds.get(0).sort+"_"+nds.get(1).sort+"_"+1;
            n.name = '*';
            n.state = false;
            n.left =  nds.get(0);
            n.right = nds.get(1);
            nds.remove(0);
            nds.remove(0);
            nds.add(n);
            return simpl(nds);
        }
    }
}
