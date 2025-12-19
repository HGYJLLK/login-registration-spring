// 主题配置文件 - 定义多个配色方案

export const themes = {
  // 1. 深色主题（默认 - Spotify 风格）
  dark: {
    name: '深色主题',
    icon: '🌙',
    colors: {
      // 背景色
      '--bg-primary': '#121212',
      '--bg-secondary': '#181818',
      '--bg-tertiary': '#282828',
      '--bg-hover': '#2a2a2a',

      // 文字色
      '--text-primary': '#FFFFFF',
      '--text-secondary': '#B3B3B3',
      '--text-tertiary': '#808080',

      // 主题色
      '--color-primary': '#1db954',
      '--color-primary-hover': '#1ed760',
      '--color-primary-active': '#1aa34a',

      // 边框色
      '--border-color': '#404040',
      '--border-light': '#282828',

      // 状态色
      '--color-success': '#1db954',
      '--color-warning': '#FF9900',
      '--color-danger': '#E22134',
      '--color-info': '#3b82f6',

      // 特殊元素
      '--shadow': '0 8px 24px rgba(0,0,0,.5)',
      '--card-bg': '#181818',
      '--input-bg': '#121212',
    }
  },

  // 2. 浅色主题（Apple Music 风格）
  light: {
    name: '浅色主题',
    icon: '☀️',
    colors: {
      '--bg-primary': '#FFFFFF',
      '--bg-secondary': '#F5F5F7',
      '--bg-tertiary': '#E8E8ED',
      '--bg-hover': '#F0F0F5',

      '--text-primary': '#1D1D1F',
      '--text-secondary': '#6E6E73',
      '--text-tertiary': '#86868B',

      '--color-primary': '#FA243C',
      '--color-primary-hover': '#FB4258',
      '--color-primary-active': '#D91E33',

      '--border-color': '#D2D2D7',
      '--border-light': '#E8E8ED',

      '--color-success': '#34C759',
      '--color-warning': '#FF9500',
      '--color-danger': '#FF3B30',
      '--color-info': '#007AFF',

      '--shadow': '0 4px 16px rgba(0,0,0,.1)',
      '--card-bg': '#FFFFFF',
      '--input-bg': '#F5F5F7',
    }
  },

  // 3. 紫色梦幻主题
  purple: {
    name: '紫色梦幻',
    icon: '💜',
    colors: {
      '--bg-primary': '#0F0A1E',
      '--bg-secondary': '#1A1333',
      '--bg-tertiary': '#2D1B4E',
      '--bg-hover': '#3d2766',

      '--text-primary': '#F0E6FF',
      '--text-secondary': '#C4B5D9',
      '--text-tertiary': '#9580B3',

      '--color-primary': '#A855F7',
      '--color-primary-hover': '#C084FC',
      '--color-primary-active': '#9333EA',

      '--border-color': '#4C3575',
      '--border-light': '#2D1B4E',

      '--color-success': '#8B5CF6',
      '--color-warning': '#F59E0B',
      '--color-danger': '#EF4444',
      '--color-info': '#06B6D4',

      '--shadow': '0 8px 32px rgba(168, 85, 247, 0.2)',
      '--card-bg': '#1A1333',
      '--input-bg': '#0F0A1E',
    }
  },

  // 4. 蓝色科技主题
  blue: {
    name: '蓝色科技',
    icon: '🔵',
    colors: {
      '--bg-primary': '#0A1929',
      '--bg-secondary': '#132F4C',
      '--bg-tertiary': '#1E4976',
      '--bg-hover': '#2A5A8F',

      '--text-primary': '#E7EBF0',
      '--text-secondary': '#B2BAC2',
      '--text-tertiary': '#8B95A1',

      '--color-primary': '#3B82F6',
      '--color-primary-hover': '#60A5FA',
      '--color-primary-active': '#2563EB',

      '--border-color': '#2E5A7D',
      '--border-light': '#1E4976',

      '--color-success': '#10B981',
      '--color-warning': '#F59E0B',
      '--color-danger': '#EF4444',
      '--color-info': '#06B6D4',

      '--shadow': '0 8px 32px rgba(59, 130, 246, 0.2)',
      '--card-bg': '#132F4C',
      '--input-bg': '#0A1929',
    }
  },

  // 5. 粉色甜美主题
  pink: {
    name: '粉色甜美',
    icon: '🌸',
    colors: {
      '--bg-primary': '#FFF0F5',
      '--bg-secondary': '#FFE4EC',
      '--bg-tertiary': '#FFD4E5',
      '--bg-hover': '#FFC0D9',

      '--text-primary': '#4A1942',
      '--text-secondary': '#8B4F7D',
      '--text-tertiary': '#B87BA5',

      '--color-primary': '#EC4899',
      '--color-primary-hover': '#F472B6',
      '--color-primary-active': '#DB2777',

      '--border-color': '#F9A8D4',
      '--border-light': '#FBCFE8',

      '--color-success': '#10B981',
      '--color-warning': '#F59E0B',
      '--color-danger': '#EF4444',
      '--color-info': '#06B6D4',

      '--shadow': '0 4px 16px rgba(236, 72, 153, 0.15)',
      '--card-bg': '#FFFFFF',
      '--input-bg': '#FFE4EC',
    }
  },

  // 6. 绿色森林主题
  green: {
    name: '绿色森林',
    icon: '🌲',
    colors: {
      '--bg-primary': '#0C1E0F',
      '--bg-secondary': '#16301A',
      '--bg-tertiary': '#234628',
      '--bg-hover': '#2F5C35',

      '--text-primary': '#E8F5E9',
      '--text-secondary': '#A5D6A7',
      '--text-tertiary': '#81C784',

      '--color-primary': '#22C55E',
      '--color-primary-hover': '#4ADE80',
      '--color-primary-active': '#16A34A',

      '--border-color': '#3D7043',
      '--border-light': '#234628',

      '--color-success': '#10B981',
      '--color-warning': '#F59E0B',
      '--color-danger': '#EF4444',
      '--color-info': '#06B6D4',

      '--shadow': '0 8px 32px rgba(34, 197, 94, 0.2)',
      '--card-bg': '#16301A',
      '--input-bg': '#0C1E0F',
    }
  },

  // 7. 橙色活力主题
  orange: {
    name: '橙色活力',
    icon: '🔥',
    colors: {
      '--bg-primary': '#1C0F05',
      '--bg-secondary': '#2D1A0A',
      '--bg-tertiary': '#422710',
      '--bg-hover': '#5A3515',

      '--text-primary': '#FFF7ED',
      '--text-secondary': '#FED7AA',
      '--text-tertiary': '#FDBA74',

      '--color-primary': '#F97316',
      '--color-primary-hover': '#FB923C',
      '--color-primary-active': '#EA580C',

      '--border-color': '#7C2D12',
      '--border-light': '#422710',

      '--color-success': '#10B981',
      '--color-warning': '#F59E0B',
      '--color-danger': '#EF4444',
      '--color-info': '#06B6D4',

      '--shadow': '0 8px 32px rgba(249, 115, 22, 0.2)',
      '--card-bg': '#2D1A0A',
      '--input-bg': '#1C0F05',
    }
  },

  // 8. 青色清新主题
  cyan: {
    name: '青色清新',
    icon: '🌊',
    colors: {
      '--bg-primary': '#F0FDFA',
      '--bg-secondary': '#CCFBF1',
      '--bg-tertiary': '#99F6E4',
      '--bg-hover': '#5EEAD4',

      '--text-primary': '#134E4A',
      '--text-secondary': '#115E59',
      '--text-tertiary': '#0F766E',

      '--color-primary': '#06B6D4',
      '--color-primary-hover': '#22D3EE',
      '--color-primary-active': '#0891B2',

      '--border-color': '#5EEAD4',
      '--border-light': '#99F6E4',

      '--color-success': '#10B981',
      '--color-warning': '#F59E0B',
      '--color-danger': '#EF4444',
      '--color-info': '#0EA5E9',

      '--shadow': '0 4px 16px rgba(6, 182, 212, 0.15)',
      '--card-bg': '#FFFFFF',
      '--input-bg': '#CCFBF1',
    }
  },
}

// 默认主题
export const defaultTheme = 'dark'

// 应用主题到DOM
export function applyTheme(themeName) {
  const theme = themes[themeName] || themes[defaultTheme]
  const root = document.documentElement

  Object.entries(theme.colors).forEach(([key, value]) => {
    root.style.setProperty(key, value)
  })

  // 保存到 localStorage
  localStorage.setItem('theme', themeName)
}

// 获取当前主题
export function getCurrentTheme() {
  return localStorage.getItem('theme') || defaultTheme
}

// 初始化主题
export function initTheme() {
  const savedTheme = getCurrentTheme()
  applyTheme(savedTheme)
  return savedTheme
}
