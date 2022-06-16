import com.smartcity.service.paperservice.PaperApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.*;
import java.util.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

@EnableAspectJAutoProxy
public class test {
    @Test
    public void test(){
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext();
        ac.setAllowCircularReferences(true);
        ac.register(PaperApplication.class);
        ac.refresh();
    }

    @Test
    public void client() throws IOException {
        InetAddress address = InetAddress.getByName("localhost");
        Socket socket = new Socket(address, 8899);

        OutputStream outputStream = socket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream("皮卡丘3.jpg");

        byte[] buffer = new byte[1024];
        int len;
        while((len = fileInputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, len);
        }

        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();

        while((len = inputStream.read(buffer)) != -1){
            System.out.println(new String(buffer, 0, len));
        }

        fileInputStream.close();
        inputStream.close();
        outputStream.close();
        socket.close();
    }

    @Test
    public void server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8899);
        Socket accept = serverSocket.accept();

        InputStream inputStream = accept.getInputStream();

        FileOutputStream fileOutputStream = new FileOutputStream("皮卡丘copy.jpg");

        byte[] buffer = new byte[1024];
        int len;
        while((len = inputStream.read(buffer)) != -1){
            fileOutputStream.write(buffer, 0, len);
        }
        System.out.println(accept.getInetAddress());

        OutputStream outputStream = accept.getOutputStream();
        outputStream.write("accept".getBytes());

        fileOutputStream.close();
        inputStream.close();
        outputStream.close();
        accept.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(bf.readLine());
        for (int i = 0;i < t;i++) {
            int n = Integer.parseInt(bf.readLine());
            if(n == 0){
                System.out.println(0);
            }
            String[] str = bf.readLine().split(" ");
            int[] items = new int[n];
            for (int j = 0; j < n; j++) {
                items[j] = Integer.parseInt(str[j]);
            }

            //对数组排序
            Arrays.sort(items);

            System.out.println(costTime(items));
        }
    }

    @Test
    public void test1() throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(bf.readLine());
        for (int i = 0;i < t;i++) {
            int n = Integer.parseInt(bf.readLine());
            if(n == 0){
                System.out.println(0);
            }
            String[] str = bf.readLine().split(" ");
            int[] items = new int[n];
            for (int j = 0; j < n; j++) {
                items[j] = Integer.parseInt(str[j]);
            }

            //对数组排序
            Arrays.sort(items);

            System.out.println(costTime(items));
        }
    }

    public static int costTime(int[] items){
        int point = items.length-1, result = 0;

        while(point>=3){
            if(items[point]+items[point-1]+2*items[0] >= 2*items[1]+items[0]+items[point]){
                result += 2*items[1]+items[0]+items[point];
            }else {
                result += items[point]+items[point-1]+2*items[0];
            }
            point -= 2;
        }
        if(point == 2) {
            return result+items[1]+items[2]+items[0];
        }
        if(point == 1) {
            return result+items[1];
        }
        if(point == 0){
            return items[0] + result;
        }
        return 0;
    }
}