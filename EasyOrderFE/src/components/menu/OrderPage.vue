<template>
  <div class="sidebar-overlay" v-if="isOpen" @click="close"></div>
  <div class="sidebar" :class="{ open: isOpen }">
    <div class="sidebar-header">
      <button class="btn p-0 me-2 fs-5 refresh-btn" @click="refresh" title="Làm mới">
        <i class="bi bi-arrow-clockwise"></i>
      </button>
      <h5 class="mb-0 flex-grow-1 text-center fw-bold" style="flex: 1">Đơn hàng</h5>
      <button class="btn p-0 fs-5 close-btn" @click="close" title="Đóng">
        <i class="bi bi-x-lg"></i>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrderPage',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {}
  },
  computed: {},
  methods: {
    close() {
      this.$emit('update:isOpen', false)
    },
    formatPrice(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
    },
    goHome() {
      this.$emit('go-home')
      this.close()
    },
    
    refresh() {
      this.$emit('refresh-order')
    },
    deleteOrder() {
      this.$emit('delete-order')
    },
  },
  emits: ['update:isOpen', 'go-home', 'delete-order', 'refresh-order'],
}
</script>

<style scoped>
.sidebar-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 3000;
}

.sidebar {
  position: fixed;
  top: 0;
  right: -100%;
  width: 100%;
  height: 100%;
  background-color: white;
  z-index: 3001;
  transition: right 0.5s ease;
  display: flex;
  flex-direction: column;
}

.sidebar.open {
  right: 0;
}
.close-btn {
  border: none;
  background: none;
}
.refresh-btn {
  border: none;
  background: none;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.sidebar-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
}
</style> 