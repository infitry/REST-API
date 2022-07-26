package infitry.rest.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void 컨트롤러_권한_테스트() throws Exception {
        //given
        int httpResult = 401;
        String mainPath = "/";
        //when

        //then
        mvc.perform(MockMvcRequestBuilders.get(mainPath))
                .andExpect(MockMvcResultMatchers.status().is(httpResult))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
}