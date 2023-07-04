package hello.jdbc.exception.basic;

import jdk.jfr.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedTest {

    static class MyUnCheckedException extends RuntimeException {
        public MyUnCheckedException(String message){
            super(message);
        }
    }

    @Test
    @Description("uncheck exception catch")
    void unchecked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    @Description("uncheck exception throws")
    void unchecked_throw(){
        Service service = new Service();
        Assertions.assertThrows(MyUnCheckedException.class, service::callThrow);
    }

    static class Service {
        Repository repository = new Repository();

        public void callCatch(){
            try {
                repository.call();
            }catch (MyUnCheckedException e){
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        public void callThrow(){
            repository.call(); // 예외를 잡지 않아도 자연스럽게 상위 레벨로 throw 한다.
        }
    }

    static class Repository {
        public void call(){
            throw new MyUnCheckedException("ex"); // throws 선언하지 않아도 컴파일 에러가 발생하지 않음 (throws 생략 가능)
        }
    }


}
