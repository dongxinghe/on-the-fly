import java.util.ArrayList;
import java.util.List;

/**
 * Created by 14260 on 2019/1/6.
 */
//元组
public class Tuple {
    //原子命题
    List<String> x = new ArrayList<String>();
    //不可接受状态列表
    List<String> can_nacc = new ArrayList<String>();
    //下一个状态列表
    List<String> next_acc = new ArrayList<String>();
}
