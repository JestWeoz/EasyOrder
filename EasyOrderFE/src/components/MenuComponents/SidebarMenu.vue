<template>
  <div class="sidebar-overlay" v-if="isOpen" @click="close"></div>
  <div class="sidebar" :class="{ open: isOpen }">
    <div class="sidebar-header">
      <h5 class="mb-0">Easy Order xin chào quý khách</h5>
      <button class="btn p-0" @click="close">
        <i class="bi bi-x-lg"></i>
      </button>
    </div>
    <div class="sidebar-content">
      <div class="contact-info">
        <h1 class="text-center">Liên hệ</h1>
        <p class="mb-0">
          <i class="bi bi-telephone me-2"></i>
          0961891998
        </p>
        <p class="mb-0">
          <i class="bi bi-envelope me-2"></i>
          kebincoffeeinbed@gmail.com
        </p>
        <p class="mb-2">
          <i class="bi bi-geo-alt me-2"></i>
          06/106 Lê Thanh Nghị, P.Bách Khoa, Hai Bà Trưng, Hà Nội
        </p>
      </div>
      <div class="action-buttons">
        <button class="btn btn-primary w-100 mb-2" @click="callStaff">
          <i class="bi bi-person me-2"></i>
          Gọi nhân viên
        </button>
        <button class="btn btn-secondary w-100" @click="requestCheckout">
          <i class="bi bi-cash-coin me-2"></i>
          Gọi thanh toán
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SidebarMenu',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['update:isOpen', 'call-staff', 'request-checkout'],
  methods: {
    close() {
      this.$emit('update:isOpen', false)
    },
    callStaff() {
      this.$emit('call-staff')
      this.close()
    },
    requestCheckout() {
      this.$emit('request-checkout')
      this.close()
    },
  },
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
  z-index: 2000;
}

.sidebar {
  position: fixed;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background-color: white;
  z-index: 2001;
  transition: left 0.6s ease;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.sidebar.open {
  left: 0;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.sidebar-header button {
  border: 1px #ddd;
  padding: 4px 8px;
}

.sidebar-content {
  padding: 16px;
}

.contact-info {
  margin-bottom: 24px;
}

.contact-info p {
  font-size: 14px;
  color: #333;
}

.action-buttons {
  padding: 16px 0;
}

.staff-button {
  background-color: #e6f4ff;
  color: #1877f2;
  border: 1px solid #1877f2;
  font-weight: bold;
}

.checkout-button {
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
  font-weight: bold;
}

.bi {
  font-size: 18px;
}
</style> 