# BaseLib
- com.tzj.baselib

### 引用的包

    compileOnly "com.android.support:design:$_supportVersion"
    //滑动返回(子类不需要用到)
    implementation 'me.imid.swipebacklayout.lib:library:1.3.0'
    //RecyclerView
    api 'com.github.tzjandroid:TzjRecyclerView:c6822559c2'
    //下拉刷新
    api 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.5.1'
    //glide 图片加载
    api 'com.github.bumptech.glide:glide:4.7.1'
}

### BaseActivity 

- 权限
- startActivity 拦截
- onActivityResult 直接接口返回
- Delegate
- 加载Dialog
- 右滑返回
- 用系统的标题栏 **还没做**

### BaseFragment

- 缓存View
- 懒加载
- 调用 activity 方法

### Dialog
* BaseDialog
    - 调用方法设置，不用style 方式
    - clearBack 无背景色
    - setFromBottom 从底部弹出
* listDialog
    - 高度控制（最大高度）
    - 底部两个按钮
    
### BaseLibFragmentAdapter
- 加了刷新当前页的方法

### View
* JsWebView
    - 上传文件
    - alipay 的跳转
    - 应运市场的跳转
    - 结束时清除网页（有的背景音乐停不了）
    - 清楚Cookie缓存
    - UserAgent 加入 app信息，用户信息
    - 定位 权限
    - 断网显示
    - title 的显示
    - 进程条
    - 下拉刷新
    - js 的支持
* 时间滚轮
* 大图展示,可缩放,可旋转
* Banner 左右，上下
* 搜索记录

### Env
- File
- User
- Login

### Util


- UtilBle
- UtilShare
- UtilFile
- UtilSystem
- UtilDate
- UtilLog
- UtilDowload
- Util加解密
- UtilID
- UtilUi
- UtilView
