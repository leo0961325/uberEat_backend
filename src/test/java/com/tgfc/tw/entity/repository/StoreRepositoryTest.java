package com.tgfc.tw.entity.repository;

import com.tgfc.tw.MainApplication;
import com.tgfc.tw.entity.model.po.Store;
import com.tgfc.tw.entity.model.po.StorePicture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;


@SpringBootTest(classes = MainApplication.class)
public class StoreRepositoryTest {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    StorePictureRepository storePictureRepository;

    @Test
    @Rollback(value = false)
    public void saveStoreTest() {
        Store store = new Store();
        store.setId(1);
        store.setTel("03-3345678");
        store.setName("TestStore");
        storeRepository.save(store);
    }

    @Test
    public void picTest(){
        List<StorePicture> list = storePictureRepository.getByStoreId(3);
        String str = "";
    }
}