package cn.net.health.xiyou.shiro.mongo.common;

/***
 * 一些需要用equal 判断等的字段
 */

/**
 * @author xiyou
 * @version 1.2
 * @date 2019/11/6 11:45
 */
public class Constants {

    /**
     * 查找用户的字段类型
     */
    public static final String USER_USER_NAME = "username";

    /**
     * 真
     */
    public static final String TRUE_STATUS = "true";


    /***
     * 假
     */
    public static final String FALSE_STATUS = "false";

    /***
     * 查询全部
     */
    public static final String SELECT_ALL = "all";

    /**
     * git上传类型
     */
    public static final String GIT_IMPORT = "git";

    /**
     * svn上传类型
     */
    public static final String SVN_IMPORT = "svn";

    /***
     * file 上传类型
     */
    public static final String FILE_IMPORT = "file";


    /***
     * folder上传类型
     */
    public static final String FOLDER_IMPORT = "folder";


    /***
     * 组件类型是开源还是闭源
     */
    public static final String MODULE_OPEN = "open";


    /***
     *  组件类型是开源还是闭源
     */
    public static final String MODULE_CLOSE = "close";

    /***
     *  逆序
     */
    public static final String DESC = "desc";


    /***
     *  正序
     */
    public static final String ASC = "asc";


    /***
     *  cve漏洞
     */
    public static final String BUG_CVE = "nvd";


    /***
     * VnlnDB漏洞
     */
    public static final String BUG_VNLNDB = "VnlnDB";


    /***
     *  CoNET漏洞
     */
    public static final String BUG_CONET = "CoNET";


    /***
     *  右边文件数网络连接
     */
    public static final String NET_FILE_CONTENT = "net_file_content";


    /***
     *  分析服务连接
     */
    public static final String NET_ANALY = "net_analy";


    /***
     *  static类型
     */
    public static final String STATIC_TYPE = "static";


    /***
     *  userSession有问题
     */
    public static final String USER_SESSION = "userSession";


    /***
     *  totalSize
     */
    public static final String TOTAL_SIZE = "totalSize";


    /***
     *  thisPageSize
     */
    public static final String THIS_PAGE_SIZE = "thisPageSize";


    /***
     *  dataContent
     */
    public static final String DATA_CONTENT = "dataContent";
}
