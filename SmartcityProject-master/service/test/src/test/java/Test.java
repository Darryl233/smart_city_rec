import com.smartcity.TestApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    @org.junit.Test
    public void test(){
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(TestApplication.class);
        annotationConfigApplicationContext.refresh();
        annotationConfigApplicationContext.close();
    }
}
