import Vue from 'vue'
import Router from 'vue-router'
import HomePage from '@/components/HomePage'
import Login from '@/components/common/Login'
import UserLoginDetail from '@/components/common/UserLoginDetail'
import AdminPage from '@/components/admin/AdminPage'
import BillPage from '@/components/bill/BillPage'
import CustomerPage from '@/components/customer/CustomerPage'
import EmployeePage from '@/components/employee/EmployeePage'
import ReportPage from '@/components/report/ReportPage'
import TreeTypePage from '@/components/tree/TreeTypePage'
import TreePage from '@/components/tree/TreePage'
import RolePage from '@/components/role/RolePage'
import Dashboard from '@/components/dashboard/Dashboard'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/dashboard',
      name: 'HomePage',
      component: HomePage,
      children:[
        {
          path: '/admin',
          name: 'Admin',
          components: {
            Admin: AdminPage
          },
        },
        {
          path: '/customer',
          name: 'Customer',
          components: {
            Customer:CustomerPage
          },
        },
        {
          path: '/employee',
          name: 'Employee',
          components: {
            Employee:EmployeePage
          },
        },
        {
          path: '/role',
          name: 'Role',
          components: {
            Role:RolePage
          },
        },
        {
          path: '/treeType',
          name: 'TreeType',
          components: {
            TreeType: TreeTypePage
          },
        },
        {
          path: '/tree',
          name: 'Tree',
          components: {
            Tree:TreePage
          },
        },
        {
          path: '/bill',
          name: 'Bill',
          components: {
            Bill: BillPage
          },
        },
        {
          path: '/report',
          name: 'Report',
          components: {
            Report:ReportPage
          },
        },
        {
          path: '/dashboard',
          name: 'Dashboard',
          components: {
            Dashboard:Dashboard
          },
        },
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
    },
    {
      path: '/userDetail',
      name: 'UserLoginDetail',
      component: UserLoginDetail,
    },
  ]
})
