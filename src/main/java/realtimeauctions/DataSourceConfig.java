package realtimeauctions;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	
	@Bean
	public SqlSessionFactory firstSqlSessionFactory(DataSource firstDataSource, ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(firstDataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mappers/first/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean
	public SqlSessionTemplate firstSqlSessionTemplate(SqlSessionFactory firstSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(firstSqlSessionFactory);
	}
}