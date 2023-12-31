package com.example.bookjac.mapper;

import com.example.bookjac.domain.Book;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface BookMapper {

    //게시글 목록
    @Select("""
            SELECT
            id, title,writer,publisher,categoryId,inPrice,outPrice,totalCount,inCount,displayCount,checkEvent
            FROM Book
            ORDER By id DESC
            """)
    List<Book> selectAll();

    @Select("""
			<script>
			<bind name="pattern" value="'%' + search + '%'" />
			SELECT COUNT(*) 
			FROM Book
			
			where
			
				   title  LIKE #{pattern}
				OR publisher   LIKE #{pattern}
				OR writer LIKE #{pattern}
			
			
			</script>
			""")
    Integer countAll(String search);

	@Select("""
			<script>
			<bind name="pattern" value="'%' + search + '%'" />
			SELECT
				title,
				id,
				displayCount,
				totalCount,
				writer,
				publisher,
				categoryId,
				checkEvent,
				isbn
			     
			FROM Book 
			
			where
				
				   title  LIKE #{pattern}
				OR publisher   LIKE #{pattern}
				OR writer LIKE #{pattern}
				
			

			ORDER BY id DESC
			LIMIT #{startIndex}, #{rowPerPage}
			</script>
			""")
	List<Book> selectAllPaging(Integer startIndex, Integer rowPerPage, String search);

	@Select("""
			<script>
			<bind name="pattern" value="'%' + search + '%'" />
			SELECT
				title,
				id,
				displayCount,
				totalCount,
				writer,
				publisher,
				event,
				eventStartDate,
				eventEndDate,
				isbn
			     
			FROM Book 
			
			where
				<if test="search neq '' ">
				   (title  LIKE #{pattern}
				OR publisher   LIKE #{pattern}
				OR writer LIKE #{pattern})
				OR
				 </if>
				 (event is Not Null)
				
			

			ORDER BY eventStartDate ASC
			LIMIT #{startIndex}, #{rowPerPage}
			</script>
			""")
	List<Book> selectAllPagingEvent(Integer startIndex, Integer rowPerPage, String search);

	@Update("""
			UPDATE Book
			SET displayCount =  displayCount-#{sellAmount},
				totalCount = inCount + displayCount
		
			WHERE
			id = #{id}
			""")
	Integer bookSellUpdate(Book book);

	@Update("""
			UPDATE Book
			SET displayCount = displayCount + #{refundAmount},
				totalCount = inCount + displayCount
			WHERE 
			id = #{id}
			""")
	Integer bookRefundUpdate(Book book);

	@Select("""
			SELECT *
			FROM Book
			WHERE id =#{id}
			""")
	Book selectById(Integer id);

	@Update("""
			UPDATE Book
			SET 
				event = #{event},
				eventStartDate = #{eventStartDate},
				eventEndDate = #{eventEndDate},
				checkEvent = 1
			WHERE id = #{id}
				
			""")
	int update(Book book);

	@Update("""
			UPDATE Book
			SET event = Null,
			checkEvent = 0,
			eventStartDate = Null,
			eventEndDate = Null
			
			WHERE id = #{id}
			""")
	int deleteById(Integer id);




}
