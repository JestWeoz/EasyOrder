<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <router-view></router-view>
</template>

<style>
/* Global styles */
body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell,
    'Open Sans', 'Helvetica Neue', sans-serif;
}

/* Hide scrollbar but keep functionality */
::-webkit-scrollbar {
  display: none;
}
</style>