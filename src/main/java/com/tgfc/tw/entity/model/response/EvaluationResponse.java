package com.tgfc.tw.entity.model.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Map;

public class EvaluationResponse {

    private double rank;
    private int totalUsers;

    public static EvaluationResponse valueOf(Map<String,Object> mStoreReview){
        DecimalFormat df = new DecimalFormat("##.0");
        EvaluationResponse res = new EvaluationResponse();
        res.setRank( Double.parseDouble(df.format((((BigDecimal) mStoreReview.get("review")).doubleValue()) / ((BigInteger) mStoreReview.get("totalUsers")).intValue())));
        res.setTotalUsers(((BigInteger) mStoreReview.get("totalUsers")).intValue());
        return res;
    }

    public EvaluationResponse(){}

    public EvaluationResponse(double rank, int totalUsers) {
        this.rank = rank;
        this.totalUsers = totalUsers;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
}
