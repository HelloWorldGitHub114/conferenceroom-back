package com.xd11z.myserver.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 展示会议室管理页面的下拉搜索框
 * 返回值为三个列表 分别为所有可能的序号、类型、容纳人数数据
 */
public class Conditions implements Serializable
{
    public List<Map<String,Integer>> floors;
    public List<Map<String,String>> types;
    public List<Map<String,Integer>> sizes;

    public Conditions(List<Map<String,Integer>> f, List<Map<String,String>> t, List<Map<String,Integer>> s)
    {
        this.floors=f;
        this.types=t;
        this.sizes=s;
    }
}