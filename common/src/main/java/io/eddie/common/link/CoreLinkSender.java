package io.eddie.common.link;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import io.eddie.common.util.StringUtil;

/**
 * 코어뱅킹 전문 송신
 * layout/{전문ID}.properties 의 위치·길이로 고정 길이 전문을 만들고 로그로 남긴다. (실제 송신하지 않음)
 */
public class CoreLinkSender {

    private static Logger log = Logger.getLogger(CoreLinkSender.class);

    private String layoutDir = "layout";

    public void setLayoutDir(String layoutDir) { this.layoutDir = layoutDir; }

    public void send(String msgId, Object vo, String linkKey) throws Exception {
        if (linkKey == null || linkKey.length() == 0) {
            throw new IllegalArgumentException("연계 인증키 없음");
        }
        String msg = build(msgId, vo);
        log.info("SEND " + msgId + " [" + msg + "]");
    }

    String build(String msgId, Object vo) throws Exception {
        Properties layout = loadLayout(msgId);
        List fields = new ArrayList();
        for (Enumeration e = layout.propertyNames(); e.hasMoreElements();) {
            String name = (String) e.nextElement();
            String[] def = layout.getProperty(name).split(",");
            fields.add(new Object[] { name, Integer.valueOf(def[0].trim()), Integer.valueOf(def[1].trim()), def[2].trim() });
        }
        Collections.sort(fields, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Integer) ((Object[]) o1)[1]).compareTo((Integer) ((Object[]) o2)[1]);
            }
        });

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < fields.size(); i++) {
            Object[] f = (Object[]) fields.get(i);
            String name = (String) f[0];
            int len = ((Integer) f[2]).intValue();
            String value = "TRX_CD".equals(name) ? msgId : getValue(vo, name);
            if (value == null) value = "";
            if (value.length() > len) value = value.substring(0, len);
            sb.append("N".equals(f[3]) ? StringUtil.lpad(value, len, '0') : StringUtil.rpad(value, len, ' '));
        }
        return sb.toString();
    }

    private Properties loadLayout(String msgId) throws IOException {
        Properties p = new Properties();
        InputStream in = new FileInputStream(layoutDir + "/" + msgId + ".properties");
        try {
            p.load(in);
        } finally {
            in.close();
        }
        return p;
    }

    private String getValue(Object vo, String fieldName) throws Exception {
        String[] parts = fieldName.toLowerCase().split("_");
        StringBuffer getter = new StringBuffer("get");
        for (int i = 0; i < parts.length; i++) {
            getter.append(Character.toUpperCase(parts[i].charAt(0))).append(parts[i].substring(1));
        }
        Method m = vo.getClass().getMethod(getter.toString(), new Class[0]);
        Object v = m.invoke(vo, new Object[0]);
        return v == null ? null : v.toString();
    }
}
