
package com.hyva.restopos.kafka;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyva.restopos.rest.HiposControllerendpoints.HiposController;
import com.hyva.restopos.rest.entities.TablesPos;
import com.hyva.restopos.rest.pojo.TableReservationPojo;
import com.hyva.restopos.rest.repository.TablesPosRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
@ConditionalOnProperty(value = "kafka.enabled", matchIfMissing = true)
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    @Autowired
    MessageStorage storage;
    @Autowired
    ConnectService connectService;
    @Autowired
    TablesPosRepository tablesPosRepository;
    @Autowired
    HiposController hiposController;

    @KafkaListener(topics="${jsa.kafka.topic}")
    public void processMessage(String content) throws Exception {
        Gson gson = new Gson();
        log.info("received content = '{}'", content);
        JSONObject jsonObj = new JSONObject(content);
        String jsonString=jsonObj.get("jsonData").toString();
        jsonObj = new JSONObject(jsonString);
        if(jsonObj.has("store_id")&&!jsonObj.has("noOfPersons")){
            connectService.statusChange(jsonObj.toString());
        }else if(jsonObj.has("noOfPersons")) {
            Type type = new TypeToken<TableReservationPojo>(){}.getType();
            hiposController.saveTableReservation(gson.fromJson(jsonString,type));
        }else if(jsonObj.has("tablename")&&jsonObj.has("message")){
            TablesPos tablesPos = tablesPosRepository.findAllByTableName(jsonObj.get("tablename").toString());
            if(tablesPos!=null) {
                tablesPos.setMessage(jsonObj.get("message").toString());
                tablesPosRepository.save(tablesPos);
            }
        }else{
            String data = connectService.urbanNotification(jsonString);
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode object = mapper.readTree(data);
            connectService.saveNotificationData(data, object.get("channel").asText(), jsonString,1,object.get("tableName").asText(),object.get("waiter").asText());
        }
    }

}