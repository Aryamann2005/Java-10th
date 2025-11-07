package com.example.osms.config;


import java.util.Properties;


import javax.sql.DataSource;


import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import com.example.osms.dao.StudentDAOImpl;
import com.example.osms.service.FeeServiceImpl;


@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.example.osms")
public class AppConfig {


@Bean
public DataSource dataSource() {
DriverManagerDataSource ds = new DriverManagerDataSource();
ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
ds.setUrl("jdbc:mysql://localhost:3306/osms?useSSL=false&serverTimezone=UTC");
ds.setUsername("root");
ds.setPassword("password");
return ds;
}


@Bean
public LocalSessionFactoryBean sessionFactory() {
LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
lsfb.setDataSource(dataSource());
lsfb.setPackagesToScan("com.example.osms.model");
Properties props = new Properties();
props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
props.put("hibernate.hbm2ddl.auto", "update");
props.put("hibernate.show_sql", "true");
props.put("hibernate.format_sql", "true");
lsfb.setHibernateProperties(props);
return lsfb;
}


@Bean
public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
HibernateTransactionManager tm = new HibernateTransactionManager();
tm.setSessionFactory(sessionFactory);
return tm;
}


@Bean
public StudentDAOImpl studentDAO(SessionFactory sf) {
return new StudentDAOImpl(sf);
}


@Bean
public FeeServiceImpl feeService(StudentDAOImpl dao) {
return new FeeServiceImpl(dao);
}
}
