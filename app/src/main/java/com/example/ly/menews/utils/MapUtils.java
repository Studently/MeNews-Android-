package com.example.ly.menews.utils;

import com.example.ly.menews.domain.News;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapUtils {
    /**
     * 使用 Map按value进行排序
     * @param oriMap
     * @return
     */

    public static Map<News, Double> sortMapByValue(Map<News, Double> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<News, Double> sortedMap = new LinkedHashMap<News, Double>();
        List<Map.Entry<News, Double>> entryList = new ArrayList<Map.Entry<News, Double>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<News, Double>> iter = entryList.iterator();
        Map.Entry<News, Double> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}

class MapValueComparator implements Comparator<Map.Entry<News, Double>> {

    @Override
    public int compare(Map.Entry<News, Double> me1, Map.Entry<News, Double> me2) {

        return me2.getValue().compareTo(me1.getValue());
    }
}

