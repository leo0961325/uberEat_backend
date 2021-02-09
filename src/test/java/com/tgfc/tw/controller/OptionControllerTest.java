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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MainApplication.class)
@AutoConfigureMockMvc
public class OptionControllerTest {

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
    @Rollback
    @Transactional
    public void addOneOption() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("name", "超級賽亞炒飯");
        param.put("price", 143);
        param.put("orderId", 112);
        param.put("groupId", 100);
        param.put("userId", 3);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/option/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(param))
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/option/plusList")
                .param("groupId","100")
                .param("orderId","112")
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("超級賽亞炒飯")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price", is(143)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].count", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].totalPrice", is(203)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].orderPrice", is(60)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].user.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].user.floorId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].user.name", is("一般管理員")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].user.englishName", is("manager")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void managerAddOneOption() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("name", "超級無敵霹靂經理級賽亞炒飯");
        param.put("price", 143);
        param.put("orderId", 111);
        param.put("groupId", 100);
        param.put("userId", 3);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/option/managerAdd")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(param))
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/option/plusList")
                .param("groupId","100")
                .param("orderId","111")
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("超級無敵霹靂經理級賽亞炒飯")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price", is(143)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].count", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].totalPrice", is(213)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].orderPrice", is(70)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void getOptionByOrder() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/option/list")
                .param("orderId","116")
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id",is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name",is("無")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price",is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId",is(116)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void updateOption() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", 1);
        param.put("name", "超級修改炒飯");
        param.put("price", 0);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/option/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(param))
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/option/plusList")
                .param("groupId","100")
                .param("orderId","111")
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("超級修改炒飯")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].count", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice", is(70)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderPrice", is(70)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteOption() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/option/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id","1")
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void getOption() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/option/get")
                .param("optionId","1")
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Rollback
    @Transactional
    public void getOptionByGroup()throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/option/plusList")
                .param("groupId","100")
                .param("orderId","111")
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("不加醬")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].count", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice", is(80)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderPrice", is(70)))
                .andDo(MockMvcResultHandlers.print());
    }
}