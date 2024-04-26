package bo.gob.an.demo.productor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value(value = "${kafka.topic.name}")
    private String topic;

    //Metodo para enviar un mensaje simple
    public void sendMessage(String message) {

        var future = kafkaTemplate.send(this.topic, message);
        future.whenComplete(
                (res, error) -> {
                    if (error != null) {
                        System.out.println("No se pudo mandar el mensaje");
                    } else if (res != null) {
                        System.out.println("Mensaje enviado correctamente!");
                    }
                }
        );
    }
    // Metodo para enviar un mensaje de tipo Exactly once:
    // TODO revsar la documentacion: https://refactorizando.com/kafka-spring-boot-exactly-once/
    public void sendMessageTransacction(String pMensaje){
        var vRespuesta = kafkaTemplate.executeInTransaction(kt->{
            var result = kt.send(this.topic,pMensaje);
            result.whenComplete(
                    (res,error)->{
                        if (error != null) {
                            System.out.println("No se pudo mandar el mensaje");
                        } else if (res != null) {
                            System.out.println("Mensaje enviado correctamente!");
                        }
                    }
            );
            return result;
        });
    }
}