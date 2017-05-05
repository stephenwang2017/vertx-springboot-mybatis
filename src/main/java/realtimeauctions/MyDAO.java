package realtimeauctions;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class MyDAO {
	
	@Autowired
	@Qualifier("firstSqlSessionTemplate")
	private SqlSession sqlSession;
	
	public String getCurrentTime() {
		return sqlSession.selectOne("com.jsonobject.example.mapper.ExampleMapper.getCurrentDateTime");
	}
}
