package bo.gob.an.demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {


    @KafkaListener(topics = "${kafka.topic.name}")
    public void listener(String message) {
        System.out.println("--> Mensaje consumnido:"+message);

        //Do something
    }


}