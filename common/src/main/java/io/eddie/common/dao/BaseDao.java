package io.eddie.common.dao;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

public abstract class BaseDao {

    private SqlSessionFactory sqlSessionFactory;
    private DataSource dataSource;

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) { this.sqlSessionFactory = sqlSessionFactory; }
    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }

    protected Object selectOne(String id, Object param) {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            return open(con).selectOne(id, param);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    protected List selectList(String id, Object param) {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            return open(con).selectList(id, param);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    protected int update(String id, Object param) {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            return open(con).update(id, param);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    protected int insert(String id, Object param) {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            return open(con).insert(id, param);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    private SqlSession open(Connection con) {
        return sqlSessionFactory.openSession(con);
    }
}
