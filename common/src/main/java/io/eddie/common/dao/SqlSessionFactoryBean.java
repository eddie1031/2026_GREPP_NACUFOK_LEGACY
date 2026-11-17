package io.eddie.common.dao;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class SqlSessionFactoryBean implements FactoryBean, InitializingBean {

    private DataSource dataSource;
    private Resource[] mapperLocations;
    private SqlSessionFactory sqlSessionFactory;

    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }
    public void setMapperLocations(Resource[] mapperLocations) { this.mapperLocations = mapperLocations; }

    public void afterPropertiesSet() throws Exception {
        ManagedTransactionFactory txFactory = new ManagedTransactionFactory();
        Properties txProps = new Properties();
        txProps.setProperty("closeConnection", "false");
        txFactory.setProperties(txProps);

        Configuration configuration = new Configuration(new Environment("bank", txFactory, dataSource));
        for (int i = 0; mapperLocations != null && i < mapperLocations.length; i++) {
            InputStream in = mapperLocations[i].getInputStream();
            try {
                new XMLMapperBuilder(in, configuration, mapperLocations[i].toString(), new HashMap()).parse();
            } finally {
                in.close();
            }
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    public Object getObject() { return sqlSessionFactory; }
    public Class getObjectType() { return SqlSessionFactory.class; }
    public boolean isSingleton() { return true; }
}
