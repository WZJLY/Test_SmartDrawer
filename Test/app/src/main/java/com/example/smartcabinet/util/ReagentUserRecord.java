package com.example.smartcabinet.util;

/**
 * Created by WZJ on 2018/3/13.
 */

public class ReagentUserRecord {
    public String reagentId;
    public int operationType;
    public String operationTime;
    public String operator;
    public String reagentTotalSize;
    public String reagentSize;
    public String consumption;
    public ReagentUserRecord(String strReagentId, int strOperationType, String strOperationTime,
                   String strOperator,String strReagentTotalSize,String strReagentSize,
                   String strConsumption){
        /*
        * 试剂ID，操作类型（1-入柜、2-取用、3-归还、4-移除），操作时间；
        * 操作者，称重值，余量；
        * 消耗量；
      *
        * */
        this.reagentId = strReagentId;
        this.operationType = strOperationType;
        this.operationTime = strOperationTime;

        this.operator = strOperator;

        this.reagentSize = strReagentSize;
        this.reagentTotalSize = strReagentTotalSize;
        this.consumption = strConsumption;

    }
    public  int getOperationType()
    {
        return this.operationType;
    }

}
