package com.tgfc.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.tw.MainApplication;
import com.tgfc.tw.controller.exception.ErrorCodeException;
import com.tgfc.tw.entity.model.po.StorePicture;
import com.tgfc.tw.entity.repository.StorePictureRepository;
import com.tgfc.tw.service.StorePictureService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MainApplication.class)
@AutoConfigureMockMvc
public class StorePictureControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;

    Cookie[] cookies;
    String xsrf;

    MvcResult ResultPhotoId;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    StorePictureRepository storePictureRepository;

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
//    @Test
//    @Transactional
//    @Rollback
//    public void deletePicUrlById()throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/storePicture/deletePicUrlById")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .param("picId", "11")
//                .cookie(new Cookie("XSRF-TOKEN",xsrf))
//                .header("X-XSRF-TOKEN",xsrf)
//                .session(session)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//    }

    @Test
    public void deletePic() throws Exception{

        InputStream inputStream = new FileInputStream(new File("../hungry/src/test/resources/mc.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile("files", "timg.jpeg",null , inputStream);
        ResultPhotoId=mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload/photo").file(multipartFile)
                .contentType(MediaType.IMAGE_JPEG)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
        String jsonStr=ResultPhotoId.getResponse().getContentAsString();
        System.out.println(jsonStr);
        JSONArray arrayList = new JSONArray(jsonStr);
        String fileName = arrayList.getString(0);
        JSONObject jsonObject = new JSONObject(fileName);
        System.out.println(jsonObject.get("storePicName"));
        String photoId =jsonObject.get("id").toString();
        String photoName =jsonObject.get("storePicName").toString();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/storePicture/deletePic")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("picId", photoId)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        StorePicture storePicture = storePictureRepository.getById((int)jsonObject.get("id"));
        assertThat(storePicture).isNull();
    }

    @Test
    public void getPicUrlByStoreId()throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/storePicture/getPicUrlByStoreId")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("storeId", "166")
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].url", is("/api/images/1574650143166.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("1574650143166.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].url", is("/api/images/1574650146064.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("1574650146064.jpg")));
    }

    @Test
    public void uploadPhoto() throws Exception{

        //InputStream inputStream = new FileInputStream(new File("C:/files/1528095526.jpg"));
        InputStream inputStream = new FileInputStream(new File("../hungry/src/test/resources/mc.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile("files", "timg.jpeg",null , inputStream);
        ResultPhotoId=mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload/photo").file(multipartFile)
                .contentType(MediaType.IMAGE_JPEG)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
        String jsonStr=ResultPhotoId.getResponse().getContentAsString();
        System.out.println(jsonStr);
        JSONArray arrayList = new JSONArray(jsonStr);
        String fileName = arrayList.getString(0);
        JSONObject jsonObject = new JSONObject(fileName);
        System.out.println(jsonObject.get("storePicName"));
        StorePicture storePictureModel=new StorePicture();
        storePictureModel.setId((int)jsonObject.get("id"));
        storePictureModel.setUrl(jsonObject.get("storePictureUrl").toString());
        storePictureModel.setName(jsonObject.get("storePicName").toString());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/images/{fileName}",storePictureModel.getName())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fileName", storePictureModel.getName())
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
        StorePicture storePicture = storePictureRepository.getById(storePictureModel.getId());
        assertEquals(storePicture.getId(), storePictureModel.getId());
        assertEquals(storePicture.getName(), storePictureModel.getName());
        assertEquals(storePicture.getUrl(), storePictureModel.getUrl());
    }

    @Test
    public void getPhoto() throws Exception{
        InputStream inputStream = new FileInputStream(new File("../hungry/src/test/resources/mc.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile("files", "timg.jpeg",null , inputStream);
        ResultPhotoId=mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload/photo").file(multipartFile)
                .contentType(MediaType.IMAGE_JPEG)
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
        String jsonStr=ResultPhotoId.getResponse().getContentAsString();
        System.out.println(jsonStr);
        JSONArray arrayList = new JSONArray(jsonStr);
        String fileName = arrayList.getString(0);
        JSONObject jsonObject = new JSONObject(fileName);
        System.out.println(jsonObject.get("storePicName"));
        StorePicture storePictureModel=new StorePicture();
        storePictureModel.setId((int)jsonObject.get("id"));
        storePictureModel.setUrl(jsonObject.get("storePictureUrl").toString());
        storePictureModel.setName(jsonObject.get("storePicName").toString());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/images/{fileName}",storePictureModel.getName())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fileName", storePictureModel.getName())
                .cookie(new Cookie("XSRF-TOKEN",xsrf))
                .header("X-XSRF-TOKEN",xsrf)
                .session(session)
                .accept(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}