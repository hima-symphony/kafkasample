import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Scanner;

import example.avro.User;

/**
 * Created by himalathacherukuru on 12/6/16.
 */
public class KafkaProducerWithAvro {
  private static Scanner scanner;

  public static void  main(String[] args) {
    String topicName = "testopicforthreebrokers";
    scanner = new Scanner(System.in);

    //Configure the Producer
    Properties configProperties = new Properties();
    configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
    configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        io.confluent.kafka.serializers.KafkaAvroSerializer.class);
    configProperties.put("schema.registry.url", "http://localhost:8081");


    Producer<String, GenericRecord> producer = new org.apache.kafka.clients.producer.KafkaProducer
        (configProperties);
    ProducerRecord<String, GenericRecord> rec;

    User user1;

    String message = "";
    while(message!="exit") {
      message = scanner.nextLine();
      user1 = new User();
      user1.setName(message);
      user1.setFavoriteNumber(256);

     rec = new ProducerRecord<String, GenericRecord>(topicName,
          "1", user1);
     producer.send(rec);
    }

    producer.close();
  }
}
