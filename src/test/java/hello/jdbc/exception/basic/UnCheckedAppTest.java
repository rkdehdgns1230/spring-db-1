package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
public class UnCheckedAppTest {
    @Test
    void unchecked(){
        Controller controller = new Controller();
        Assertions.assertThrows(RuntimeSQLException.class, controller::request);
    }

    @Test
    void printEx(){
        Controller controller = new Controller();
        try {
            controller.request();
        }
        catch (Exception e){
//            e.printStackTrace(); 이 방식은 좋지 않다고 함.. -> System.out 으로 출력하기 때문
            log.info("ex", e); // 마지막 파라미터로 exception 전달하면, stacktrace 출력해준다.
        }
    }

    static class Controller {
        Service service = new Service();
        public void request() { // 호출하는 부분에 throws를 매번 작성해줘야 한다.
            service.logic();
        }

    }
    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() throws RuntimeConnectException{
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message){
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) { // cause 이용해 이전 예외를 포함해서 가지도록 설정 가능
            super(cause);
        }
    }
}
