import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServlet;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Main1 {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            ArrayList<Object> object = new ArrayList<Object>();
            object.add(new Integer(1));
            int n = sc.nextInt();
        }
        ServiceImpl service = new ServiceImpl();
        Service proxy= (Service) Proxy.newProxyInstance(service.getClass().getClassLoader(), service.getClass().getInterfaces(), new MyInvocationHandler(service));
        proxy.send("hello world");
    }
}

interface Service{
    public default String send(String msg){
        System.out.println("default");
        return "default";
    };
}

class ServiceImpl implements Service{
    @Override
    public String send(String msg) {
        System.out.println("send msg:" + msg);
        return msg;
    }
}

class MyInvocationHandler implements InvocationHandler{

    private final Object targer;

    MyInvocationHandler(Object targer) {
        this.targer = targer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("增强send方法");
        Object res = method.invoke(targer, args);
        return res;
    }
}

