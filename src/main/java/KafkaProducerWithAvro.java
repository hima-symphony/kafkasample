import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import example.avro.User;

/**
 * Created by himalathacherukuru on 12/6/16.
 */
public class KafkaProducerWithAvro {

  public static void  main(String[] args) {
    String topicName = "testopicforthreebrokers";

    //Configure the Producer
    Properties configProperties = new Properties();
    configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
    configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        io.confluent.kafka.serializers.KafkaAvroSerializer.class);
    configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        io.confluent.kafka.serializers.KafkaAvroSerializer.class);
    configProperties.put("schema.registry.url", "http://localhost:8081");

    User user1 = new User();
    user1.setName("Alyssa");
    user1.setFavoriteNumber(256);

    Producer<String, GenericRecord> producer = new org.apache.kafka.clients.producer.KafkaProducer
        (configProperties);
    ProducerRecord<String, GenericRecord> rec = new ProducerRecord<String, GenericRecord>(topicName,
        "1", user1);
    producer.send(rec);

    producer.close();
  }
}
