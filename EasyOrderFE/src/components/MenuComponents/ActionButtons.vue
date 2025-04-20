<template>
  <div class="row custom-margin-top px-2">
    <div class="col-6 pe-1">
      <button class="btn btn-outline-primary w-100 fw-bold" @click="showStaffModal">
        <i class="bi bi-check-circle me-1"></i>
        Gọi nhân viên
      </button>
    </div>
    <div class="col-6 ps-1">
      <button class="btn btn-outline-secondary w-100 fw-bold" @click="requestCheckout">
        <i class="bi bi-cash-coin me-1"></i>
        Gọi thanh toán
      </button>
    </div>

    <StaffCallModal ref="staffModalRef" />
  </div>
</template>

<script>
import { ref } from 'vue'
import { sendMessage } from '@/utils/websocket'
import StaffCallModal from './StaffCallModal.vue'

export default {
  name: 'ActionButtons',
  components: {
    StaffCallModal,
  },
  setup() {
    const staffModalRef = ref(null)

    const showStaffModal = () => {
      staffModalRef.value.showModal()
    }

    const requestCheckout = () => {
      const message = {
        tableId: '37', // Thay thế bằng ID bàn thực tế
        type: 'CHECKOUT_REQUEST',
      }

      if (sendMessage('/app/checkout', message)) {
        alert('Đã gửi yêu cầu thanh toán')
      } else {
        alert('Không thể gửi yêu cầu thanh toán. Vui lòng thử lại sau.')
      }
    }

    return {
      staffModalRef,
      showStaffModal,
      requestCheckout,
    }
  },
}
</script>

<style scoped>
.custom-margin-top {
  margin-top: 1rem !important;
}

.btn-outline-primary {
  background-color: #ffffff !important;
  border-color: #0d6efd !important;
  color: #0d6efd !important;
}

.btn-outline-primary:active {
  background-color: #d7ebff !important;
}

.confirm-button {
  background-color: #e6f4ff;
  color: #1877f2;
  border: 1px solid #066bf0ff;
  font-weight: bold;
  font-size: 12px;
}

.checkout-button {
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
  font-weight: bold;
  font-size: 12px;
}
</style>
