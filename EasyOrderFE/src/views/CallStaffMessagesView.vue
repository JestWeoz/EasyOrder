<template>
  <div class="call-staff-messages">
    <h1>Tin nhắn</h1>
    <div class="messages-container">
      <div v-if="messages.length === 0" class="no-messages">Chưa có tin nhắn nào</div>
      <div v-else class="message-list">
        <div v-for="message in messages" :key="message.id" class="message-item">
          <div class="message-header">
            <span class="sender">bàn {{ message.tableId }}</span>
            <span class="time">{{ formatTime(message.timestamp) }}</span>
          </div>
          <div class="message-content">{{ message.content }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { connectWebSocket, subscribe, disconnectWebSocket } from '@/utils/websocket'

export default {
  name: 'CallStaffMessagesView',
  data() {
    return {
      messages: [],

      subscription: null,
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
  },
  async created() {
    try {
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
      console.error('Lỗi khi kết nối WebSocket:', error)
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
.call-staff-messages {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.messages-container {
  margin-top: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.no-messages {
  text-align: center;
  color: #666;
  padding: 20px;
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
  margin-bottom: 8px;
}

.sender {
  font-weight: bold;
  color: #333;
}

.time {
  color: #666;
  font-size: 0.9em;
}

.message-content {
  color: #444;
  line-height: 1.5;
}
</style> 