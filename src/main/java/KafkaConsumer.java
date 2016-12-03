import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by himalathacherukuru on 12/3/16.
 */
public class KafkaConsumer {
  private static Scanner scanner;

  public static void main(String[] args) throws InterruptedException {
    String topicName = "javaworld";
    String groupId = "group1";

    scanner = new Scanner(System.in);

    ConsumerThread consumerThread = new ConsumerThread(topicName, groupId);
    consumerThread.run();
    String message = "";
    while(!message.equals("exit")) {
      message = scanner.nextLine();
    }

    consumerThread.getConsumer().wakeup();
    System.out.println("Stopping consumer");
    consumerThread.join();
  }

  public static class ConsumerThread extends Thread {
    Consumer consumer;
    String topicName;
    String groupId;

    public ConsumerThread(String topicName, String groupId) {
      this.topicName = topicName;
      this.groupId = groupId;
    }

    public Consumer getConsumer() {
      return this.consumer;
    }

    public void run() {
      Properties properties = new Properties();
      properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common" +
          ".serialization.StringDeserializer");
      properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common" +
          ".serialization.StringDeserializer");
      properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
      properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple");

      consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(properties);
      consumer.subscribe(Arrays.asList(topicName));

      try {
        while (true) {
          ConsumerRecords<String, String> records = consumer.poll(100);
          for (ConsumerRecord<String, String> record : records) {
            System.out.println(record.value());
            System.out.println(record.key());
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
