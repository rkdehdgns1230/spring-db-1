package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Transaction - parameter 연동, pool을 고려한 종료
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final MemberRepositoryV2 memberRepository;
    private final DataSource dataSource;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException{
        Connection con = dataSource.getConnection();

        try {
            con.setAutoCommit(false); // transaction start
            // business logic
            bizLogic(fromId, toId, money, con);

            con.commit(); // commit when success
        } catch (Exception e){
            e.printStackTrace();
            con.rollback(); // rollback when fail
            throw new IllegalStateException(e);
        } finally{
            release(con);
        }
    }

    private static void release(Connection con) {
        if(con != null){
            try{
                // autocommit을 다시 true로 바꿔줘야 한다. (다시 쓸 때에 false이면 문제 발생 가능)
                con.setAutoCommit(true);
                con.close();
            } catch (Exception e){
                log.info("error message={}", "error message", e);
            }
        }
    }

    private void bizLogic(String fromId, String toId, int money, Connection con) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember); // 임의의 error logic 추가
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
