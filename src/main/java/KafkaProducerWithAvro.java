import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.Future;

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
        "1", null);
    Future resultFuture = producer.send(rec);

    producer.close();
  }

  public static class User {
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getFavoriteNumber() {
      return favoriteNumber;
    }

    public void setFavoriteNumber(int favoriteNumber) {
      this.favoriteNumber = favoriteNumber;
    }

    String name;
    int favoriteNumber;


  }
}
