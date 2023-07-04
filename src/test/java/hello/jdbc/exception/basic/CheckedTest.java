package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {


    @Test
    void checked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw(){
        Service service = new Service();
        Assertions.assertThrows(MyCheckedException.class, service::callThrow);
    }
    /**
     * Exception 상속 받은 예외는 체크 예외가 된다.
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message){
            super(message);
        }
    }

    /**
     * Checked 예외는
     * 예외를 던지거나 잡아서 처리해야 한다.
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch(){
            // 에러를 상위 레벨에 던지거나 현재 레벨에서 처리해야만 한다. (여기서는 처리한다)
            try {
                repository.call();
            } catch (Exception e) {
                log.info("예외 처리, message={}", e.getMessage(), e); // exception stack trace 출력하고 싶은 경우 마지막 인수로 넘겨줘야 한다.
                // exception 발생해도 로그 출력, stack trace 출력 과정을 따른 뒤 void return 되어 이후 로직이 수행되기 때문에, 프로그램이 정상적으로 종료된다.
            }
        }

        public void callThrow() throws MyCheckedException {
            repository.call();
        }
    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
