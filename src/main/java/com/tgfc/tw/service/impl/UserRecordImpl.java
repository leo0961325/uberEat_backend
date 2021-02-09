package com.tgfc.tw.service.impl;


import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.RecordResponse;
import com.tgfc.tw.entity.model.response.user.UserOrderRecordListResponse;
import com.tgfc.tw.entity.model.response.user.UserOrderRecordResponse;
import com.tgfc.tw.entity.repository.UserRepository;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserRecordImpl implements UserRecordService {

    @Autowired UserRepository userRepository;

    @Override
    public UserOrderRecordListResponse getUserOrderWeekRecord(){

        User user = userRepository.getUserById(ContextHolderHandler.getId());

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekday< 0)
            weekday= 0;
        java.sql. Date startTime = null;
        java.sql.Date endTime = null;

        //判斷當天星期幾
        if(weekday == 0) {
            startTime = new java.sql.Date(currentDate.getTime());
            endTime = new java.sql.Date(currentDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        }else if(weekday == 1){
            startTime = new java.sql.Date(currentDate.getTime() - 1 * 24 * 60 * 60 * 1000);
            endTime = new java.sql.Date(currentDate.getTime() + 6 * 24 * 60 * 60 * 1000);
        }else if(weekday == 2){
            startTime = new java.sql.Date(currentDate.getTime() - 2 * 24 * 60 * 60 * 1000);
            endTime = new java.sql.Date(currentDate.getTime() + 5 * 24 * 60 * 60 * 1000);
        }else if(weekday == 3){
            startTime = new java.sql.Date(currentDate.getTime() - 3 * 24 * 60 * 60 * 1000);
            endTime = new java.sql.Date(currentDate.getTime() + 4 * 24 * 60 * 60 * 1000);
        }else if(weekday == 4){
            startTime = new java.sql.Date(currentDate.getTime() - 4 * 24 * 60 * 60 * 1000);
            endTime = new java.sql.Date(currentDate.getTime() + 3 * 24 * 60 * 60 * 1000);
        }else if(weekday == 5){
            startTime = new java.sql.Date(currentDate.getTime() - 5 * 24 * 60 * 60 * 1000);
            endTime = new java.sql.Date(currentDate.getTime() + 2 * 24 * 60 * 60 * 1000);
        }else if(weekday == 6){
            startTime = new java.sql.Date(currentDate.getTime() - 6 * 24 * 60 * 60 * 1000);
            endTime = new java.sql.Date(currentDate.getTime() + 1 * 24 * 60 * 60 * 1000);
        }

        List<Map<String, Object>> getUserOrderRecord = userRepository.getUserOrderRecord(user.getId(),startTime,endTime);
        List<Map<String, Object>> getUserOrderRecordWeek = userRepository.getUserOrderRecordWeek(user.getId(),startTime,endTime);

        List<Integer> dayRepeat = new ArrayList<>();
        List<UserOrderRecordResponse> userOrderRecordResponseList = new ArrayList<>();

        //一周點餐
        for(Map userOrderRecordWeek : getUserOrderRecordWeek){
            boolean isRepeat = false;
            Date date = (Timestamp)userOrderRecordWeek.get("openDate");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;

            java.sql.Date date1 = new java.sql.Date(((Timestamp)userOrderRecordWeek.get("openDate")).getTime());
            if(!(dayRepeat.contains(w))){
                isRepeat = true;
            }
            dayRepeat.add(w);

            List<RecordResponse> recordResponseList = new ArrayList<>();
            if(isRepeat){
                int sum = 0;
                //每周點餐資料
                for(Map userOrderRecord : getUserOrderRecord) {
                    java.sql.Date date2 = new java.sql.Date(((Timestamp)userOrderRecord.get("openDate")).getTime());
                    if (date1.toLocalDate().equals(date2.toLocalDate())) {

                        RecordResponse recordResponse = new RecordResponse((String) userOrderRecord.get("productName"), (BigInteger) userOrderRecord.get("itemCount"), (int) userOrderRecord.get("productPrice"), (int) userOrderRecord.get("optionPrice"));
                        recordResponseList.add(recordResponse);
                        sum = sum + recordResponse.getTotalPrice();
                    }
                }

                UserOrderRecordResponse uorr = new UserOrderRecordResponse((Timestamp) userOrderRecordWeek.get("openDate"), w ,recordResponseList,sum);
                userOrderRecordResponseList.add(uorr);

            }

        }
        UserOrderRecordListResponse userOrderRecordListResponse = new UserOrderRecordListResponse(userOrderRecordResponseList);
        return userOrderRecordListResponse;
    }

}
