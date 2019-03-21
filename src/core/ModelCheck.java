import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by 14260 on 2019/1/7.
 */




public class ModelCheck {


    static boolean PASS = true;
    static class VNode {
        List<String> V;
        List<String> A;
        List<String> W;

        public VNode() {
        }

        public VNode(List<String> v, List<String> a, List<String> w) {
            V = v;
            A = a;
            W = w;
        }
    }

    public static void main(String[] args) throws Exception {

        init();
        List<String> ls = new ArrayList<String>();
        ls.add("{s0}");
        check(ls);
        if(PASS) System.out.println("PASS");
    }

    static List<VNode> vNodes = new ArrayList<VNode>();
    static void init() throws Exception {
        // [[ {3_1},{s0} ], [ {3_1:5_1} ], [ {3_1} ,  s ,  {3_1:5_1} ,  "emptyset" ], [ "emptyset",{b} ]]
        BufferedReader br = new BufferedReader(new FileReader(new File("output/result.txt")));
        String line = "";
        line=br.readLine();
        while(line!=null && !line.equals("")){
            String replace = line.replace(" ", "").substring(2);
            String replace1 = replace.substring(0,replace.length()-2);
            String[] split = replace1.split("],\\[");
            VNode node =  new VNode();
            node.V = transferToList(split[0]);
            node.A = transferToList(split[1]);
            node.W = transferToList(split[2]);
            vNodes.add(node);
            line = br.readLine();
        }
        br.close();

        BufferedReader br1 = new BufferedReader(new FileReader(new File("output/acc.txt")));
        line=br1.readLine();
        while(line!=null && !line.equals("")){
            ACC.add(line.trim());
            line=br1.readLine();
        }
        br1.close();
    }

    private static List<String> transferToList(String s) {
        String[] split = s.split(",");
        List<String> sss = new ArrayList<String>();
        for (String ss : split) {
            sss.add(ss);
        }
        return sss;
    }

    static class HashItem{
        List<String> V;
        Integer num = 0;
        public HashItem() {
        }
        public HashItem(List<String> v, Integer num) {
            V = v;
            this.num = num;
        }


    }

    static class RootItem{
        List<String> V;
        Integer num ;

        public RootItem() {
        }

        public RootItem(Integer num, List<String> v) {
            this.num = num;
            V = v;
        }
    }


    static int Num = 0;

    static Stack<HashItem> Hash = new Stack<HashItem>();
    static Stack<RootItem> Root = new Stack<RootItem>();
    static Stack<List<String>> Arc = new Stack<List<String>>();

    static void check(List<String> s0){
        Hash.push(new HashItem(s0,1));
        Root.push( new RootItem(1,new ArrayList<String>()));
        Arc.add(new ArrayList<String>());
        Explore(s0,1);
    }


    public static HashItem getByW(List<String> itm) {
        for (HashItem htm:Hash) {
            if(htm.V.size()!=itm.size()) continue;
            boolean ok = true;
            for(int n = 0 ; n < htm.V.size();n++){
                if(!htm.V.get(n).equals(itm.get(n))){
                    ok = false;
                }
            }
            if(ok) return htm;

        }
        return null;
    }

    static List<String> ACC = new ArrayList<String>();
    private static void Explore(List<String> s0, int order) {
        int i = 0;
        List<VNode> vns = getNext(s0);
        for( VNode v : vns ){
            HashItem w = getByW(v.W);
            if(w!=null){
                Hash.push(new HashItem(v.W,++Num));
                Root.push(new RootItem(Num,v.W));
                Explore(v.W,Num);
            }else if(w.num!=0) {
                RootItem R = Root.pop();
                List<String> B = union(R.V,w.V);
                i = R.num;
                while(i>w.num){
                    List<String> A = Arc.pop();
                    B = union(A,B);
                    RootItem T = Root.pop();
                    B = union(B,T.V);
                }
                Root.push(new RootItem(i,B));
                if(eq(B,ACC)) {
                    PASS = false;
                    System.out.println("ERR");
                }
            }
        }

        RootItem ri = Root.peek();
        if(order == i){
            Num = order-1;
            Root.pop();
            Arc.pop();
            Remeve(s0);
        }

    }

    private static boolean eq(List<String> b, List<String> acc) {
        if(b.size()!=acc.size()) return false;
        for(int n = 0 ; n < b.size();n++){
            boolean ok = true;
            if(!b.get(n).equals(acc.get(n))){
                ok = false;
            }
            if(ok) return true;
        }
        return false;
    }

    private static void Remeve(List<String> s0) {
        HashItem w = getByW(s0);
        if(w.num!=0) {
            List<VNode> vns = getNext(s0);
            for (VNode v : vns) {
                Remeve(v.V);
            }
        }
    }


    private static List<String> union(List<String> v, List<String> v1) {

        for(String s : v ){
            if(!v1.contains(s)) v1.add(s);
        }
        return v1;
    }

    private static List<VNode> getNext(List<String> s0) {
//        HashItem w = getByW(v.W);
        List<VNode> ns = new ArrayList<VNode>();

        for(VNode n: vNodes ){
            if(eq(n.V,s0)){
                ns.addAll(getNodesByV(n.W));
            }
        }
        return ns;
    }

    private static List<VNode> getNodesByV(List<String> w) {
        List<VNode> ns = new ArrayList<VNode>();
        for(VNode n: vNodes ){
            if(eq(n.V,w)){
                ns.add(n);
            }
        }
        return ns;
    }


}
