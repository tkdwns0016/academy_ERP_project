package suggestionBoard;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import anonymousBoard.AnonymousBoard;
import service.EmplClass;
import service.Employee;

@Mapper
public interface SuggestionBoardMapper {
	@Select("select * from suggestion_board order by id desc limit #{firstRow},#{pagePerCount}")
	public List<SuggestionBoard> selectList(@Param("firstRow")int firstRow,@Param("pagePerCount")int pagePerCount);
	@Select("select * from suggestion_board order by id desc limit 5")
	public List<SuggestionBoard> mainList();
	
	@Select("select count(*) from suggestion_board")
	public int count();
	
	@Select("select * from suggestion_board where id=#{id}")
	public SuggestionBoard select(int id);

	@Insert("insert into suggestion_board(title,writer,content,wirte_date) "
			+ "values(#{title},#{writer},#{content},#{writeDate})")
	public int insert(SuggestionBoard suggestionBoard);
	
	@Update("update suggestion_board set title=#{title},writer=#{writer}, content=#{content},"
			+ "write_date=#{writeDate} where id=#{id}")
	public int update(SuggestionBoard suggestionBoard);
	@Update("update suggestion_board set count=#{count} where id=#{id}")
	public int countPlus(@Param("count")int count,@Param("id")int id);
	@Delete("delete from suggestion_board where id=#{id}")
	public int delete(int id);
	@Select("select name,department_id from employee where user_id=#{writer}")
	public Employee getWriter(int writer);
	@Select("select name from employee where user_id=#{writer}")
	public EmplClass getECWriter(int writer);
	@Select("select department_name from department where department_id=#{departmentId}")
	public String getDepartment(int departmentId);
	@Select("select id from suggestion_board order by id desc limit 1")
	public int getLastIndex();
	@Select("select id from suggestion_board order by id asc limit 1")
	public int getFirstIndex();
	@Select("select id from suggestion_board where id=(select min(id) from suggestion_board where id>#{id})")
	public int getNextIndex(int id);
	@Select("select id from suggestion_board where id=(select max(id) from suggestion_board where id<#{id})")
	public int getBeforeIndex(int id);
	
}
