package org.zerock.sb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Board;
import org.zerock.sb.repository.search.BoardSearch;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    //board가 기준이므로 group by b,,
    //Pageable타입을 파라미터로 받으면 Page타입으로 받음
    //select할때 하나 이상이면 object[]로 받음
    @Query("select b, count(r) from Board b left join Reply r on r.board = b group by b")
    Page<Object[]> ex1(Pageable pageable);

}
