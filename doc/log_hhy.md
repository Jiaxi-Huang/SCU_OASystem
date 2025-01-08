
### 接口授权

2024/11/18

gy说的那个“添加完新页面后去mock里面授权的地方把对应的路径加上”的用后端接口的话路径是在src\main\java\com\example\backend\controllers\Auth.java，mock文件夹是还没有后端的时候用来临时模拟后端响应的，现在后端搭好了mock就已经弃用了


### yarn安装错误

2024/12/2

如果yarn install的时候husky提示错误的话，就在vue文件夹下git init，建一个空的仓库


### 获取用户角色

2024/12/21

accessToken -> userId -> userService.getUserById.getRole