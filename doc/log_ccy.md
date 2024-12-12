Vue 的模板要求每个组件的模板文件都必须有一个唯一的根节点。
template不代表根节点

这么写导致页面切换无法加载
<template>
    <h3>文件管理</h3>
    <vuecmf-fileexplorer>
    </vuecmf-fileexplorer>
</template>

正确写法：
<template>
    <div>
        <h3>文件管理</h3>
        <vuecmf-fileexplorer>
        </vuecmf-fileexplorer>
    </div>
</template>