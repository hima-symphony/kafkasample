import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificData;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import example.avro.User;

/**
 * Created by himalathacherukuru on 12/7/16.
 */
public class KafkaConsumerWithAvro {
  private static Scanner scanner;

  public static void main(String[] args) throws InterruptedException {
    String topicName = "testopicforthreebrokers";
    String groupId = "group1";

    scanner = new Scanner(System.in);

    ConsumerThread consumerThreadOne =
        new ConsumerThread("consumerone", topicName, groupId);
    consumerThreadOne.run();
    String message = "";
    while(!message.equals("exit")) {
      message = scanner.nextLine();
    }

    consumerThreadOne.getConsumer().wakeup();
    System.out.println("Stopping consumer");
    consumerThreadOne.join();
  }

  public static class ConsumerThread extends Thread {
    Consumer consumer;
    String topicName;
    String groupId;
    String consumerName;

    public ConsumerThread(String consumerName, String topicName, String groupId) {
      this.topicName = topicName;
      this.groupId = groupId;
      this.consumerName = consumerName;
    }

    public Consumer getConsumer() {
      return this.consumer;
    }

    public void run() {
      System.out.println(consumerName);
      Properties properties = new Properties();
      properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
          "org.apache.kafka.common.serialization.StringDeserializer");
      properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
          io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
      properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
      properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "himatest");
      properties.put("schema.registry.url", "http://localhost:8081");

      consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(properties);
      consumer.subscribe(Arrays.asList(topicName));

      try {
        while (true) {
          ConsumerRecords<String, GenericRecord> records = consumer.poll(100);
          for (ConsumerRecord<String, GenericRecord> record : records) {
            User user = (User) SpecificData.get().deepCopy(User.SCHEMA$, record.value());

            System.out.printf("consumer: %s, key: %s, schema: %s, username: %s, " +
                    "userfavoritenumber: %s, useraddress: %s " +
                    " offset: %d \n",
                consumerName,
                record.key(), user.getSchema(), user.getName(), user.getFavoriteNumber(),
                user.getAddress(), record.offset());
          }
        }
      } catch(WakeupException e) {
        System.out.println("Exception caught" + e.getMessage());
      } finally {
        consumer.close();
        System.out.println("Stopping kafka consumer");
      }
    }
  }
}
