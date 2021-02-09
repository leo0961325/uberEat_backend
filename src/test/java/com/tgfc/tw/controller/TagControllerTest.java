package com.tgfc.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.tw.MainApplication;
import com.tgfc.tw.entity.model.po.Tag;
import com.tgfc.tw.entity.repository.TagRepository;
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

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MainApplication.class)
@AutoConfigureMockMvc
public class TagControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;

    Cookie[] cookies;
    String xsrf;

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    TagRepository tagRepository;

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
    public void addTag() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("name", "123456");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tag/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(param))
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tag/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].id", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].name", is(param.get("name"))));

    }

    @Test
    @Transactional
    @Rollback
    public void deleteTag() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tag/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id","1")
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        Tag tag=tagRepository.getById(1);
        assertThat(tag).isNull();


    }

    @Test
    @Transactional
    @Rollback
    public void updateTag()throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("id", 1);
        param.put("name", "1234561");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tag/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(param))
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tag/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(param.get("id"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is(param.get("name"))));
    }

    @Test
    @Transactional
    @Rollback
    public void getTag() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tag/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("午後甜點")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("下午茶")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", is("早餐")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].name", is("午餐")));

    }
}