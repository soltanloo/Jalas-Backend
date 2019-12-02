package DataManagers;

import java.util.ArrayList;
import java.util.List;

public class DataHelpers {
    public static String stringify(List<String> list) {
        String rs = "";
        boolean isFirst = true;
        for (String s : list) {
            if(isFirst) {
                rs = s;
                isFirst = false;
            }
            else
                rs = rs + ',' + s;
        }
        return rs;
    }

    public static ArrayList<String> makeList(String rs){
        ArrayList<String> rl = new ArrayList<>();
        String[] a = rs.split(",");
        for (String str : a) {
            rl.add(str);
        }
        return rl;
    }
}
