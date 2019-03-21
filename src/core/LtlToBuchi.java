import java.util.ArrayList;
import java.util.List;

/**
 * Created by 14260 on 2019/1/6.
 */
public class LtlToBuchi {


    public static List<Tuple> XToTuple(String x){
        Tuple t = new Tuple();
        t.next_acc.add(x.replace("R_X_",""));
        System.out.println(x.replace("R_X_",""));
        List<Tuple> tuples = new ArrayList<Tuple>();
        tuples.add(t);
        return tuples;
    }

    public static List<Tuple> AToTuple(String a){
        Tuple t = new Tuple();
        t.can_nacc.add(a.replace("a_",""));
        List<Tuple> tuples = new ArrayList<Tuple>();
        tuples.add(t);
        return tuples;
    }

    public static List<Tuple> QToTuple(String q){
        if(q.equals("false")){
            List<Tuple> tuples = new ArrayList<Tuple>();
            return tuples;
        }
        Tuple t = new Tuple();
        t.x.add(q);
        List<Tuple> tuples = new ArrayList<Tuple>();
        tuples.add(t);

        return tuples;
    }



    public static List<Tuple> or(List<Tuple> tuples1,List<Tuple> tuples2){
        for (Tuple t :tuples2) {
            tuples1.add(t);
        }
        return tuples1;
    }


    public static List<Tuple> and(List<Tuple> tuples1,List<Tuple> tuples2){

        List<Tuple> tps = new ArrayList<Tuple>();
        if(tuples1.size()==0 || tuples2.size() == 0) return tps;
        for (Tuple t1 :tuples1) {
            for (Tuple t2 :tuples2) {
                for(String s : t2.x){
                    if(!t1.x.contains(s)) t1.x.add(s);
                }
                for(String s : t2.can_nacc){
                    if(!t1.can_nacc.contains(s)) t1.can_nacc.add(s);
                }
                for(String s : t2.next_acc){
                    if(!t1.next_acc.contains(s)) t1.next_acc.add(s);
                }
                tps.add(t1);
            }
        }
        return tps;
    }
}
