<template>
  <div class="theme-switch">
    <el-dropdown trigger="click" @command="handleThemeChange">
      <span class="theme-trigger">
        <span class="theme-icon">{{ currentThemeIcon }}</span>
      </span>
      <template #dropdown>
        <el-dropdown-menu class="theme-dropdown">
          <div class="theme-dropdown-header">
            选择主题
          </div>
          <el-dropdown-item
            v-for="(theme, key) in themes"
            :key="key"
            :command="key"
            :class="{ 'is-active': currentTheme === key }"
          >
            <div class="theme-option">
              <span class="theme-option-icon">{{ theme.icon }}</span>
              <span class="theme-option-name">{{ theme.name }}</span>
              <span v-if="currentTheme === key" class="theme-option-check">✓</span>
            </div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { themes, applyTheme, getCurrentTheme } from '../themes'

const currentTheme = ref('dark')

const currentThemeIcon = computed(() => {
  return themes[currentTheme.value]?.icon || '🌙'
})

const handleThemeChange = (themeName) => {
  currentTheme.value = themeName
  applyTheme(themeName)
}

onMounted(() => {
  currentTheme.value = getCurrentTheme()
})
</script>

<style scoped>
.theme-switch {
  position: relative;
}

.theme-trigger {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  color: var(--text-primary);
  transition: all 0.3s ease;
}

.theme-trigger:hover {
  background-color: var(--bg-hover);
  color: var(--color-primary);
}

.theme-icon {
  font-size: 20px;
  display: inline-block;
  line-height: 1;
}

:deep(.theme-dropdown) {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 8px 0;
  box-shadow: var(--shadow);
  min-width: 200px;
}

.theme-dropdown-header {
  padding: 12px 16px 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  border-bottom: 1px solid var(--border-light);
  margin-bottom: 8px;
}

:deep(.el-dropdown-menu__item) {
  padding: 0;
  color: var(--text-primary);
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: var(--bg-hover);
}

:deep(.el-dropdown-menu__item.is-active) {
  background-color: var(--bg-tertiary);
}

.theme-option {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  width: 100%;
}

.theme-option-icon {
  font-size: 18px;
  width: 24px;
  text-align: center;
}

.theme-option-name {
  flex: 1;
  font-size: 14px;
  color: var(--text-primary);
}

.theme-option-check {
  color: var(--color-primary);
  font-weight: bold;
  font-size: 16px;
}

/* 响应式 */
@media (max-width: 768px) {
  .theme-trigger {
    width: 36px;
    height: 36px;
  }

  .theme-icon {
    font-size: 18px;
  }
}
</style>
