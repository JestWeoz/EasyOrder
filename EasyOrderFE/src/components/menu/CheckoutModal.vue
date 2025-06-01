<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="isOpen" class="modal-overlay" @click="closeModal">
        <div class="modal-container" @click.stop>
          <div class="modal-header">
            <h5 class="modal-title">Xác nhận thanh toán</h5>
          </div>
          <div class="modal-body">
            <p class="confirmation-text">Bạn có chắc muốn gọi thanh toán không?</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn-primary" @click="confirmCheckout">Xác nhận</button>
            <button type="button" class="btn-secondary" @click="closeModal">Hủy</button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref } from 'vue'

const isOpen = ref(false)
const tableId = ref(null)
const tableInfo = ref({})

const showModal = () => {
  isOpen.value = true
}

const closeModal = () => {
  isOpen.value = false
}

const confirmCheckout = () => {
  emit('send-checkout-request')
  closeModal()
}

const emit = defineEmits(['send-checkout-request'])

defineExpose({
  showModal,
  closeModal,
  tableId,
  tableInfo,
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background: white;
  width: 90%;
  max-width: 332px;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
  transition: all 0.3s ease;
}

.confirmation-text {
  text-align: center;
  font-size: 16px;
  margin: 10px 0;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.5s ease;
}

.modal-enter-to,
.modal-leave-from {
  opacity: 1;
}

.modal-header {
  text-align: center;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.modal-title {
  font-weight: bold;
  margin: 0;
  font-size: 1.25rem;
  text-align: center;
  width: 100%;
}

.modal-body {
  margin-bottom: 20px;
}

.modal-footer {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.btn-primary,
.btn-secondary {
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn-primary {
  background-color: #f38e4a;
  color: white;
}

.btn-secondary {
  background-color: #0d6efd;
  color: white;
}

.btn-primary:hover,
.btn-secondary:hover {
  opacity: 0.9;
}
</style> 