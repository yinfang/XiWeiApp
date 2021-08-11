package com.kunbo.xiwei.modle;

/**
 * 站长 班次
 */

public class StationTeamEntity {
    private String teamId;
    private String timeRange;
    private String monitor;
    private String monitorId;
    private String groupType;
    private String number;

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    @Override
    public String toString() {
        return "StationTeamEntity{" +
                "teamId='" + teamId + '\'' +
                ", timeRange='" + timeRange + '\'' +
                ", monitor='" + monitor + '\'' +
                ", monitorId='" + monitorId + '\'' +
                ", groupType='" + groupType + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
