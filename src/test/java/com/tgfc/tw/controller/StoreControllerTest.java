package com.tgfc.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.tw.MainApplication;
import com.tgfc.tw.entity.repository.StoreRepository;
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

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MainApplication.class)
@AutoConfigureMockMvc
public class StoreControllerTest {


    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;

    Cookie[] cookies;
    String xsrf;

    @Autowired
    private StoreRepository storeRepository;

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
    public void addStore() throws Exception{
        Map<String, Object> param = new HashMap<>();
        List<Integer> picList=new ArrayList<>();
        List<Integer> tagList=new ArrayList<>();
        picList.add(11);
        tagList.add(10);
        tagList.add(3);
        param.put("storeName", "香雞城A11");
        param.put("storeTel", "04-222310795");
        param.put("storeAddress", "青海路");
        param.put("remark", "還不錯");
        param.put("picList",picList);
        param.put("tagList",tagList);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/store/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(param))
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.storeName",is(param.get("storeName"))));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/store/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("id", "169")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    @Rollback
    public void getStore() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/store/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("id", "168")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",is(168)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.storeName",is("帕莎蒂娜")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.storeTel",is("123123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.storeAddress",is("asdfa")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.remark",is("asdfs")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tag[0].id",is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tag[0].name",is("早餐")));
    }

    @Test
    @Transactional
    @Rollback
    public void removeStore() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/store/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .param("id","168")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        assertThat(storeRepository.getById(168)).isNull();

    }

    @Test
    @Transactional
    @Rollback
    public void updateStore()  throws Exception{
        Map<String, Object> param = new HashMap<>();
        List<Integer> picList=new ArrayList<>();
        List<Integer> tagList=new ArrayList<>();
        picList.add(11);
        picList.add(12);
        tagList.add(1);
        tagList.add(2);
        param.put("id", 167);
        param.put("storeName", "香雞城11");
        param.put("storeTel", "04-222310795");
        param.put("storeAddress", "青海路");
        param.put("remark", "還不錯");
        param.put("picList",picList);
        param.put("tagList",tagList);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/store/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .content(objectMapper.writeValueAsString(param))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/store/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("id", "167")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",is(167)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.storeName",is("香雞城11")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.storeTel",is("04-222310795")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.storeAddress",is("青海路")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.remark",is("還不錯")));
    }

    @Test
    @Transactional
    @Rollback
    public void getAllStore()  throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/store/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("storeName", "")
                .param("tagIdList", "")
                .param("pageNumber", "1")
                .param("pageSize", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/store/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("id", "166")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",is(166)));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/store/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("id", "167")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",is(167)));

    }
// 新版沒有
//    @Test
//    @Transactional
//    @Rollback
//    public void reviewStore()  throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/store/review")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .param("storeId", "166")
//                .param("review", "1")
//                .cookie(new Cookie("XSRF-TOKEN",xsrf))
//                .header("X-XSRF-TOKEN",xsrf)
//                .session(session)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//    }
// 新版沒有
//    @Test
//    @Transactional
//    @Rollback
//    public void commentStore()  throws Exception{
//        Map<String, Object> param = new HashMap<>();
//        param.put("storeId", 166);
//        param.put("storeName", "香雞城11");
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/store/comment")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(param))
//                .cookie(new Cookie("XSRF-TOKEN",xsrf))
//                .header("X-XSRF-TOKEN",xsrf)
//                .session(session)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }
// 新版沒有
//    @Test
//    @Transactional
//    @Rollback
//    public void deleteComment()  throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/store/deleteComment")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .param("id", "1")
//                .cookie(new Cookie("XSRF-TOKEN",xsrf))
//                .header("X-XSRF-TOKEN",xsrf)
//                .session(session)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }
}