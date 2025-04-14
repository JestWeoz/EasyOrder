<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="isOpen" class="modal-overlay" @click="closeModal">
        <div class="modal-container" @click.stop>
          <div class="modal-header">
            <h5 class="modal-title">Gọi nhân viên</h5>
          </div>
          <div class="modal-body">
            <textarea
              class="form-control"
              v-model="staffMessage"
              placeholder="Nhập yêu cầu của quý khách"
              style="font-size: 15px"
              rows="4"
              ref="textareaRef"
              @keydown.esc="closeModal"
            ></textarea>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn-primary" @click="sendStaffRequest">Gửi yêu cầu</button>
            <button type="button" class="btn-secondary" @click="closeModal">Đóng</button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue'
import { connectWebSocket, sendMessage, disconnectWebSocket } from '@/utils/websocket'

const isOpen = ref(false)
const staffMessage = ref('')
const textareaRef = ref(null)

onUnmounted(() => {
  disconnectWebSocket()
})

const showModal = () => {
  connectWebSocket(
    () => {
      console.log('WebSocket connected successfully')
      isOpen.value = true
      nextTick(() => {
        textareaRef.value?.focus()
      })
    },
    (error) => {
      console.error('WebSocket connection error:', error)
      alert('Không thể kết nối với máy chủ. Vui lòng thử lại sau.')
    }
  )
}

const closeModal = () => {
  isOpen.value = false
  disconnectWebSocket()
}

const sendStaffRequest = () => {
  if (staffMessage.value.trim()) {
    const message = {
      tableId: '37', // Thay thế bằng ID bàn thực tế
      message: staffMessage.value,
      type: 'STAFF_CALL',
    }

    if (sendMessage('/app/call-staff', message)) {
      closeModal()
      staffMessage.value = ''
    } else {
      alert('Không thể gửi yêu cầu. Vui lòng thử lại sau.')
    }
  } else {
    alert('Vui lòng nhập nội dung yêu cầu')
  }
}

defineExpose({
  showModal,
  closeModal,
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

.form-control {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: none;
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