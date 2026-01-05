import {
  createRouter,
  createWebHashHistory
} from 'vue-router';
import {
  getToken
} from '../utils/auth';
import Index from '../pages/index.vue'
import About from '../pages/About.vue'
import NotFound from '../pages/404.vue'
import Login from '../pages/login.vue'
import Admin from '../layouts/admin.vue';
import Student from '../pages/student/List.vue'
import Course from '../pages/course/List.vue'
import StudentInput from '../pages/student/input.vue'
import CourseInput from '../pages/course/input.vue'
import DepartmentList from '../pages/department/List.vue'

const routes = [{
  path: "/",
  component: Admin,
  children: [{
    path: "",
    redirect: "/index"
  },
  {
    path: "/student/list",
    component: Student,
    meta: {
      title: '学生列表'
    }
  },
  {
    path: "/student/input",
    component: StudentInput,
    meta: {
      title: '新生录入'
    }
  },
  {
    path: "/course/list",
    component: Course,
    meta: {
      title: '课程列表'
    }
  },
  {
    path: "/course/input",
    component: CourseInput,
    meta: {
      title: '课程添加'
    }
  },
  {
    path: '/department/list',
    name: 'DepartmentList',
    component: DepartmentList,
    meta: {
      title: '院系列表'
    }
  },
  {
    path: "/index",
    name: "Index",
    component: Index,
    meta: {
      title: '首页'
    }
  },

  {
    path: "/about",
    name: "About",
    component: About,
    meta: {
      title: '关于'
    }
  }
  ]
},

{
  path: "/login",
  name: "Login",
  component: Login,
  meta: {
    title: '登录'
  }
},
{
  path: "/:pathMatch(.*)*",
  name: "NotFound",
  component: NotFound,
  meta: {
    title: '页面不存在'
  }
}
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const whiteList = ['/login'];
  const hasToken = getToken(); 

  if (hasToken) {
    if (to.path === '/login') {
      next('/'); 
    } else {
      next(); 
    }
  }
  else {
    if (whiteList.includes(to.path)) {
      next(); 
    } else {
      next('/login'); 
    }
  }
});
export default router