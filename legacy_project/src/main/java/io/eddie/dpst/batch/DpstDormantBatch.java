package io.eddie.dpst.batch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.eddie.dpst.dao.DpstCloseDao;

/**
 * 휴면
 */
public class DpstDormantBatch {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("batch-context.xml");
        DpstCloseDao dao = (DpstCloseDao) ctx.getBean("dpstCloseDao");
        dao.updateDormant(args[0]);
    }
}
