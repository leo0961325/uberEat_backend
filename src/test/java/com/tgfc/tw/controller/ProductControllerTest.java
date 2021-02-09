package com.tgfc.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.tw.MainApplication;
import com.tgfc.tw.entity.model.po.Product;
import com.tgfc.tw.entity.repository.OrderRepository;
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
import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MainApplication.class)
@AutoConfigureMockMvc
public class ProductControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    OrderRepository orderRepository;

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
    public void getList() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("storeId", "167")
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .param("keyword", "")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(114)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("原味沙拉3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price", is(50)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].remark", is("")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size", is(10)));

    }

    @Test
    @Transactional
    @Rollback
    public void addOrder() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("name", "香雞城111");
        param.put("price", 317);
        param.put("remark", "青海路");
        param.put("storeId", 166);
        param.put("groupId",100);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .content(objectMapper.writeValueAsString(param))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("orderId", "117")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(param.get("name"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(param.get("price"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.remark", is(param.get("remark"))));
    }

    @Test
    @Transactional
    @Rollback
    public void addOrderList() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("name", "香雞城1111");
        param.put("price", 3107);
        param.put("remark", "青海路1");
        param.put("storeId", 168);
        param.put("groupId",100);
        List<Map> requests=new ArrayList<>();
        requests.add(param);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order/addList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .content(objectMapper.writeValueAsString(requests))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        Product product=orderRepository.getByStoreId((int)param.get("storeId")).get(0);
        assertEquals(product.getName(), param.get("name"));
        assertEquals(product.getRemark(), param.get("remark"));
        assertEquals(product.getPrice(), param.get("price"));
        assertEquals(product.getStore().getId(),param.get("storeId"));
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/get")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .session(session)
//                .param("orderId", "117")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(param.get("price"))))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(param.get("name"))))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.remark", is(param.get("remark"))));




    }

    @Test
    @Transactional
    @Rollback
    public void updateOrder()throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("id", 111);
        param.put("name", "不加醬");
        param.put("remark", "青海路111");
        param.put("price", 200);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .content(objectMapper.writeValueAsString(param))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("orderId", "111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(param.get("name"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(param.get("price"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.remark", is(param.get("remark"))));

    }

    @Test
    @Transactional
    @Rollback
    public void managerUpdateOrder() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", 111);
        param.put("name", "不");
        param.put("remark", "青海路111");
        param.put("price", 166);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/managerUpdate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .content(objectMapper.writeValueAsString(param))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("orderId", "111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(param.get("name"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(param.get("price"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.remark", is(param.get("remark"))));


    }

    @Test
    @Transactional
    @Rollback
    public void removeOrder() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/order/delete")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .param("id", "114")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        Optional<Product> Product = orderRepository.findById(114);
        assertThat(Product.isPresent()).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    public void managerRemoveOrder()throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/order/managerDelete")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .param("id", "111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        Optional<Product> Product = orderRepository.findById(111);
        assertThat(Product.isPresent()).isFalse();

    }


    @Test
    @Transactional
    @Rollback
    public void getOrder()throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("orderId", "111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("原味沙拉1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(70)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.remark").isEmpty());

    }

    //新版API暫時沒有
//    @Test
//    @Transactional
//    @Rollback
//    public void getStoreList() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/getStoreList")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .session(session)
//                .param("groupId", "100")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(166)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].storeName", is("我誰!!!我修改店家!!!!")));
//
////         [{"id":166,"storeName":"我誰!!!我修改店家!!!!","tagList":{}}]
//    }

    @Test
    @Transactional
    @Rollback
    public void getFloorList() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/getFloorList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("3F")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("24F")));
    }

    @Test
    @Transactional
    @Rollback
    public void getUserList()throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/getUserList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .param("groupId", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].floorId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("一般管理員")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].englishName", is("manager")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].floorId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("tt")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].englishName", is("tt")));

  }
}