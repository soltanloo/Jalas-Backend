package DataManagers;

import java.util.ArrayList;
import java.util.List;

public class DataHelpers {
    public static String stringify(List<Integer> list) {
        String rs = "";
        boolean isFirst = true;
        for (int s : list) {
            if(isFirst) {
                rs = Integer.toString(s);
                isFirst = false;
            }
            else
                rs = rs + ',' + s;
        }
        return rs;
    }

    public static ArrayList<Integer> makeList(String rs){
        ArrayList<Integer> rl = new ArrayList<>();
        String[] a = rs.split(",");
        for (String str : a) {
            rl.add(Integer.parseInt(str));
        }
        return rl;
    }
}
