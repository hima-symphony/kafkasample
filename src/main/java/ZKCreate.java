import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by himalathacherukuru on 12/1/16.
 */
public class ZKCreate {
  private static ZooKeeper zk;

  private static ZooKeeperConnection zooKeeperConnection;

  public static void create(String path, byte[] data) throws KeeperException, InterruptedException {
    zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
  }

  public static void main(String[] args) {
    String path = "/myFirstZNode";

    byte[] data = "My first zookeeper node".getBytes();

    try {
      zooKeeperConnection = new ZooKeeperConnection();
      zk = zooKeeperConnection.connect("localhost");
      create(path, data);
      zooKeeperConnection.close();
    } catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
