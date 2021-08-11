package com.kunbo.xiwei.modle;

/**
 * 班组值班记录 入口车道
 */

public class CarInOutEntity {
    private String lane;
    private String laneId;
    //private String timeRange;
    private String feeMans;
    private String feeManIds;

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getLaneId() {
        return laneId;
    }

    public void setLaneId(String laneId) {
        this.laneId = laneId;
    }

   /* public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }
*/
    public String getFeeMans() {
        return feeMans;
    }

    public void setFeeMans(String feeMans) {
        this.feeMans = feeMans;
    }

    public String getFeeManIds() {
        return feeManIds;
    }

    public void setFeeManIds(String feeManIds) {
        this.feeManIds = feeManIds;
    }

    @Override
    public String toString() {
        return "CarInOutEntity{" +
                "lane='" + lane + '\'' +
                ", laneId='" + laneId + '\'' +
                //", timeRange='" + timeRange + '\'' +
                ", feeMans='" + feeMans + '\'' +
                ", feeManIds='" + feeManIds + '\'' +
                '}';
    }
}
