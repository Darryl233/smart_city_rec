import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.rs.client.UserHistoryClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class test {

    @Autowired
    private UserHistoryClient historyClient;

    @Test
    public void test1(){
        System.out.println(historyClient.getAllHistory());
    }

    /**
     * 将 logid resourcetype resourceid 转化为 logid ： resourcetype： resourceidList
     */
    @Test
    public void dataFormat(){
        HashMap<String, HashMap<Integer, List<String>>> lists = new HashMap<>();
        List<CommonHistory> allHistory = historyClient.getAllHistory();
        for (CommonHistory history : allHistory){
            if( !lists.containsKey(history.getLogId()) ){
                lists.put(history.getLogId(), new HashMap<>());
                for (int i=1; i < 9; i++){
                    lists.get(history.getLogId()).put(i, new ArrayList<>());
                }
            }
            lists.get(history.getLogId()).get(history.getResourceType()).add(history.getKgId());
        }
        System.out.println(lists);
    }
}
