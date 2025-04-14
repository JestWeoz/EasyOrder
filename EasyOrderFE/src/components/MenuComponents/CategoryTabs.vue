<template>
  <ul class="nav nav-tabs mt-3 px-2">
    <li v-for="tab in categories" :key="tab.id" class="nav-item">
      <a
        class="nav-link"
        :class="{ active: activeTab === tab.id }"
        href="#"
        @click.prevent="selectTab(tab.id)"
      >
        {{ tab.name }}
      </a>
    </li>
  </ul>
</template>
  
<script>
export default {
  name: 'CategoryTabs',
  props: {
    activeTab: {
      type: [String, Number],
      required: true,
    },
    categories: {
      type: Array,
      required: true,
      default: () => [],
    },
  },
  emits: ['update:tab'],
  methods: {
    selectTab(tabId) {
      this.$emit('update:tab', tabId)
    },
  },
}
</script>

<style scoped>
.nav-tabs {
  overflow-x: auto;
  overflow-y: hidden;
  flex-wrap: nowrap;
  border-bottom: none;
  font-weight: bold;
  font-size: 13px;
}

.nav-tabs .nav-link {
  white-space: nowrap;
  border: none;
  color: #495057;
  margin-right: 8px;
  background-color: #e7ebf0;
  border-radius: 10px;
  cursor: pointer;
}

.nav-tabs .nav-link.active {
  background-color: #1877f2;
  color: white;
  border-radius: 10px;
  transition: background-color 0.3s ease, color 0.3s ease;
  font-weight: bold;
}
</style>