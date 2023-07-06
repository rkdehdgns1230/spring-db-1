package hello.jdbc.exception.basic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

public class CheckedAppTest {

    @Test
    void checked(){
        Controller controller = new Controller();
        Assertions.assertThrows(Exception.class, controller::logic);
    }

    static class Controller {
        Service service = new Service();
        public void logic() throws SQLException, ConnectException{ // 호출하는 부분에 throws를 매번 작성해줘야 한다.
            service.logic();
        }

    }
    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws SQLException, ConnectException{
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() throws ConnectException{
            throw new ConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() throws SQLException{
            throw new SQLException("ex");
        }
    }
}
