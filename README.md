# Burpsuite-XFFHelper

某些网站使用XFF一些列头可以直接绕过身份认证，于是编写了这款插件

自动添加XFF等一系列的请求头，用来检测一些未授权访问

可以在repeater/intruder/proxy等多个菜单的请求中右键使用

如果已经有重复的头，那么会替换了value，保证不会有重复的请求头

过两天考虑自定义ip，目前是写死的127.0.0.1，实际上一些其他内网ip也是有效的

![](https://xuyiqing-1257927651.cos.ap-beijing.myqcloud.com/burpsuite/xff.png)


