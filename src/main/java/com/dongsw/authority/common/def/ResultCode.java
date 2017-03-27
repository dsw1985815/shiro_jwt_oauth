package com.dongsw.authority.common.def;

/**
 * @author 顾文伟
 *         自定义返回码或者其他消息
 *         1.1xx：信息，请求收到，继续处理
 *         2.2xx：成功，行为被成功地接受、理解和采纳
 *         3.3xx：重定向，为了完成请求，必须进一步执行的动作
 *         4.4xx：客户端错误，请求包含语法错误或者请求无法实现
 *         5.5xx：服务器错误，服务器不能实现一种明显无效的请求
 *         <p>
 *         update code	liaozhanggen
 *         1. delete code "" 减少JVM缓存占用
 *         2. class => interface
 */
public class ResultCode {



    //	200 OK - [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。
//	201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。
//	202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务）
//	204 NO CONTENT - [DELETE]：用户删除数据成功。
//	400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
//	401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。
//	403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
//	404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
//	406 Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
//	410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。
//	422 Unprocesable entity - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。
//	500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。

    //	2xx  成功
    public static final int SUCCESS = 200;                    //服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。
    public static final int SUCCESS_CREATED = 201;            //[POST/PUT/PATCH]：用户新建或修改数据成功。
    public static final int SUCCESS_ACCEPTED = 202;            //表示一个请求已经进入后台排队（异步任务）
    public static final int SUCCESS_RETURNPART = 203;        //部分信息 — 返回的信息只是一部分。
    public static final int SUCCESS_NOCONTENT = 204;            //用户删除数据成功。

    //	3xx  重定向
    public static final int REDIRECT_MOVED = 301;            //已移动 — 请求的数据具有新的位置且更改是永久的。
    public static final int REDIRECT_FINDED = 302;            //已找到 — 请求的数据临时具有不同 URI。
    public static final int REDIRECT_FINDOTHER = 303;        //请参阅其它 — 可在另一 URI 下找到对请求的响应，且应使用 GET 方法检索此响应。
    public static final int REDIRECT_UNMODIFIED = 304;        //未修改 — 未按预期修改文档。
    public static final int REDIRECT_USEPROXY = 305;            //使用代理 — 必须通过位置字段中提供的代理来访问请求的资源。
    public static final int REDIRECT_UNUSED = 306;            //未使用 — 不再使用；保留此代码以便将来使用。

    //	4xx  客户机中出现的错误
    public static final int FAILED = 400;                    //	400  用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
    public static final int FAILED_UNAUTHORIZED = 401;        //	401  表示用户没有权限（令牌、用户名、密码错误）。
    public static final int FAILED_NEEDPAY = 402;            //	402  需要付款 — 表示计费系统已有效。
    public static final int FAILED_BAN = 403;                //	403  表示用户得到授权（与401错误相对），但是访问是被禁止的。
    public static final int FAILED_NOTFIND = 404;            //	404  用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
    public static final int FAILED_NOTACCEPTABLE = 406;    //	406 用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
    public static final int FAILED_PORXYREQ = 407;            //	407  代理认证请求 — 客户机首先必须使用代理认证自身。
    public static final int FAILED_GONE = 410;                //	410 用户请求的资源被永久删除，且不会再得到的。
    public static final int FAILED_TYPENOSUPPORT = 415;    //	415  介质类型不受支持 — 服务器拒绝服务请求，因为不支持请求实体的格式。
    public static final int FAILED_UNPROCESABLE = 422;        //	422[POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。

    //	5xx  服务器中出现的错误
    public static final int SERVERERROR_INTERNAL = 500;//	500  内部错误 — 因为意外情况，服务器不能完成请求。
    public static final int SERVERERROR_NOEXECUTION = 501;//	501  未执行 — 服务器不支持请求的工具。
    public static final int SERVERERROR_BADGATEWAY = 502;//	502  错误网关 — 服务器接收到来自上游服务器的无效响应。
    public static final int SERVERERROR_UNGETSERVER = 503;//	503  无法获得服务 — 由于临时过载或维护，服务器无法处理请求。


    /******start code guwenwei 2016-11-28*****************/
    // 服务端返回结果的标志
    public static final String STATUS = "status";
    // 服务端返回结果原因
    public static final String MESSAGE = "message";
    public static final String MSG_LOGIN_SUCCESS = "登录成功";

    public static final String MSG_RECOLLECTION = "已经收藏该条信息，请勿重复操作";
    public static final String MSG_USERNOTEXIT = "用户不存在或角色权限缺失";

    public static final String MSG_SYSTEM_ERROR = "系统错误";
    public static final String MSG_PARAMS_NULL = "请求参数为空";

    public static final String MSG_DATA_EMPTY = "数据不存在";
    public static final String MSG_PARAMS_ERROR = "参数传递错误";


    public static final String MSG_RESPONSE_NULL = "服务端返回结果为空";
    public static final String MSG_QUERY_SUCCESS = "查询成功";
    public static final String MSG_CREATE_SUCCESS = "创建成功";
    public static final String MSG_UPDATE_SUCCESS = "修改成功";
    public static final String MSG_DELETE_SUCCESS = "删除成功";
    public static final String MSG_UPDATEPART_SUCCESS = "部分修改成功";
    public static final String MSG_UPDATE_FAILD = "修改失败";
    public static final String MSG_DELETE_FAILD = "删除失败";
    // 返回给前段页面成功或失败的标志
    public static final String MSG_ISERROR = "isError";
    // 执行删除操作
    public static final String MSG_DELETE = "DELETE";
    /******start code guwenwei 2016-11-28*****************/

    /******start code liaozhanggen 2016-11-10*****************/

    public static final String MSG_SUCCESS = "SUCCESS";
    public static final String MSG_ERROR = "ERROR";
    public static final String MSG_CREATE_ERROR = "CREATE_ERROR";
    public static final String MSG_UPDATE_ERROR = "UPDATE_ERROR";
    public static final String MSG_DATA_ISEMPTY = "DATA_ISEMPTY";
    public static final String MSG_OPRETOID_COULDNOT_BE_NULL = "操作员编号不能为空";
    /******end code liaozhanggen 2016-11-10*****************/

    private ResultCode() {
    }
}
