package com.homechart.app.commont;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017/6/1.
 * @file UrlConstants.java .
 * @brief url地址整合 .
 */
public class UrlConstants {

    public static final String USER_LOGIN = "https://api.idcool.com.cn/user/account/login";//用户登录
    public static final String JUDGE_MOBILE = "https://api.idcool.com.cn/security/verification/mobile";//验证用户手机号
    public static final String JIYAN_GETPARAM = "https://api.idcool.com.cn/security/verification/gt";  //获取极验验证参数
    public static final String SEND_MOBILE = "https://api.idcool.com.cn/security/verification/sendCaptcha";  //发送验证码
    public static final String REGISTER_MOBILE = "https://api.idcool.com.cn/user/account/signup";  // 验证验证码
    public static final String USER_LOGIN_BYYOUMENG = "https://api.idcool.com.cn/user/account/connect";  // 友盟登陆后，调用自己的后台登陆
    public static final String RESET_PASSWORD = "https://api.idcool.com.cn/user/account/resetPwd";  // 重置密码
    public static final String USER_INFO = "https://api.idcool.com.cn/user/user/getUserInfo";  // 获取用户信息
    public static final String FENSI_LIST = "https://api.idcool.com.cn/user/user/fansList";  // 获取粉丝列表
    public static final String GUANZHU_LIST = "https://api.idcool.com.cn/user/user/followList";  // 获取关注列表
    public static final String CITY_LIST = "https://api.idcool.com.cn/base/location/getList";  // 城市列表
    public static final String SHOUCANG_LIST = "https://api.idcool.com.cn/collect/single/list";  // 获取收藏列表
    public static final String SHOUCANG_ARTICLE_LIST = "https://api.idcool.com.cn/collect/article/slist";  // 获取收藏的文章列表
    public static final String SHOUCANG_SHOP_LIST = "https://api.idcool.com.cn/collect/product/slist";  // 获取用户收藏的商品列表
    public static final String SHAIJIA_LIST = "https://api.idcool.com.cn/single/single/list";  // 获取晒家图片列表
    public static final String SHAIJIA_ARTICLE_LIST = "https://api.idcool.com.cn/article/article/getList";  // 获取晒家文章列表
    public static final String SAVE_INFO = "https://api.idcool.com.cn/user/user/setUserInfo";  // 保存用户个人资料
    public static final String PUT_FILE = "https://api.idcool.com.cn/user/user/uploadAvatar";  // 上传头像
    public static final String PUT_IMAGE = "https://api.idcool.com.cn/base/image/uploadPicture";  // 上传图片
    public static final String BUNDLE_MOBILE = "https://api.idcool.com.cn/user/account/bindMobile";  // 绑定手机号码
    public static final String DELETE_SHOUCANG = "https://api.idcool.com.cn/collect/single/remove";  // 删除收藏列表
    public static final String DELETE_SHAIJIA = "https://api.idcool.com.cn/single/single/delete";  // 删除晒家列表
    public static final String DELETE_ARTICLE_SHAIJIA = "https://api.idcool.com.cn/article/article/delete";  // 删除晒家文章列表
    public static final String DELETE_ARTICLE_SHOUCANG = "https://api.idcool.com.cn/collect/article/remove";  // 删除收藏的文章
    public static final String DELETE_SHOP_SHOUCANG = "https://api.idcool.com.cn/collect/product/remove";  // 删除收藏的商品
    public static final String ADD_ARTICLE_SHOUCANG = "https://api.idcool.com.cn/collect/article/add";  // 添加收藏的文章
    public static final String REMOVE_ARTICLE_SHOUCANG = "https://api.idcool.com.cn/collect/article/remove";  // 删除收藏的文章
    public static final String ISSUE_BACK = "https://api.idcool.com.cn/base/feedback/add";  // 问题反馈
    public static final String MESSAGE_LIST = "https://api.idcool.com.cn/user/account/noticeList";  // 通知列表
    public static final String HOT_WORDS = "https://api.idcool.com.cn/search/search/hotWords";  // 搜索热词
    public static final String UNREADER_MSG = "https://api.idcool.com.cn/user/account/newNoticeNum";  // 未读消息数
    public static final String RECOMMEND_LIST = "https://api.idcool.com.cn/home/index/recommend";  // 获取首页推荐列表
    public static final String SEARCH_LIST = "https://api.idcool.com.cn/home/index/recommend";  // 新版获取搜索列表
    public static final String FAXIAN_LIST = "https://api.idcool.com.cn/search/search/item";  // 新版获取搜索列表
//    public static final String SEARCH_LIST = "https://api.idcool.com.cn/search/search/single";  // 获取搜索列表
    public static final String TAG_DATA = "https://api.idcool.com.cn/base/tag/getFilterTag";  // 获取筛选标签
    public static final String TUIJIAN_TAG = "https://api.idcool.com.cn/base/tag/getRecommendTag";  // 获取推荐标签
    public static final String GUANZHU = "https://api.idcool.com.cn/user/user/followMe";  //关注某个用户
    public static final String QUXIAO_GUANZHU = "https://api.idcool.com.cn/user/user/cancelFollow";  //取消关注某个用户
    public static final String COLOR_LIST = "https://api.idcool.com.cn/base/color/getFilterColor";  //获取颜色列表
    public static final String DOING_ACTIVITY = "https://api.idcool.com.cn/activity/activity/getDoingList";  //进行中的活动列表
    public static final String FABU = "https://api.idcool.com.cn/single/single/publish";  //用户发布晒家图片
    public static final String ITEM_FABU = "https://api.idcool.com.cn/single/single/getItemInfo";  //发布详情
    public static final String ADD_ZAN_PIC = "https://api.idcool.com.cn/like/single/add";  //对单张图片点赞
    public static final String ADD_ZAN_ARTICLE = " https://api.idcool.com.cn/like/article/add";  //对单张图片点赞
    public static final String REMOVE_ZAN_PIC = "https://api.idcool.com.cn/like/single/cancel";  //取消对单张图片点赞
    public static final String REMOVE_ZAN_ARTICLE = "https://api.idcool.com.cn/like/article/cancel";  //取消对文章点赞
    public static final String ADD_SHOUCANG = "https://api.idcool.com.cn/collect/single/add";  //收藏单张图片
    public static final String REMOVE_SHOUCANG = "https://api.idcool.com.cn/collect/single/remove";  //删除收藏的单图
    public static final String IMAGE_EDIT = "https://api.idcool.com.cn/single/single/modify";  //编辑晒家图片
    public static final String PING_LIST = "https://api.idcool.com.cn/comment/single/list";  //获取单图评论列表
    public static final String PING_REPLY = "https://api.idcool.com.cn/comment/single/reply";  //回复单图评论
    public static final String PING_IMAGE = "https://api.idcool.com.cn/comment/single/add";  //单图评论
    public static final String LIKE_CAI = "https://api.idcool.com.cn/single/single/recommend";  //猜你喜欢
    public static final String ACTIVITY_IMAGE = "https://api.idcool.com.cn/activity/activity/getSingleList";  //参与活动的图片列表
    public static final String ACTIVITY_DETAILS = "https://api.idcool.com.cn/activity/activity/getInfo";  //获取活动详情
    public static final String ADD_SHARED = "https://api.idcool.com.cn/base/share/updateShareNum";  //增加分享数
    public static final String SEARCH_ARTICLE = "https://api.idcool.com.cn/search/search/article";  //搜索文章列表
    public static final String DETAILS_ARTICLE = "https://api.idcool.com.cn/article/article/getInfo";  //获取文章详情
    public static final String PINGLIST_ARTICLE = "https://api.idcool.com.cn/comment/article/slist";  //获取文章的评论列表
    public static final String PING_ARTICLE_ALL = "https://api.idcool.com.cn/comment/article/add";  //对文章发表评论
    public static final String PING_ARTICLE_SINGLE = "https://api.idcool.com.cn/comment/article/reply";  //回复对文章发表的评论
    public static final String LIKE_ARTICLE = "https://api.idcool.com.cn/article/article/recommend";  //猜你喜欢
    public static final String PING_ADDZAN = "https://api.idcool.com.cn/like/comment/add";  //赞评论
    public static final String PING_REMOVEZAN = "https://api.idcool.com.cn/like/comment/cancel";  //取消评论的点赞
    public static final String LAST_VERSION = "https://api.idcool.com.cn/base/version/lastVersion";  //获取最新版本号
    public static final String HUODONG_LIST = "https://api.idcool.com.cn/activity/activity/getList";  //获取最新版本号
    public static final String ALL_SET = "https://api.idcool.com.cn/base/app/getConfig";  //APP启动时获取APP配置信息
    public static final String ALL_MSG = "https://api.idcool.com.cn/notice/notice/slist";  //获取全部消息列表
    public static final String GUANZHU_MSG = "https://api.idcool.com.cn/notice/follow/slist";  //关注消息
    public static final String DINGYUE_MSG = "https://api.idcool.com.cn/notice/subscribe/slist";  //获取订阅消息列表
    public static final String SHOUCANG_MSG = "https://api.idcool.com.cn/notice/collect/slist";  //收藏消息
    public static final String ALBUM_MSG = "https://api.idcool.com.cn/notice/addToAlbum/slist";  //加入灵感辑提醒消息
    public static final String PINGLUN_MSG = "https://api.idcool.com.cn/notice/comment/slist";  //评论消息
    public static final String DETAIL_SHOP = "https://api.idcool.com.cn/product/product/info";  //商品详情
    public static final String MORELIKE_SHOP = "https://api.idcool.com.cn/product/product/similar";  //相似商品
    //    public static final String CHECK_IMAGE = "https://api.idcool.com.cn/product/product/detect";  //检测图片中的物体
    public static final String CHECK_IMAGE = "https://api.idcool.com.cn/ai/ai/detect";  //检测图片中的物体新接口
    public static final String SEARCH_SHOP_IMAGE = "https://api.idcool.com.cn/product/product/search";  //从商品库中检索所圈商品的同款
    public static final String SHOUCANG_ADD_SHOP = "https://api.idcool.com.cn/collect/product/add";  //收藏商品
    public static final String SHOUCANG_REMOVE_SHOP = "https://api.idcool.com.cn/collect/product/remove";  //删除收藏的商品
    public static final String HISTORY_SHIBIE = "https://api.idcool.com.cn/product/product/history";  //检测历史记录
    public static final String NEW_HISTORY_SHIBIE = "https://api.idcool.com.cn/ai/ai/history";  //新的检测历史记录
    public static final String NEW_HISTORY_SHIBIE_DEFAULT = "https://api.idcool.com.cn/ai/ai/defaultImage";  //默认的检测历史记录
    public static final String TYPE_SHOP = "https://api.idcool.com.cn/ai/ai/category";  //获取商品分类列表
    public static final String NEW_SEARCH_SHOPS = "https://api.idcool.com.cn/ai/ai/search";  //以图搜索图
    public static final String JUBAO = "https://api.idcool.com.cn/report/report/add";  //提交举报
    public static final String REMOVE_SHOP = "https://api.idcool.com.cn/product/product/remove";  //移除下架商品
    public static final String INSPIRATION_SERIES = "https://api.idcool.com.cn/album/album/getListByUserId";  //获取用户专辑列表
    public static final String INSPIRATION_XIANGGUAN = "https://api.idcool.com.cn/album/album/getListByItemId";  //获取添加过图片的专辑列表
    public static final String INSPIRATION_PICS = "https://api.idcool.com.cn/album/album/getItems";  //获取专辑图片列表
    public static final String CREATE_INSPIRATION = "https://api.idcool.com.cn/album/album/create";  //创建新专辑
    public static final String ADD_IMG_INSPIRATION = "https://api.idcool.com.cn/item/item/addToAlbum";  //将图片加入灵感辑
    public static final String INSPIRATION_DETAIL = "https://api.idcool.com.cn/album/album/getAlbumInfo";  //获取专辑详情
    public static final String REMOVE_PIC = "https://api.idcool.com.cn/item/item/remove";  //删除图片
    public static final String EDIT_IMAGE = "https://api.idcool.com.cn/item/item/modify";  //编辑图片
    public static final String DINGYUE = "https://api.idcool.com.cn/subscribe/album/add";  //订阅一个专辑
    public static final String DINGYUE_QUXIAO = "https://api.idcool.com.cn/subscribe/album/remove";  //批量取消订阅的专辑
    public static final String COPY_INSPIRATION_PIC = "https://api.idcool.com.cn/item/item/copyToAlbum";  //将图片复制到其他灵感辑
    public static final String MOVE_INSPIRATION_PIC = "https://api.idcool.com.cn/item/item/moveToAlbum";  //将图片移动到其他灵感辑
    public static final String REMOVE_INSPIRATION = "https://api.idcool.com.cn/album/album/remove";  //删除专辑
    public static final String EDIT_INSPIRATION = "https://api.idcool.com.cn/album/album/modify";  //编辑专辑
    public static final String DINGYUE_LIST = "https://api.idcool.com.cn/subscribe/album/getListByUserId";  //用户订阅专辑列表
    public static final String DELETE_DINGYUE = "https://api.idcool.com.cn/subscribe/album/remove";  //批量取消订阅的专辑
    public static final String GET_HUODONG_INFO = "https://api.idcool.com.cn/activity/activity/getInfo";  //获取一个活动的详细信息
    public static final String ZHUANJI_LIST = "https://api.idcool.com.cn/activity/activity/getAlbumList";  //获取参与活动的专辑列表
    public static final String ADD_HUODONG = "https://api.idcool.com.cn/activity/activity/join";  //报名参加活动
    public static final String PRIZE_USER = "https://api.idcool.com.cn/activity/activity/getPrizeUser";  //获取获奖名单
    public static final String SEARCH_IMAGE = "https://api.idcool.com.cn/ai/ai/searchItem";  //搜索相似图片
    public static final String IMAGE_USER = "https://api.idcool.com.cn/item/item/getListByUserId";  //获取一个用户的所有图片
    public static final String JUBAO_LIST = "https://api.idcool.com.cn/report/report/getList";  //获取举报选项
    public static final String PINGDAO_TAGS = "https://api.idcool.com.cn/base/tag/getChannelTag";  //获取频道列表及关联标签
    public static final String GUANLIAN_TAGS = "https://api.idcool.com.cn/base/tag/getRelationTag";  //获取标签的关联标签

    public static final String TAG_DINGYUE = "https://api.idcool.com.cn/subscribe/tag/add";  //订阅一个可订阅的标签
    public static final String TAG_DINGYUE_QUXIAO = "https://api.idcool.com.cn/subscribe/tag/remove";  //取消已订阅的标签
    public static final String DINGYUE_GUANLI_LIST = "https://api.idcool.com.cn/subscribe/tag/subscribeTagList";  //获取订阅管理标签列表

}
