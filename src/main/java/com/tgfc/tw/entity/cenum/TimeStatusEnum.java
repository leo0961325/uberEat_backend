package com.tgfc.tw.entity.cenum;

public enum TimeStatusEnum {
    TIME_EARLY(0, Status.EARLY),
    TIME_TODAY(1, Status.TODAY),
    TIME_AFTER(2, Status.AFTER),
    TIME_FUTURE(3, Status.FUTURE),
    TIME_TIMEOUT(4, Status.TIMEOUT);

    int timeStatus;
    String status;

    public class Status {
        public static final String EARLY = "近期收單";
        public static final String TODAY = "即將收單";
        public static final String AFTER = "進行中";
        public static final String FUTURE = "未開始";
        public static final String TIMEOUT = "已完成單";
    }

    public String value(int timeStatus) {
        this.timeStatus = timeStatus;
        return this.status;
    }

    TimeStatusEnum(int timeStatus, String status) {
        this.timeStatus = timeStatus;
        this.status = status;
    }

}
