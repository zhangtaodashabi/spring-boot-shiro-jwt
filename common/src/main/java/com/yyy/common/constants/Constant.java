package com.yyy.common.constants;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;

/**
 * @Author: Mr.Liu
 * @Classname Constant
 * @Description 常量类
 * @Date 2020/4/14
 */
public class Constant {

    private Constant() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 系统换行符
     */
    public static final String LINE_ST = System.getProperty("line.separator");

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";
    /**
     * 空字符串
     */
    public static final String EMPTY_STRING = "";

    /**
     * 删除标识（0：未删除
     */
    public static final Integer DEL_FLAG_0 = 0;

    /**
     * 整数1
     */
    public static final int NUM_1 = 1;


    /**
     * 整数1
     */
    public static final int NUM_2 = 2;

    /**
     * 正则-验证字符串是否为整数
     */
    public static final String REGX_STRING_OF_NUMBER = "^[-\\+]?[\\d]*$";

    /**
     * 正则-验证字符串是否为double
     */
    public static final String REGX_STRING_OF_DOUBLE_NUMBER = "[0-9]+[.]{0,1}[0-9]*[dD]{0,1}";
    /**
     * 科目状态（0：注销；1：正常）
     */
    public static final String DEPARTMENT_STATUS_0 = "0";

    /**
     * 科目状态（0：注销；1：正常）
     */
    public static final String DEPARTMENT_STATUS_1 = "1";

    /**
     * 科目/科室是否准予开设（0：不允许；1：允许）
     */
    public static final String APPROVE_OPEN_FLAG_0 = "0";

    /**
     * 是否备案（0：否；1：是）
     */
    public static final String IF_ACCEPTED_1 = "1";

    /**
     * 医生状态：坐诊
     */
    public static final String DOCTOR_STATUS_0 = "0";
    /**
     * 医生状态：停诊
     */
    public static final String DOCTOR_STATUS_1 = "1";

    /**
     * 医生状态：违规已下线
     */
    public static final String DOCTOR_STATUS_2 = "2";

    /**
     * 图文问诊
     */
    public static final String IMAGE_TEXT_INQUIRY = "1";

    /**
     * 视频问诊
     */
    public static final String VIDEO_INQUIRY = "2";

    /**
     * 预约状态（0：待预约；1：已预约；2：接诊中；3：已接诊；4：已拒绝）
     */
    public static final String APPOINTMENT_STATUS_0 = "0";

    /**
     * 预约状态（0：待预约；1：已预约；2：接诊中；3：已接诊；4：已拒绝）
     */
    public static final String APPOINTMENT_STATUS_1 = "1";

    /**
     * 预约状态（0：待预约；1：已预约；2：接诊中；3：已接诊；4：已拒绝）
     */
    public static final String APPOINTMENT_STATUS_2 = "2";

    /**
     * 预约状态（0：待预约；1：已预约；2：接诊中；3：已接诊；4：已拒绝）
     */
    public static final String APPOINTMENT_STATUS_3 = "3";

    /**
     * 预约状态（0：待预约；1：已预约；2：接诊中；3：已接诊；4：已拒绝）
     */
    public static final String APPOINTMENT_STATUS_4 = "4";

    /**
     * 查询类型（1：科目）
     */
    public static final String QUERY_SUBJECT = "1";

    /**
     * 查询类型（2：科室）
     */
    public static final String QUERY_DEPARTMENT = "2";

    /**
     * 身份证件类别代码(01：居民身份证)
     */
    public static final String IDCARD_TYPE_CODE_01 = "01";

    /**
     * 消息是否已读（0：未读；1：已读）
     */
    public static final String READ_FLAG_0 = "0";

    /**
     * 消息是否已读（0：未读；1：已读）
     */
    public static final String READ_FLAG_1 = "1";

    /**
     * 患者评价状态（0：待审核；1：通过；2：不通过）
     */
    public static final String EVALUATE_STATUS_0 = "0";

    /**
     * 患者评价状态（0：待审核；1：通过；2：不通过）
     */
    public static final String EVALUATE_STATUS_1 = "1";

    /**
     * 患者评价状态（0：待审核；1：通过；2：不通过）
     */
    public static final String EVALUATE_STATUS_2 = "2";

    public static final String PHONE_CHECK = "^1[345789]\\d{9}$";

    /**
     * 检查人员编号
     * */
    public static final String PERSON_CHECK = "^[a-zA-z]+[A-Za-z0-9]+$";


    /**
     * 个人填报菜单类型 ：2
     * */
    public static  final  Integer PERSONAL_FILL = 2;

    public static  final  String UNKNOWN = "unknown";

    public static  final String LOG_PLACEHOLDER = "{}";

    public static  final String THE_DATA_IS_WRONG = "数据有误，请检查";

    public static final String TABULATED_DATA_IS_INCORRECT="表格数据有误";

    public static final String  COLUMN_HEAD_IS_WRONG = "列头错误";
    /**
     * kpiStaffResult state状态：0 已保存
     */
    public static final Integer STATE_SAVE = 0;

    /**
     * kpiStaffResult state状态：1 已提交审核
     */
    public static final Integer STATE_SUBMIT = 1;

    /**
     * kpiStaffResult state状态：2 已审核通过
     */
    public static final Integer STATE_SUCCESS = 2;

    /**
     * kpiStaffResult state状态：3 已审核未通过
     */
    public static final Integer STATE_FAILED = 3;

    public static final int BYTE_BUFFER = 1024;

    private static final Set<String> METHOD_URL_SET = Sets.newConcurrentHashSet();

    public static Set<String> getMethodUrlSet() {
        return METHOD_URL_SET;
    }

    public static final String GET = "GET";
    /**
     * 用户注册默认角色
     */
    public static final int DEFAULT_REGISTER_ROLE = 5;

    public static final int BUFFER_MULTIPLE = 10;

    //验证码过期时间
    public static final Long PASS_TIME =  50000 * 60 *1000L;

    //根菜单节点
    public static final String ROOT_MENU = "0";

    //菜单类型，1：菜单  2：按钮操作
    public static final int TYPE_MENU = 1;

    //菜单类型，1：菜单  2：按钮操作
    public static final int TYPE_BUTTON = 2;

    public static final Boolean IS_PASS = false;

    //用户名登录
    public static final int LOGIN_USERNAME = 0;
    //手机登录
    public static final int LOGIN_MOBILE = 1;
    //邮箱登录
    public static final int LOGIN_EMAIL = 2;

    //启用
    public static final int ENABLE = 1;
    //禁用
    public static final int DISABLE = 0;

    public static final int FIXED_HEAD_LENGTH = 4;

    public static final String  DICTIONARY_ENCODEING_EXISTS= "code已存在，请重新输入";
    //管理机构类型
    public static final String ORG_ADMIN = "admin";

    /**
     * 添加速率.保证是单例的
     * */
    private static  RateLimiter limiter = RateLimiter.create(1000);

    public static RateLimiter getRateLimiter() {
        return limiter;
    }

    public static void setRateLimiter(RateLimiter rateLimiter) {
        limiter = rateLimiter;
    }

    /**
     * 使用url做为key,存放令牌桶 防止每次重新创建令牌桶
     * */
    private static final Map<String, RateLimiter> LIMITER_MAP = Maps.newConcurrentMap();

    public static Map<String, RateLimiter> getLimiterMap() {
        return LIMITER_MAP;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    private static  ApplicationContext context = null;

    public static final String REGEX_NUM = "[`~!@#$%^&*()\\+\\=\\{}|:\"?><【】\\/r\\/n]";
//    public static class FilePostFix{
//        protected static final String ZIP_FILE =".zip";
//
//        protected static final String [] IMAGES ={"jpg", "jpeg", "JPG", "JPEG", "gif", "GIF", "bmp", "BMP", "png"};
//        protected static final String [] ZIP ={"ZIP","zip","rar","RAR"};
//        protected static final String [] VIDEO ={"mp4","MP4","mpg","mpe","mpa","m15","m1v", "mp2","rmvb"};
//        protected static final String [] APK ={"apk","exe"};
//        protected static final String [] OFFICE ={"xls","xlsx","docx","doc","ppt","pptx"};
//
//    }
//    public class FileType{
//        protected static final int FILE_IMG = 1;
//        protected static final int FILE_ZIP = 2;
//        protected static final int FILE_VEDIO= 3;
//        protected static final int FILE_APK = 4;
//        protected static final int FIVE_OFFICE = 5;
//        protected static final String FILE_IMG_DIR= "/img/";
//        protected static final String FILE_ZIP_DIR= "/zip/";
//        protected static final String FILE_VEDIO_DIR= "/video/";
//        protected static final String FILE_APK_DIR= "/apk/";
//        protected static final String FIVE_OFFICE_DIR= "/office/";
//    }
//
//    public class RoleType{
//        //超级管理员
//        protected static final String SYS_ASMIN_ROLE= "sysadmin";
//        //管理员
//        protected static final String ADMIN= "admin";
//        //普通用户
//        protected static final String USER= "user";
//    }
}
