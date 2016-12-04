import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Scanner;

/**
 * Created by himalathacherukuru on 12/3/16.
 * Kafka Example
 */
public class KafkaProducer {
  private static Scanner in;

  public static void  main(String[] args) {
    String topicName = "javaworld";

    //Configure the Producer
    Properties configProperties = new Properties();
    configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
    configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
    configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");


    in = new Scanner(System.in);
    System.out.println("Enter message(type exit to quit)");
    String message = in.nextLine();
    Producer producer = new org.apache.kafka.clients.producer.KafkaProducer(configProperties);
    while(!message.equals("exit")) {
      ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName, message);
      producer.send(rec);
      message = in.nextLine();
    }
    in.close();
    producer.close();
  }
}
