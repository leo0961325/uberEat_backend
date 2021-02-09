package com.tgfc.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.tw.MainApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MainApplication.class)
@AutoConfigureMockMvc
public class GroupControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;

    Cookie[] cookies;
    String xsrf;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("account", "admin");
        param.put("password", "123456");
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param));
        MvcResult Resultu=mockMvc.perform(requestBuilder).andReturn();
        cookies=Resultu.getResponse().getCookies();
        session=(MockHttpSession)Resultu.getRequest().getSession();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("XSRF-TOKEN")){
                xsrf=  cookie.getValue();
            }
        }
    }
    @Test
    @Transactional
    @Rollback
    public void insertGroup()throws Exception {
        Map<String, Object> param = new HashMap<>();
        List<Integer> storeList=new ArrayList<>();
        List<Integer> userList=new ArrayList<>();
        List<Integer> floorIdList=new ArrayList<>();
        storeList.add(166);
        storeList.add(167);
        userList.add(1);
        userList.add(2);
        userList.add(3);
        floorIdList.add(1);
        floorIdList.add(2);
        param.put("name", "香雞城11");
        param.put("storeTel", "04-222310795");
        param.put("storeAddress", "青海路");
        param.put("remark", "還不錯");
        param.put("storeList",storeList);
        param.put("userList",userList);
        param.put("floorIdList",floorIdList);
        param.put("startTime", "2019-11-01 09:30:00");
        param.put("endTime", "2030-12-06 10:30:00");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/group/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(param))
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/timeList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .param("keyword","香雞城11")
                .param("tagId","1,2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].name",is("香雞城11")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[0].id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[0].name",is("午後甜點")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[1].id",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[1].name",is("下午茶")));
    }

    @Test
    @Transactional
    @Rollback
    public void updateGroup() throws Exception{
        Map<String, Object> param = new HashMap<>();
        List<Integer> userList=new ArrayList<>();
        List<Integer> floorIdList=new ArrayList<>();
        userList.add(1);
        userList.add(2);
        userList.add(3);
        floorIdList.add(1);
        floorIdList.add(2);
        param.put("id", "100");
        param.put("name", "香雞城22");
        param.put("userList",userList);
        param.put("floorIdList",floorIdList);
        param.put("startTime", "2019-12-01 09:30:00");
        param.put("endTime", "2030-12-31 10:30:00");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/group/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(param))
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/timeList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .param("keyword","香雞城22")
                .param("tagId","1,2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].name",is("香雞城22")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].startTime",is("2019-12-01 09:30:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].endTime",is("2030-12-31 10:30:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[0].id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[0].name",is("午後甜點")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[1].id",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[1].name",is("下午茶")));
    }

    @Test
    @Transactional
    @Rollback
    public void deleteGroup()throws Exception {
        Map<String, Object> param = new HashMap<>();
        List<Integer> storeList=new ArrayList<>();
        List<Integer> userList=new ArrayList<>();
        List<Integer> floorIdList=new ArrayList<>();
        storeList.add(166);
        storeList.add(167);
        userList.add(1);
        userList.add(2);
        userList.add(3);
        floorIdList.add(1);
        floorIdList.add(2);
        param.put("name", "香雞城11");
        param.put("storeTel", "04-222310795");
        param.put("storeAddress", "青海路");
        param.put("remark", "還不錯");
        param.put("storeList",storeList);
        param.put("userList",userList);
        param.put("floorIdList",floorIdList);
        param.put("startTime", "2019-11-01 09:30:00");
        param.put("endTime", "2030-12-06 10:30:00");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/group/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(param))
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/group/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .param("id","101")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
//新版沒有
//    @Test
//    @Transactional
//    @Rollback
//    public void getByTypeAndKeyword()throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/list")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .session(session)
//                .param("keyword","")
//                .param("isOwner","true")
//                .param("pageNumber","1")
//                .param("pageSize","10")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @Transactional
    @Rollback
    public void getGroupList()throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/timeList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .param("tagId","1,2")
                .param("keyword","")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[0].id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[0].name",is("午後甜點")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[1].id",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[1].name",is("下午茶")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[2].id",is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].tagList[2].name",is("早餐")));
    }

    @Test
    @Transactional
    @Rollback
    public void getInitData() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/getInitData")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tags[0].id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tags[0].name",is("午後甜點")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tags[1].id",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tags[1].name",is("下午茶")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tags[2].id",is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tags[2].name",is("早餐")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tags[3].id",is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tags[3].name",is("午餐")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[0].timeStatus",is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[0].status",is("提早收單")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[1].timeStatus",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[1].status",is("即將收單")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[2].timeStatus",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[2].status",is("進行中")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[3].timeStatus",is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[3].status",is("未開始")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[4].timeStatus",is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timeTypeResponses[4].status",is("逾時收單")));
    }

    @Test
    @Transactional
    @Rollback
    public void lock()throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/timeList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .param("keyword","皮卡丘嗨起來")
                .param("tagId","1,2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].name",is("皮卡丘嗨起來")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].groupResponses[0].locked",is(false)));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/group/lock")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .param("groupId","100")
                .param("status","true")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/timeList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .param("keyword","皮卡丘嗨起來")
                .param("tagId","1,2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].groupResponses[0].name",is("皮卡丘嗨起來")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].groupResponses[0].locked",is(true)));
    }

    //新版沒有
//    @Test
//    @Transactional
//    @Rollback
//    public void getOne()throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/get")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .session(session)
//                .param("id","236")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @Transactional
    @Rollback
    public void getFloorItemData() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/floorSelect")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name",is("3F")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name",is("24F")));
    }

    @Test
    @Transactional
    @Rollback
    public void getGroupManagerByFloorID() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/getGroupManager")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .param("floorIdList","1,2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name",is("超級管理員")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].englishName",is("admin")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].floorId",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name",is("一般管理員")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].englishName",is("manager")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].floorId",is(1)));
    }
}