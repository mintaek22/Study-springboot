package hello.itemservice.messsage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage(){
        String message = ms.getMessage("hello", null, null);
        assertThat(message).isEqualTo("안녕");
    }

    @Test
    void argumentMessage(){
        String message = ms.getMessage("hello.name", new Object[]{"Spring", null},null);
        assertThat(message).isEqualTo("안녕 Spring");
    }

    @Test
    void enLang(){
        assertThat(ms.getMessage("hello",null, Locale.ENGLISH)).isEqualTo("hello");
    }
}
