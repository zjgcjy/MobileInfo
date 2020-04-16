package ucas.iie.rd6.mobileinfo.hardware;


import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanyan on 20200414
 */
public class SimCardUtils {
    public static JSONArray getAllSimInfo(TelephonyManager tel) throws Exception {
        Class<?> clazz = tel.getClass();

        //获取可以进行反射的字段
        List<EMethod> list = new ArrayList<>();
        Map<String, Integer> listIgnore = new HashMap<>();

        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            String name = method.getName();
            if(!name.startsWith("get"))
                continue;

            if(listIgnore.get(name) != null)
                continue;
            listIgnore.put(name, 0);

            Method m1 = null;
            Method m2 = null;
            Method m3 = null;
            try {m1 = clazz.getDeclaredMethod(name); } catch(Exception e) {}
            try {m2 = clazz.getDeclaredMethod(name, int.class); } catch(Exception e) {}
            try {m3 = clazz.getDeclaredMethod(name, long.class); } catch(Exception e) {}

            if(m1 != null && ((m2 == null && m3 != null) || (m2 != null && m3 == null))) {
                Class<?> c1 = m1.getReturnType();
                Class<?> c2 = m2 == null ? null : m2.getReturnType();
                Class<?> c3 = m3 == null ? null : m3.getReturnType();
                if(m2 == null) {
                    if(c1 == null || c1 != c3)
                        continue;
                } else {
                    if(c1 == null || c1 != c2)
                        continue;
                }
                EMethod item = new EMethod(name, m2 == null ? 1 : 0, c1);
                list.add(item);
            }
        }
        listIgnore.clear();

        JSONArray array = new JSONArray();
        for(int i=0; i<10; i++) {
            JSONObject json = new JSONObject();
            for(EMethod em : list) {
                Method method = null;
                Object param = null;
                if(em.type == 0) {
                    method = clazz.getDeclaredMethod(em.name, int.class);
                    param = i;
                } else {
                    method = clazz.getDeclaredMethod(em.name, long.class);
                    param = new Long(i);
                }
                if(!method.isAccessible())
                    method.setAccessible(true);

                String name = em.name.substring(3);
                Object value = null;
                try {
                    value = method.invoke(tel, param);
                } catch(Exception e) {
                    //前面已经对private设置了可访问，有些还是会报错，就不管这个了
                    continue;
                }

                json.put(name, value);
            }

            if(json.optInt("SimState") == TelephonyManager.SIM_STATE_UNKNOWN || json.optInt("SimState") == TelephonyManager.SIM_STATE_ABSENT)
                continue;

            String imsi = json.optString("SubscriberId");
            if(imsi == null || imsi.length() == 0)
                continue;

            //根据imsi去重
            boolean repeact = false;
            for(int j=0; j<array.length(); j++) {
                if(imsi.equals(array.optJSONObject(j).optString("SubscriberId"))) {
                    repeact = true;
                    break;
                }
            }
            if(repeact)
                continue;

            array.put(json);
        }
        return array;
    }
    //调这个函数
    public static String getSimCardInfo(TelephonyManager tel) throws Exception {
        JSONArray simList = getAllSimInfo(tel);
        String result = "\n\n28.SIM卡信息:";
        for (int i=0; i<simList.length(); i++){
            String s = "\n\n卡" + (i+1) + ":\n\t\t手机号:";
            s = s + simList.optJSONObject(i).optString("Line1Number");
            s = s + "\n\t\tIMSI:";
            s = s + simList.optJSONObject(i).optString("SubscriberId");
            s = s + "\n\t\t运营商:";
            s = s + simList.optJSONObject(i).optString("SimOperatorName");

            result = result + s;
        }
        return  result;
    }

    static class EMethod {
        public String name;
        public int type;	//0为int，1为long
        public Class<?> returnType;	//返回类型
        public EMethod(String name, int type, Class<?> returnType) {
            this.name = name;
            this.type = type;
            this.returnType = returnType;
        }
    }

}
