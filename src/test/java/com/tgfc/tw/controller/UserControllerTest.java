package com.tgfc.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.tw.MainApplication;
import com.tgfc.tw.entity.model.po.UserPay;
import com.tgfc.tw.entity.repository.UserPayRepository;
import net.minidev.json.JSONObject;
import org.h2.command.dml.Update;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
//import sun.jvm.hotspot.memory.HeapBlock;
//import sun.net.www.protocol.ftp.Handler;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.servlet.http.Cookie;
import java.util.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MainApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {


    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;

    Cookie[] cookies;
    String xsrf;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    UserPayRepository userPayRepository;

    @BeforeEach
    @Test
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
//        xsrf=Resultu.getResponse().getHeader("Set-Cookie");
//        System.out.println(xsrf);
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("XSRF-TOKEN")){
                xsrf=  cookie.getValue();
            }

        }

    }


    @Test
    @Transactional
    @Rollback
    public void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", is("admin")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("超級管理員")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.englishName", is("admin")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.floorId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allFloors[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allFloors[0].name", is("3F")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allFloors[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allFloors[1].name", is("24F")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.permissions.1", is("超級管理者")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.permissions.2", is("一般管理者")));

    }


    @Test
    @Transactional
    @Rollback
    public void list() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("keyword", "t").param("pageNumber", "1").param("pageSize", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].username", is("tt")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("tt")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].englishName", is("tt")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].floorName", is("3F")));
  }

    @Test
    @Transactional
    @Rollback
    public void update() throws Exception {
        Map<String, Object> param = new HashMap<>();
        List<Integer> list=new ArrayList<>();
        list.add(1);
        param.put("id", 3);
        param.put("name", "Amy2");
        param.put("englishName", "Amy2");
        param.put("floorId", 2);
        param.put("permissions",list);

        JSONObject request = new JSONObject();
        request.put("id", 3);
        request.put("name", "Amy2");
        request.put("englishName", "Amy2");
        request.put("floorId", 2);
        request.put("permissions",list);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .content(objectMapper.writeValueAsString(param))
//                .content(request.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("id", "3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("id")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(param.get("id"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(param.get("name"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.englishName", is(param.get("englishName"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.floorId", is(param.get("floorId"))));




    }


    //新版沒有
//    @Test
//    @Transactional
//    @Rollback
//    public void addAllUserByLdap() throws Exception{
//
//        List<String> list=new ArrayList<>();
//        list.add("a");
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/addLdapUser")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .cookie(new Cookie("XSRF-TOKEN",xsrf))
//                .header("X-XSRF-TOKEN",xsrf)
//                .param("a")
//                .session(session)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @Transactional
    @Rollback
    public void getPermission() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getPermission")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", is("ROLE_MANAGER")));

    }

//    @Test
//    @Transactional
//    @Rollback
//    public void payStatus() throws Exception{
//        Map<String, Object> param = new HashMap<>();
//        List<Integer> list=new ArrayList<>();
//        list.add(166);
//        param.put("groupId", 100);
//        param.put("userId", 3);
//        param.put("pay", 100);
//        param.put("storeId",list);
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/pay")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .cookie(new Cookie("XSRF-TOKEN",xsrf))
//                .header("X-XSRF-TOKEN",xsrf)
//                .session(session)
//                .content(objectMapper.writeValueAsString(param))
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", is(true)));
//        UserPay userPay=userPayRepository.getById(1);
//        assertEquals(userPay.getGroup().getId(), param.get("groupId"));
//        assertEquals(userPay.getUser().getId(), param.get("userId"));
//        assertEquals(userPay.getPay(), param.get("pay"));
//        assertEquals(userPay.getTotalPrice(), 80);
//        assertEquals(userPay.getStore().getId(), list.get(0));
//
//    }

//    @Test
//    @Transactional
//    @Rollback
//    public void payTotal() throws Exception{
//        Map<String, Object> param = new HashMap<>();
//        List<Integer> list=new ArrayList<>();
//        list.add(166);
//        param.put("groupId", 100);
//        param.put("userId", 3);
//        param.put("pay", 100);
//        param.put("storeId",list);
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/payTotal")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
////                .cookie(new Cookie("XSRF-TOKEN",xsrf))
//                .header("X-XSRF-TOKEN",xsrf)
//                .cookie(cookies)
//                .session(session)
//                .content(objectMapper.writeValueAsString(param))
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", is(true)));
//
//        UserPay userPay=userPayRepository.getByGroupIdaAndUserId((int)param.get("groupId"),(int)param.get("userId"),list.get(0));
//        assertEquals(userPay.getGroup().getId(), param.get("groupId"));
//        assertEquals(userPay.getUser().getId(), param.get("userId"));
//        assertEquals(userPay.getTotalPrice(), 80);
//        assertEquals(userPay.getStore().getId(),list.get(0));
//    }



    @Test
    @Transactional
    @Rollback
    public void getRole() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getRole")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code", is("ROLE_MANAGER")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("一般管理者")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(-1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].code", is("ROLE_NORMAL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("一般使用者")));



    }
}