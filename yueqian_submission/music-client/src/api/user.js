import request from './request'

// 登录
export const login = (data) => {
  return request({
    url: '/auth/login',
    method: 'POST',
    data
  })
}

// 注册
export const register = (data) => {
  return request({
    url: '/auth/register',
    method: 'POST',
    data
  })
}

// 获取所有用户
export const getAllUsers = () => {
  return request({
    url: '/users',
    method: 'GET'
  })
}

// 更新用户信息
export const updateUser = (id, data) => {
  return request({
    url: `/users/${id}`,
    method: 'PUT',
    data
  })
}

// 删除用户
export const deleteUser = (id) => {
  return request({
    url: `/users/${id}`,
    method: 'DELETE'
  })
}
