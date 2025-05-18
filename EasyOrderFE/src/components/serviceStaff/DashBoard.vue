<template>
  <div class="dashboard">
    <div class="row">
      <!-- Thông báo gọi nhân viên -->
      <div class="col-md-6">
        <div class="card">
          <div class="card-header">
            <h5 class="card-title">Thông báo gọi nhân viên</h5>
            <button v-if="messages.length > 0" class="clear-button" @click="clearMessages">
              Xóa tin nhắn
            </button>
          </div>
          <div class="card-body">
            <div v-if="messages.length === 0" class="no-messages">
              <p>Chưa có thông báo nào</p>
            </div>
            <div v-else class="messages-container">
              <div
                v-for="message in messages"
                :key="message.id"
                :class="[
                  'message-item',
                  { 'checkout-message': message.type === 'CHECKOUT_REQUEST' },
                ]"
              >
                <div class="message-header">
                  <span class="badge bg-primary">{{ message.tableId }}</span>
                  <span class="time">{{ formatTime(message.timestamp) }}</span>
                </div>
                <div class="message-content">
                  {{ message.content }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Danh sách bàn -->
      <div class="col-md-6">
        <div class="card">
          <div class="card-header">
            <h5 class="card-title">Danh sách bàn</h5>
          </div>
          <div class="card-body">
            <div class="table-grid">
              <div
                v-for="table in tables"
                :key="table.id"
                :class="['table-item', { occupied: table.status === 'occupied' }]"
                @click="handleTableClick(table)"
              >
                <div class="table-number">{{ table.name }}</div>
                <div class="table-status">{{ getStatusText(table.status) }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal hiển thị thông tin bàn -->
    <div v-if="showTableModal" class="modal-backdrop">
      <div class="modal-content">
        <h5>Thông tin bàn</h5>
        <p><strong>Tên bàn:</strong> Bàn {{ selectedTable.number }}</p>
        <p><strong>Mô tả:</strong> {{ selectedTable.description }}</p>
        <p><strong>Sức chứa:</strong> {{ selectedTable.capacity }}</p>
        <p>
          <strong>Trạng thái:</strong>
          <select v-model="selectedTable.status">
            <option value="empty">Trống</option>
            <option value="occupied">Đang sử dụng</option>
          </select>
        </p>
        <div class="modal-actions">
          <button @click="updateTableStatus">Lưu</button>
          <button @click="showTableModal = false">Đóng</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { connectWebSocket, subscribe, disconnectWebSocket } from '@/utils/websocket'
import axios from 'axios'

export default {
  name: 'DashBoard',
  data() {
    return {
      messages: [],
      subscription: null,
      tables: [],
      showTableModal: false,
      selectedTable: null,
    }
  },
  methods: {
    formatTime(timestamp) {
      return new Date(timestamp).toLocaleString('vi-VN')
    },
    handleNewMessage(response) {
      const message = response.result
      console.log(message)
      const newMessage = {
        id: Date.now(),
        tableId: message.tableId,
        content: message.message,
        type: message.type,
        timestamp: new Date().toISOString(),
      }
      this.messages.unshift(newMessage)
      this.saveMessagesToLocalStorage()
    },
    getStatusText(status) {
      return status === 'empty' ? 'Trống' : 'Đang sử dụng'
    },
    handleTableClick(table) {
      this.selectedTable = { ...table }
      this.showTableModal = true
    },
    async getTables() {
      try {
        const response = await axios.get('http://localhost:8081/table/getAll')
        this.tables = response.data.result.map((table) => ({
          id: table.id,
          name: table.name,
          description: table.description,
          capacity: table.capacity,
          status: table.status === 1 ? 'empty' : 'occupied',
        }))
      } catch (error) {
        console.error('Lỗi khi lấy danh sách bàn:', error)
      }
    },
    async updateTableStatus() {
      try {
        const formData = new FormData()
        formData.append('id', this.selectedTable.id)
        formData.append('status', this.selectedTable.status === 'empty' ? 1 : 0)
        formData.append('name', this.selectedTable.name)
        formData.append('description', this.selectedTable.description)
        formData.append('capacity', this.selectedTable.capacity)
        formData.append('url', this.selectedTable.url)

        await axios.put(`http://localhost:8081/table`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        await this.getTables()
        this.showTableModal = false
      } catch (error) {
        alert('Cập nhật trạng thái bàn thất bại!')
      }
    },
    saveMessagesToLocalStorage() {
      localStorage.setItem('staff_messages', JSON.stringify(this.messages))
    },
    loadMessagesFromLocalStorage() {
      const savedMessages = localStorage.getItem('staff_messages')
      if (savedMessages) {
        this.messages = JSON.parse(savedMessages)
      }
    },
    clearMessages() {
      this.messages = []
      this.saveMessagesToLocalStorage()
    },
  },
  async created() {
    try {
      await this.getTables()
      this.loadMessagesFromLocalStorage()

      connectWebSocket(
        () => {
          console.log('WebSocket connected successfully')
          this.subscription = subscribe('/topic/staff', this.handleNewMessage)
          console.log('Subscription:', this.subscription)
        },
        (error) => {
          console.error('WebSocket connection error:', error)
        }
      )
    } catch (error) {
      console.error('Lỗi khi khởi tạo:', error)
    }
  },
  beforeUnmount() {
    if (this.subscription) {
      this.subscription.unsubscribe()
    }
    disconnectWebSocket()
  },
}
</script>

<style scoped>
.dashboard {
  width: 100%;
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.row {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -10px;
}

.col-md-6 {
  flex: 0 0 50%;
  max-width: 50%;
  padding: 0 10px;
}

@media (max-width: 768px) {
  .col-md-6 {
    flex: 0 0 100%;
    max-width: 100%;
  }
}

.card {
  margin-bottom: 25px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border-radius: 12px;
  background-color: #fff;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  overflow: hidden;
  border: none;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.card-header {
  background-color: #4a90e2;
  border-bottom: none;
  padding: 18px 25px;
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  margin: 0;
  font-size: 1.25rem;
  color: white;
  font-weight: 600;
  letter-spacing: 0.3px;
}

.card-body {
  padding: 20px 25px;
}

.messages-container {
  max-height: 450px;
  overflow-y: auto;
  padding-right: 8px;
}

.messages-container::-webkit-scrollbar {
  width: 6px;
}

.messages-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.messages-container::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 10px;
}

.messages-container::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}

.message-item {
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 15px;
  background-color: #f8faff;
  border-left: 4px solid #4a90e2;
  transition: transform 0.2s ease;
}

.message-item.checkout-message {
  border-left: 4px solid #f44336;
  background-color: #fff8f8;
}

.message-item:hover {
  transform: translateX(5px);
}

.message-item:last-child {
  margin-bottom: 0;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.badge {
  background-color: #4a90e2;
  color: white;
  padding: 5px 10px;
  border-radius: 20px;
  font-weight: 500;
  font-size: 0.9rem;
}

.time {
  color: #75808f;
  font-size: 0.85em;
  font-weight: 500;
}

.message-content {
  color: #333;
  line-height: 1.6;
  font-size: 0.95rem;
}

.table-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 15px;
  padding: 5px;
}

.table-item {
  background-color: #fff;
  border: 2px solid #e6eaf0;
  border-radius: 12px;
  padding: 18px 15px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.table-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background-color: #4caf50;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.table-item.occupied::before {
  background-color: #f44336;
  opacity: 1;
}

.table-item:not(.occupied)::before {
  background-color: #4caf50;
  opacity: 1;
}

.table-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  border-color: #d0d8e2;
}

.table-item.occupied {
  background-color: #fff8f8;
  border-color: #ffcdd2;
}

.table-number {
  font-weight: 700;
  font-size: 1.2rem;
  margin-bottom: 8px;
  color: #2c3e50;
}

.table-status {
  font-size: 0.9rem;
  color: #4caf50;
  font-weight: 500;
  padding: 4px 12px;
  border-radius: 20px;
  display: inline-block;
  background-color: rgba(76, 175, 80, 0.1);
}

.table-item.occupied .table-status {
  color: #f44336;
  background-color: rgba(244, 67, 54, 0.1);
}

.no-messages {
  text-align: center;
  color: #8795a1;
  padding: 40px 20px;
  background-color: #f8faff;
  border-radius: 10px;
  border: 1px dashed #d8e2f1;
}

.no-messages p {
  margin: 0;
  font-size: 1rem;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(15, 23, 42, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
  backdrop-filter: blur(4px);
}

.modal-content {
  background: #fff;
  padding: 30px;
  border-radius: 16px;
  width: 450px;
  max-width: 90%;
  max-height: 90vh;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.18);
  animation: slideIn 0.35s ease;
  position: relative;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.modal-content h5 {
  margin: 0 0 25px 0;
  color: #1e293b;
  font-size: 1.4rem;
  font-weight: 700;
  text-align: center;
  position: relative;
  padding-bottom: 15px;
}

.modal-content h5::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background-color: #4a90e2;
  border-radius: 3px;
}

.modal-content p {
  margin: 12px 0;
  color: #4b5563;
  font-size: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-content strong {
  color: #1e293b;
  min-width: 100px;
}

.modal-content select {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  margin-top: 5px;
  font-size: 1rem;
  color: #1e293b;
  background-color: #f8fafc;
  transition: all 0.3s ease;
  cursor: pointer;
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='%234a5568' viewBox='0 0 16 16'%3E%3Cpath d='M8 11.5a.5.5 0 0 1-.354-.146l-4-4a.5.5 0 0 1 .708-.708L8 10.293l3.646-3.647a.5.5 0 0 1 .708.708l-4 4A.5.5 0 0 1 8 11.5z'/%3E%3C/svg%3E");
  background-position: right 15px center;
  background-repeat: no-repeat;
  padding-right: 40px;
}

.modal-content select:focus {
  outline: none;
  border-color: #4a90e2;
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.15);
}

.modal-actions {
  margin-top: 30px;
  display: flex;
  gap: 15px;
  justify-content: center;
}

.modal-actions button {
  padding: 10px 24px;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 120px;
}

.modal-actions button:first-child {
  background-color: #4a90e2;
  color: white;
}

.modal-actions button:first-child:hover {
  background-color: #3578c5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.25);
}

.modal-actions button:last-child {
  background-color: #f1f5f9;
  color: #475569;
}

.modal-actions button:last-child:hover {
  background-color: #e2e8f0;
  transform: translateY(-2px);
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateY(-30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@media (max-width: 768px) {
  .dashboard {
    padding: 15px;
  }

  .table-grid {
    grid-template-columns: repeat(auto-fill, minmax(110px, 1fr));
    gap: 10px;
  }

  .table-item {
    padding: 15px 10px;
  }

  .modal-content {
    padding: 20px;
    width: 95%;
  }
}

.clear-button {
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 5px 10px;
  font-size: 0.8rem;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.clear-button:hover {
  background-color: #d32f2f;
  transform: translateY(-2px);
}
</style> 