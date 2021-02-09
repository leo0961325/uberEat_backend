package com.tgfc.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.tgfc.tw.MainApplication;
import com.tgfc.tw.entity.repository.*;
import com.tgfc.tw.service.GroupService;
import com.tgfc.tw.service.StorePictureService;
import com.tgfc.tw.service.impl.FileServiceImpl;
import com.tgfc.tw.service.impl.StoreInfoServiceImpl;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MainApplication.class)
@AutoConfigureMockMvc
public class ProductHistoryControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    HttpServletRequest request;

    MockHttpSession session;

    Cookie[] cookies;

    String xsrf;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("account", "manager");
        param.put("password", "123456");
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        cookies = mvcResult.getResponse().getCookies();
        session = (MockHttpSession) mvcResult.getRequest().getSession();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("XSRF-TOKEN")) {
                xsrf = cookie.getValue();
            }
        }
    }

    @Test
    public void getGroup() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orderHistory/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("groupId", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //驗證回傳資料
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(100)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void getGroupOrderItem() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orderHistory/getGroupOrderItem")
                .session(session)
                .param("groupId", "462")
                .param("storeId", "225")
                .param("keyword", "")
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .param("userId", "194")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //驗證回傳資料
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].id", is(116)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].name", is("DeleteOrderItemTest(Manager)")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].remark", is("")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].count", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].optionAllPrice", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].orderTotalPrice", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].users[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].users[0].name", is("manager")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].users[0].floor", is("3F")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].users[0].optionId", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.content[0].users[0].count", is(2)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void getGroupByOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orderHistory/getGroupByOrder")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("groupId", "100")
                .param("storeId", "166")
                .param("floorId", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //驗證回傳資料
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.id", is(166)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.storeName", is("我誰!!!我修改店家!!!!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.storeTel", is("香港3345678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.storeAddress", is("高屏地區")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.remark", is("我吃光光!!!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.tag[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.tag[0].name", is("午後甜點")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.tag[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.tag[1].name", is("下午茶")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.tag[2].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.tag[2].name", is("早餐")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.pictures[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.pictures[0].storePictureUrl", is("/api/images/1574650143166.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.pictures[0].storePicName", is("1574650143166.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.pictures[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.pictures[1].storePictureUrl", is("/api/images/1574650146064.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].store.pictures[1].storePicName", is("1574650146064.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].id", is(116)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].name", is("DeleteOrderItemTest(Manager)")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].price", is(500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].count", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].totalPrice", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].content[0].id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].content[0].name", is("無")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].content[0].price", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].content[0].count", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].content[0].totalPrice", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].countByOrder[0].content[0].orderPrice", is(0)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional       //舊API 無須修改
    public void getGroupByUser() throws Exception {
    }

    @Test
    @Rollback
    @Transactional
    public void addOrderItem() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orderHistory/put")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie("XSRF-TOKEN", xsrf))
                .header("X-XSRF-TOKEN", xsrf)
                .session(session)
                .param("optionId", "1")
                .param("groupId", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //取值驗證 數量+1
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orderHistory/getGroupByOrderUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie("XSRF-TOKEN", xsrf))
                .header("X-XSRF-TOKEN", xsrf)
                .session(session)
                .param("groupId", "100")
                .param("storeId", "166")
                .param("floorId", "1")
                .param("userId", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].count", is(2)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void delOrderItem() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orderHistory/remove")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie("XSRF-TOKEN", xsrf))
                .header("X-XSRF-TOKEN", xsrf)
                .session(session)
                .param("orderId", "116")
                .param("groupId", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void managerAddOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orderHistory/managerAdd")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie("XSRF-TOKEN", xsrf))
                .header("X-XSRF-TOKEN", xsrf)
                .session(session)
                .param("optionId", "4")
                .param("groupId", "100")
                .param("userId", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //取值驗證 數量+1
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orderHistory/getGroupByOrderUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie("XSRF-TOKEN", xsrf))
                .header("X-XSRF-TOKEN", xsrf)
                .session(session)
                .param("groupId", "100")
                .param("storeId", "166")
                .param("floorId", "1")
                .param("userId", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].count", is(3)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void managerDeleteOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orderHistory/managerDelete")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie("XSRF-TOKEN", xsrf))
                .header("X-XSRF-TOKEN", xsrf)
                .session(session)
                .param("orderId", "116")
                .param("groupId", "100")
                .param("userId", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void getGroupByOrderUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orderHistory/getGroupByOrderUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("groupId", "100")
                .param("storeId", "166")
                .param("floorId", "1")
                .param("userId", "3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //取值驗證
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].id", is(166)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].storeName", is("我誰!!!我修改店家!!!!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].storeTel", is("香港3345678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].storeAddress", is("高屏地區")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].remark", is("我吃光光!!!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].tag[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].tag[0].name", is("午後甜點")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].tag[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].tag[1].name", is("下午茶")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].tag[2].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].tag[2].name", is("早餐")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].pictures[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].pictures[0].storePictureUrl", is("/api/images/1574650143166.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].pictures[0].storePicName", is("1574650143166.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].pictures[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].pictures[1].storePictureUrl", is("/api/images/1574650146064.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].pictures[1].storePicName", is("1574650146064.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stores[0].pictures[1].storePicName", is("1574650146064.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].userId", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].userName", is("tt")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].totalPrice", is(80)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].totalPay", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].userName", is("tt")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].id", is(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].name", is("原味沙拉1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].price", is(70)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].option", is("不加醬")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].optionPrice", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].count", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].totalPrice", is(80)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].userId", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].countByUser[0].storeId", is(166)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].userPay[0].storeId", is(166)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].userPay[0].payStatus", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].userPay[0].pay", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userCountByGroup[0].userPay[0].totalPrice", is(80)))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @Rollback
    @Transactional
    public void getOrderUserList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orderHistory/getOrderUserList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("orderId", "116")
                .param("groupId", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username",is("manager")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name",is("一般管理員")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].englishName",is("manager")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].floorName",is("3F")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void getInitDataByUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orderHistory/getInitGroupByOrderUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("groupId", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupId",is(100)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void getInitDataForOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orderHistory/getInitDataForOrder")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("groupId", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.floors[0].id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.floors[0].name",is("3F")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stores[0].id",is(166)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stores[0].storeName",is("我誰!!!我修改店家!!!!")))


                .andDo(MockMvcResultHandlers.print());
    }
}