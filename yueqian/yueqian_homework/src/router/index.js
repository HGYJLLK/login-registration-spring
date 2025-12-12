import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Layout from '../views/Layout.vue'
import Admin from '../views/Admin.vue'
import Singers from '../views/Singers.vue'
import SongLists from '../views/SongLists.vue'
import Songs from '../views/Songs.vue'
import Favorites from '../views/Favorites.vue'
import Profile from '../views/Profile.vue'

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
    redirect: '/home/singers',
    children: [
      {
        path: 'singers',
        name: 'Singers',
        component: Singers
      },
      {
        path: 'songlists',
        name: 'SongLists',
        component: SongLists
      },
      {
        path: 'songs',
        name: 'Songs',
        component: Songs
      },
      {
        path: 'favorites',
        name: 'Favorites',
        component: Favorites
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile
      }
    ]
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const username = localStorage.getItem('username')
  const isAdmin = localStorage.getItem('isAdmin') === 'true'
  const publicPages = ['/login', '/register', '/admin']  // 管理员页面无需登录也可访问
  const isPublicPage = publicPages.includes(to.path)

  if (!isPublicPage && !username) {
    // 未登录，跳转到登录页
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && username) {
    // 已登录，根据用户类型跳转
    if (isAdmin) {
      next('/admin')
    } else {
      next('/home')
    }
  } else {
    next()
  }
})

export default router
