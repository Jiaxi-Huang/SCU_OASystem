<template>
  <div class="project-intro-container">
    <el-row style="margin: 15px">
      <el-col :span="24" class="page-title-box">
        <h4 class="page-title">项目介绍</h4>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card shadow="hover" class="card">
          <section class="project-info">
            <h5 class="text-muted">项目信息</h5>
            <p><strong>项目名称:</strong> OA办公系统</p>
            <p><strong>项目启动日期:</strong> 2024-11-13</p>
            <p><strong>预计完成日期:</strong> 205-01-10</p>
          </section>
          <section class="project-description">
            <h5 class="text-muted">项目简介</h5>
            <p>这是一个基于 SpringBoot 和 Vue.js 开发的 OA 办公系统项目。该项目旨在为企业提供一个集成了用户管理、登录管理、日程管理、审批管理、文档管理和通知管理等功能的办公系统。</p>
          </section>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card shadow="hover" class="card">
          <section class="team">
            <h5 class="text-muted">团队成员</h5>
            <div class="team-member">
              <h6>组长</h6>
              <strong>黄家曦</strong>
            </div>
            <div class="team-member">
              <h6>组员</h6>
              <strong>郭源</strong>
            </div>
            <div class="team-member">
              <h6>组员</h6>
              <strong>何海洋</strong>
            </div>
            <div class="team-member">
            <h6>组员</h6>
            <strong>陈春宇</strong>
            </div>
            <div class="team-member">
            <h6>组员</h6>
            <strong>桑郎平措</strong>
            </div>
          </section>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>import { ref } from 'vue'

interface TeamMember {
  name: string;
  role: string;
  email: string;
}

const projectInfo = {
  name: '项目A',
  startDate: '2023-01-01',
  endDate: '2023-12-31',
};

const projectDescription = '这是一个旨在解决X问题的项目，通过Y技术实现...';

const team: TeamMember[] = [
  { name: '张三', role: '组长', email: 'zhangsan@example.com' },
  { name: '李四', role: '开发者', email: 'lisi@example.com' },
  { name: '王五', role: '测试人员', email: 'wangwu@example.com' },
];
</script>

<style scoped lang="stylus">
.project-intro-container
  color black
  padding 20px
  background-color #fafbfe

  .page-title-box
    box-sizing border-box
    display flex
    flex-direction row
    justify-content space-between
    align-items center
    color #6c757d
    margin-bottom 20px

    .page-title
      font-size 24px
      margin 0
      line-height 1.2
      color inherit
      font-weight 700

  :deep(.el-card)
    margin-bottom 30px
    border-radius 8px
    box-shadow 0 2px 12px 0 rgba(0, 0, 0, 0.1)

  .card
    .text-muted
      color #98a6ad

    .text-success
      color #0acf97

    .text-danger
      color #fa5c7c

    .text-primary
      color #409eff

    .text-light
      color #e3eaef

    .text-white
      color white

    .text-center
      display flex
      flex-direction row
      justify-content center
      align-items center

    .font-nomal
      font-weight 400

    .team-member
      margin-bottom 15px

      h6
        font-weight 600
        margin-bottom 5px

      p
        margin 5px 0

      strong
        font-weight 600
        color #333

    section
      margin-bottom 20px

      &:last-child
        margin-bottom 0

      h5
        font-size 18px
        margin-bottom 10px
        color #333

      p
        font-size 16px
        line-height 1.5
        color #6c757d
</style>
