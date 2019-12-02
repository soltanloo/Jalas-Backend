package DataManagers;

import java.util.ArrayList;
import java.util.List;

public class DataHelpers {
    public static String stringify(List<String> list) {
        String rs = "";
        for (String s : list) {
            rs = rs + ',' + s;
        }
        rs.substring(1);
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
