package com.tgfc.tw.entity.model.response.orderHistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

public class GroupOrderItemResponse {

    private Page<OrderHistoryGetCountByOrderResponse> page;
    @JsonProperty("total")
    private TotalCalcResponse totalCalcResponse;

    public Page<OrderHistoryGetCountByOrderResponse> getPage() {
        return page;
    }

    public void setPage(Page<OrderHistoryGetCountByOrderResponse> page) {
        this.page = page;
    }

    public TotalCalcResponse getTotalCalcResponse() {
        return totalCalcResponse;
    }

    public void setTotalCalcResponse(TotalCalcResponse totalCalcResponse) {
        this.totalCalcResponse = totalCalcResponse;
    }
}
