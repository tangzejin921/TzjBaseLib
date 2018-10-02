# 7.0 选择图片上传的适配

## 1. provider

```.xml
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```
## 2.file_paths.xml
```.xml
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <files-path
        name="share"
        path="share" />
    <cache-path
        name="share"
        path="share"/>
    <external-path
        name="share"
        path="share"/>
    <external-files-path
        name="share"
        path="share" />
    <external-cache-path
        name="share"
        path="share" />
    <root-path
        name="share"
        path="" />
</paths>
```
## 3.intent 加权限
    
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);  

## 4.给Uri授予临时权限

```java
class Util{
    public static Uri parUri(Context ctx,File cameraFile) {
        Uri imageUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }
}
```