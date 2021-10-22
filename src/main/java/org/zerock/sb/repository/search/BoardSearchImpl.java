package org.zerock.sb.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.sb.entity.Board;
import org.zerock.sb.entity.QBoard;
import org.zerock.sb.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
//QuerydslRepositorySupport를 상속하면서 QBoard 생성, 동적쿼리 처리 가능
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(char[] typeArr, String keyword, Pageable pageable) {
        log.info("....................search1...........................");

        //표준방식 -> 동적쿼리 처리위해서 생성
        QBoard board = QBoard.board;

        JPQLQuery<Board> jpqlQuery = from(board);

        if (typeArr != null && typeArr.length > 0) {

            BooleanBuilder condition = new BooleanBuilder();

            for(char type: typeArr) {
                if(type == 'T') {
                    condition.or(board.title.contains(keyword));
                } else if(type == 'C') {
                    condition.or(board.content.contains(keyword));
                } else if(type == 'W') {
                    condition.or(board.writer.contains(keyword));
                }
            }
            jpqlQuery.where(condition);
        }

        jpqlQuery.where(board.bno.gt(0L)); //bno > 0, gt -> greater
        JPQLQuery<Board> pagingQuery = this.getQuerydsl().applyPagination(pageable, jpqlQuery);

        List<Board> boardList = pagingQuery.fetch();
        Long totalCount = pagingQuery.fetchCount();

        return new PageImpl<>(boardList, pageable, totalCount);

    }

    @Override
    public Page<Object[]> searchWithReplyCount(char[] typeArr, String keyword, Pageable pageable) {
        log.info("searchWithReplyCount start......................");

        //1.EntityManager 이용
        //this.getEntityManager().createQuery("");

        //2.getQuerydsl 이용
        //this.getQuerydsl().createQuery();

        //3.QBoard 이용 - 공식문서에서 제시하는 방향
        //Query를 만들때는 QDomain -- 값을 뽑을 때는 Entity 타입 이용
        QBoard qBoard = QBoard.board;
        QReply qReply = QReply.reply;

        JPQLQuery<Board> query = from(qBoard); //from을 쓸 때는 QBoard 사용
        query.leftJoin(qReply).on(qReply.board.eq(qBoard));
        query.groupBy(qBoard);

        //검색조건
        if (typeArr != null && typeArr.length > 0) {

            BooleanBuilder condition = new BooleanBuilder();

            for(char type: typeArr) {
                if(type == 'T') {
                    condition.or(qBoard.title.contains(keyword));
                } else if(type == 'C') {
                    condition.or(qBoard.content.contains(keyword));
                } else if(type == 'W') {
                    condition.or(qBoard.writer.contains(keyword));
                }
            }
            query.where(condition);
        }

        JPQLQuery<Tuple> selectQuery = query.select(qBoard.bno, qBoard.title,
                qBoard.writer, qBoard.regDate, qReply.count());

        this.getQuerydsl().applyPagination(pageable, selectQuery);

        List<Tuple> tupleList = selectQuery.fetch();
        long totalCount = selectQuery.fetchCount();

        List<Object[]> arr = tupleList.stream().map(tuple -> tuple.toArray()).collect(Collectors.toList());

        return new PageImpl<>(arr, pageable, totalCount);
    }

}
