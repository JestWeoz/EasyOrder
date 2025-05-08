<template>
  <div class="content-wrapper">
    <div class="row">
      <!-- Thông báo gọi nhân viên -->
      <div class="col-md-6">
        <div class="card">
          <div class="card-header">
            <h5 class="card-title">Thông báo gọi nhân viên</h5>
          </div>
          <div class="card-body">
            <div v-if="messages.length === 0" class="no-messages">
              <p>Chưa có thông báo nào</p>
            </div>
            <div v-else class="messages-container">
              <div v-for="message in messages" :key="message.id" class="message-item">
                <div class="message-header">
                  <span class="badge bg-primary">Bàn {{ message.tableId }}</span>
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
  name: 'ServiceStaffDashboard',
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
      this.messages.unshift({
        id: Date.now(),
        tableId: message.tableId,
        content: message.message,
        timestamp: new Date().toISOString(),
      })
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
        const response = await axios.get('http://localhost:8081/table')
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
        console.log(formData.get('status'))
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
  },
  async created() {
    try {
      await this.getTables()
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
.content-wrapper {
  padding: 20px;
}

.card {
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
  padding: 15px 20px;
}

.card-title {
  margin: 0;
  font-size: 1.2rem;
  color: #333;
}

.messages-container {
  max-height: 400px;
  overflow-y: auto;
}

.message-item {
  border-bottom: 1px solid #eee;
  padding: 15px 0;
}

.message-item:last-child {
  border-bottom: none;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.time {
  color: #666;
  font-size: 0.9em;
}

.message-content {
  color: #444;
  line-height: 1.5;
}

.table-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 15px;
  padding: 10px;
}

.table-item {
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.table-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.table-item.occupied {
  background-color: #ffebee;
  border-color: #ffcdd2;
}

.table-number {
  font-weight: bold;
  font-size: 1.1rem;
  margin-bottom: 5px;
}

.table-status {
  font-size: 0.9rem;
  color: #666;
}

.no-messages {
  text-align: center;
  color: #666;
  padding: 20px;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.modal-content {
  background: #fff;
  padding: 25px;
  border-radius: 8px;
  width: 400px;
  height: 400px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s ease;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.modal-content h5 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 1.2rem;
  font-weight: 600;
  text-align: center;
}

.modal-content p {
  margin: 8px 0;
  color: #555;
  font-size: 0.95rem;
}

.modal-content select {
  width: 100%;
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-top: 5px;
  font-size: 0.95rem;
  color: #333;
  background-color: #f8f9fa;
  transition: border-color 0.3s ease;
}

.modal-content select:focus {
  outline: none;
  border-color: #4a90e2;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.1);
}

.modal-actions {
  margin-top: 15px;
  display: flex;
  gap: 10px;
  justify-content: center;
}

.modal-actions button {
  padding: 6px 16px;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.modal-actions button:first-child {
  background-color: #4a90e2;
  color: white;
}

.modal-actions button:first-child:hover {
  background-color: #357abd;
}

.modal-actions button:last-child {
  background-color: #f1f1f1;
  color: #666;
}

.modal-actions button:last-child:hover {
  background-color: #e0e0e0;
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
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
