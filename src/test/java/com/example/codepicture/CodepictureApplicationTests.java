package com.example.codepicture;

import com.example.codepicture.CodePicture.CreateImageCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class CodepictureApplicationTests {

    @Test
    void contextLoads() {
        CreateImageCode imageCode = new CreateImageCode(160, 40, 4, 20);
        try {
            // 保存图片到本地
            imageCode.saveImageToFile("D:/image.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
