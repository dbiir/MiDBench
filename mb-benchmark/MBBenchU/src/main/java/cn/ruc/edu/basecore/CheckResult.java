package cn.ruc.edu.basecore;

import com.mongodb.DBObject;

import java.util.Iterator;
import java.util.Map;

public interface CheckResult {
    int CheckQuery(Iterator<DBObject> result, Map<String, Object> queryMap);

    int CheckStatistics(Iterator<DBObject> result, String statisticsType, String queryContent, String contentName);

    String CheckSorted(Iterator<DBObject> result, String query, String type, String sortWay);


}
