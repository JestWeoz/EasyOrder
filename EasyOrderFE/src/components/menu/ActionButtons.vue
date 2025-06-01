<template>
  <div class="row custom-margin-top px-2">
    <div class="col-6 pe-1">
      <button class="btn btn-outline-primary w-100 fw-bold" @click="showStaffModal">
        <i class="bi bi-check-circle me-1"></i>
        Gọi nhân viên
      </button>
    </div>
    <div class="col-6 ps-1">
      <button class="btn btn-outline-secondary w-100 fw-bold" @click="showCheckoutModal">
        <i class="bi bi-cash-coin me-1"></i>
        Gọi thanh toán
      </button>
    </div>

    <StaffCallModal ref="staffModalRef" @send-staff-request="handleStaffRequest" />
    <CheckoutModal ref="checkoutModalRef" @send-checkout-request="handleCheckoutRequest" />
  </div>
</template>

<script>
import { ref } from 'vue'
import StaffCallModal from './StaffCallModal.vue'
import CheckoutModal from './CheckoutModal.vue'

export default {
  name: 'ActionButtons',
  components: {
    StaffCallModal,
    CheckoutModal,
  },
  props: {
    tableId: {
      type: [String, Number],
      required: true,
    },
    tableInfo: {
      type: Object,
      default: () => ({}),
    },
  },
  emits: ['send-staff-request', 'send-checkout-request'],
  setup(props, { emit }) {
    const staffModalRef = ref(null)
    const checkoutModalRef = ref(null)

    const showStaffModal = () => {
      if (staffModalRef.value) {
        staffModalRef.value.tableId = props.tableId
        staffModalRef.value.tableInfo = props.tableInfo
        staffModalRef.value.showModal()
      }
    }

    const showCheckoutModal = () => {
      if (checkoutModalRef.value) {
        checkoutModalRef.value.tableId = props.tableId
        checkoutModalRef.value.tableInfo = props.tableInfo
        checkoutModalRef.value.showModal()
      }
    }

    const handleStaffRequest = (message) => {
      emit('send-staff-request', message)
    }

    const handleCheckoutRequest = () => {
      emit('send-checkout-request')
    }

    return {
      staffModalRef,
      checkoutModalRef,
      showStaffModal,
      showCheckoutModal,
      handleStaffRequest,
      handleCheckoutRequest,
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
