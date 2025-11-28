import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Layout from '../views/Layout.vue'
import Nav1 from '../views/Nav1.vue'
import Nav2 from '../views/Nav2.vue'
import Nav3 from '../views/Nav3.vue'
import Nav4 from '../views/Nav4.vue'
import MusicList from '../views/MusicList.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/home',
    name: 'Home',
    component: Layout,
    redirect: '/home/nav1',
    children: [
      {
        path: 'nav1',
        name: 'Nav1',
        component: Nav1
      },
      {
        path: 'nav2',
        name: 'Nav2',
        component: Nav2
      },
      {
        path: 'nav3',
        name: 'Nav3',
        component: Nav3
      },
      {
        path: 'nav4',
        name: 'Nav4',
        component: Nav4
      },
      {
        path: 'music',
        name: 'Music',
        component: MusicList
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const username = localStorage.getItem('username')
  const publicPages = ['/login', '/register']
  const isPublicPage = publicPages.includes(to.path)

  if (!isPublicPage && !username) {
    // 未登录，跳转到登录页
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && username) {
    // 已登录，跳转到首页
    next('/home')
  } else {
    next()
  }
})

export default router
