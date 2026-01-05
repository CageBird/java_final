<!-- <template>
  <div class="f-tag-nav">
    <el-tabs 
      v-model="editableTabsValue" 
      type="card" 
      class="demo-tabs" 
      editable 
      @edit="handleTabsEdit"
      @tab-change="changeTab"
      @tab-remove="removeTab"
    >
      <template #add-icon>
        <el-icon><Select /></el-icon>
      </template>
      <el-tab-pane 
        v-for="item in editableTabs" 
        :key="item.path" 
        :label="item.title" 
        :name="item.path"
      >
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Select } from '@element-plus/icons-vue'
import { onBeforeRouteUpdate, useRoute } from 'vue-router'
import { useCookies } from '@vueuse/integrations/useCookies'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const cookie = useCookies()

// 活跃标签值
const editableTabsValue = ref(route.path)
// 标签列表
const editableTabs = ref([])

// 初始化标签列表
function initTabList() {
  const tablist = cookie.get("editableTabs")
  if (tablist && Array.isArray(tablist)) {
    editableTabs.value = tablist
    // 检查当前路由是否在标签列表中，不在则添加
    const hasCurrent = editableTabs.value.some(tab => tab.path === route.path)
    if (!hasCurrent && route.meta.title) {
      addTab({
        title: route.meta.title,
        path: route.path
      })
    } else if (hasCurrent) {
      editableTabsValue.value = route.path
    }
  } else {
    // 初始默认标签
    const defaultTabs = [
      { title: '首页', path: "/index" }
    ]
    editableTabs.value = defaultTabs
    // 设置当前激活的标签
    editableTabsValue.value = route.path || defaultTabs[0].path
    cookie.set("editableTabs", editableTabs.value)
  }
}

// 添加标签
function addTab(tab) {
  if (!tab.path || !tab.title) return
  
  const noTab = editableTabs.value.findIndex(t => t.path === tab.path) === -1
  if (noTab) {
    editableTabs.value.push({
      title: tab.title,
      path: tab.path
    })
    ElMessage.success(`已打开 ${tab.title}`)
  }
  // 更新cookie存储
  cookie.set("editableTabs", editableTabs.value)
}

// 路由更新前添加标签
onBeforeRouteUpdate((to, from) => {
  if (to.meta.title) {
    editableTabsValue.value = to.path
    addTab({
      title: to.meta.title,
      path: to.path
    })
  }
})

// 标签切换
const changeTab = (path) => {
  if (path !== route.path) {
    router.push(path)
  }
}

// 移除标签
const removeTab = (path) => {
  const tabs = editableTabs.value
  let currentValue = editableTabsValue.value
  
  // 如果删除的是当前激活的标签
  if (currentValue === path) {
    tabs.forEach((tab, index) => {
      if (tab.path === path) {
        // 找到下一个或上一个标签作为新的激活标签
        const nextTab = tabs[index + 1] || tabs[index - 1]
        if (nextTab) {
          currentValue = nextTab.path
          router.push(currentValue)
        }
      }
    })
  }
  
  // 更新标签列表
  editableTabs.value = tabs.filter(tab => tab.path !== path)
  editableTabsValue.value = currentValue
  // 更新cookie
  cookie.set("editableTabs", editableTabs.value)
  
  // 如果标签列表为空，跳转到默认页面
  if (editableTabs.value.length === 0) {
    router.push('/student/list')
  }
}

// 处理标签编辑（添加）
const handleTabsEdit = (targetName, action) => {
  if (action === 'add') {
    ElMessage.info('请从左侧菜单选择要添加的页面')
  }
}

// 监听标签变化，同步到cookie
watch(editableTabs, (newVal) => {
  cookie.set("editableTabs", newVal)
}, { deep: true })

// 初始化
initTabList()
</script>

<style>
.f-tag-nav {
  @apply fixed bg-gray-200 flex items-center border-b border-gray-300;
  top: 64px;
  right: 0;
  left: 250px;
  z-index: 100;
  height: 48px;
  padding: 0 16px;
}

.demo-tabs {
  width: 100%;
}

/* 调整标签样式 */
:deep(.el-tabs__nav) {
  height: 100%;
}

:deep(.el-tabs__item) {
  height: 48px;
  line-height: 48px;
  padding: 0 16px;
}

:deep(.el-tabs__add) {
  height: 48px;
  line-height: 48px;
  margin: 0 8px;
}
</style> -->

<template>
  <div class="f-tag-nav">
    <el-tabs 
      v-model="editableTabsValue" 
      type="card" 
      class="demo-tabs" 
      editable 
      @edit="handleTabsEdit"
      @tab-change="changeTab"
      @tab-remove="removeTab"
    >
      <template #add-icon>
        <el-icon><Select /></el-icon>
      </template>
      <el-tab-pane 
        v-for="item in editableTabs" 
        :key="item.path" 
        :label="item.title" 
        :name="item.path"
      >
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Select } from '@element-plus/icons-vue'
import { onBeforeRouteUpdate, useRoute } from 'vue-router'
import { useCookies } from '@vueuse/integrations/useCookies'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const cookie = useCookies()

// 活跃标签值
const editableTabsValue = ref(route.path)
// 标签列表
const editableTabs = ref([])

// 初始化标签列表（核心修改：强制初始仅保留首页）
function initTabList() {
  // 强制重置为仅首页，忽略cookie中旧的多标签数据
  const defaultTabs = [{ title: '首页', path: "/index" }]
  editableTabs.value = defaultTabs
  cookie.set("editableTabs", editableTabs.value)
  
  // 初始激活首页标签
  editableTabsValue.value = "/index"
  
  // 如果当前路由不是首页，自动跳转到首页（保证初始只看首页）
  if (route.path !== "/index") {
    router.push("/index")
  }
}

// 添加标签（保留原有逻辑，后续操作可正常添加）
function addTab(tab) {
  if (!tab.path || !tab.title) return
  
  const noTab = editableTabs.value.findIndex(t => t.path === tab.path) === -1
  if (noTab) {
    editableTabs.value.push({
      title: tab.title,
      path: tab.path
    })
    ElMessage.success(`已打开 ${tab.title}`)
  }
  // 更新cookie存储
  cookie.set("editableTabs", editableTabs.value)
}

// 路由更新前添加标签（保留：后续点击菜单会自动加标签）
onBeforeRouteUpdate((to, from) => {
  if (to.meta.title) {
    editableTabsValue.value = to.path
    addTab({
      title: to.meta.title,
      path: to.path
    })
  }
})

// 标签切换（保留原有逻辑）
const changeTab = (path) => {
  if (path !== route.path) {
    router.push(path)
  }
}

// 移除标签（保留原有逻辑，修复空标签时跳首页）
const removeTab = (path) => {
  const tabs = editableTabs.value
  let currentValue = editableTabsValue.value
  
  // 如果删除的是当前激活的标签
  if (currentValue === path) {
    tabs.forEach((tab, index) => {
      if (tab.path === path) {
        // 找到下一个或上一个标签作为新的激活标签
        const nextTab = tabs[index + 1] || tabs[index - 1]
        if (nextTab) {
          currentValue = nextTab.path
          router.push(currentValue)
        }
      }
    })
  }
  
  // 更新标签列表
  editableTabs.value = tabs.filter(tab => tab.path !== path)
  editableTabsValue.value = currentValue
  // 更新cookie
  cookie.set("editableTabs", editableTabs.value)
  
  // 如果标签列表为空，跳转到首页（替代原来的学生列表）
  if (editableTabs.value.length === 0) {
    router.push('/index')
    // 重新添加首页标签
    editableTabs.value = [{ title: '首页', path: "/index" }]
    cookie.set("editableTabs", editableTabs.value)
  }
}

// 处理标签编辑（添加）
const handleTabsEdit = (targetName, action) => {
  if (action === 'add') {
    ElMessage.info('请从左侧菜单选择要添加的页面')
  }
}

// 监听标签变化，同步到cookie
watch(editableTabs, (newVal) => {
  cookie.set("editableTabs", newVal)
}, { deep: true })

// 初始化
initTabList()
</script>

<style>
.f-tag-nav {
  @apply fixed bg-gray-200 flex items-center border-b border-gray-300;
  top: 64px;
  right: 0;
  left: 250px;
  z-index: 100;
  height: 48px;
  padding: 0 16px;
}

.demo-tabs {
  width: 100%;
}

/* 调整标签样式 */
:deep(.el-tabs__nav) {
  height: 100%;
}

:deep(.el-tabs__item) {
  height: 48px;
  line-height: 48px;
  padding: 0 16px;
}

:deep(.el-tabs__add) {
  height: 48px;
  line-height: 48px;
  margin: 0 8px;
}
</style>