<template>
  <router-view />
</template>

<script setup>
import { onMounted } from 'vue'
import { initTheme } from './themes'

onMounted(() => {
  // 初始化主题
  initTheme()
})
</script>

<style>
/* CSS 变量将由主题系统动态设置 */
:root {
  /* 默认深色主题变量 */
  --bg-primary: #121212;
  --bg-secondary: #181818;
  --bg-tertiary: #282828;
  --bg-hover: #2a2a2a;

  --text-primary: #FFFFFF;
  --text-secondary: #B3B3B3;
  --text-tertiary: #808080;

  --color-primary: #1db954;
  --color-primary-hover: #1ed760;
  --color-primary-active: #1aa34a;

  --border-color: #404040;
  --border-light: #282828;

  --color-success: #1db954;
  --color-warning: #FF9900;
  --color-danger: #E22134;
  --color-info: #3b82f6;

  --shadow: 0 8px 24px rgba(0,0,0,.5);
  --card-bg: #181818;
  --input-bg: #121212;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  background-color: var(--bg-primary);
  overflow: hidden;
  transition: background-color 0.3s ease;
}

#app {
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  height: 100vh;
  background-color: var(--bg-primary);
  color: var(--text-primary);
  transition: all 0.3s ease;
}

/* 自定义滚动条 - 使用主题变量 */
::-webkit-scrollbar {
  width: 12px;
}

::-webkit-scrollbar-track {
  background: var(--bg-primary);
}

::-webkit-scrollbar-thumb {
  background: var(--border-color);
  border-radius: 6px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--color-primary);
}
</style>
