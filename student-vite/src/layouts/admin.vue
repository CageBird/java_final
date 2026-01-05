<template>
    <div class="common-layout">
     <el-container>
      <el-header>
        <fheader/>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <faside/>
        </el-aside>
        <el-main>
          <ftag-nav/>
          <router-view></router-view>
        </el-main>
      </el-container>
     </el-container>
    </div>
</template>

<script setup>
import Fheader from './components/Fheader.vue'
import Faside from './components/Faside.vue'
import FtagNav from './components/FtagNav.vue'


</script>

<style>

</style>