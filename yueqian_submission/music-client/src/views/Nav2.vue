<template>
  <div class="nav2-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>导航二</span>
        </div>
      </template>
      <div class="content">
        <el-empty description="导航二页面内容待开发" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.nav2-container {
  width: 100%;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.content {
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
