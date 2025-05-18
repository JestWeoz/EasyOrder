<template>
  <div v-if="order" class="order-modal">
    <div class="order-modal-content">
      <div class="order-modal-header">
        <h2>Thanh toán đơn hàng #{{ order.id }}</h2>
        <button @click="close" class="close-btn">&times;</button>
      </div>
      <div class="order-modal-body">
        <div class="order-info">
          <p>
            <strong>Bàn:</strong>
            {{ order.table ? order.table.name : order.tableNumber }}
          </p>
          <p><strong>Thời gian:</strong> {{ formatDateTime(order.created_at) }}</p>
          <p><strong>Trạng thái:</strong> {{ getStatusLabel(order.status) }}</p>
        </div>

        <h3>Các món đã gọi</h3>
        <table class="order-items-table">
          <thead>
            <tr>
              <th>Món</th>
              <th>Số lượng</th>
              <th>Đơn giá</th>
              <th>Thành tiền</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!order.orderItems || order.orderItems.length === 0">
              <td colspan="4" class="no-items">Không có món nào trong đơn hàng</td>
            </tr>
            <tr v-for="(item, index) in order.orderItems || []" :key="index">
              <td>{{ item.product.name }}</td>
              <td>{{ item.quantity }}</td>
              <td>{{ formatCurrency(item.product.price) }}</td>
              <td>{{ formatCurrency(item.product.price * item.quantity) }}</td>
            </tr>
          </tbody>
          <tfoot>
            <tr>
              <td colspan="3" class="total-label">Tổng cộng:</td>
              <td class="total-amount">{{ formatCurrency(order.totalAmount) }}</td>
            </tr>
          </tfoot>
        </table>

        <div class="payment-section">
          <h3>Chọn phương thức thanh toán</h3>
          <div class="payment-methods">
            <div
              v-for="method in paymentMethods"
              :key="method.value"
              class="payment-method-item"
              :class="{ selected: selectedPaymentMethod === method.value }"
              @click="selectedPaymentMethod = method.value"
            >
              <span class="payment-method-name">{{ method.label }}</span>
            </div>
          </div>
          <div class="payment-actions">
            <button @click="close" class="btn btn-cancel">Hủy</button>
            <button
              @click="confirmPayment"
              class="btn btn-confirm-payment"
              :disabled="!selectedPaymentMethod"
            >
              Xác nhận thanh toán
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrderPaymentModal',
  props: {
    order: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      selectedPaymentMethod: null,
      paymentMethods: [
        { value: 'CASH', label: 'Tiền mặt' },
        { value: 'CARD', label: 'Thẻ' },
        { value: 'TRANSFER', label: 'Chuyển khoản' },
        { value: 'OTHER', label: 'Khác' },
      ],
    }
  },
  methods: {
    // Hàm định dạng thời gian
    formatDateTime(dateTimeString) {
      if (!dateTimeString) return 'N/A'
      try {
        const date = new Date(dateTimeString)
        // Lấy giờ, phút, giây theo định dạng hh:mm:ss
        const hours = date.getHours().toString().padStart(2, '0')
        const minutes = date.getMinutes().toString().padStart(2, '0')
        const seconds = date.getSeconds().toString().padStart(2, '0')
        // Định dạng: HH:MM:SS
        return `${hours}:${minutes}:${seconds}`
      } catch (e) {
        return 'N/A'
      }
    },

    // Hàm định dạng tiền tệ
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '0 ₫'
      try {
        return new Intl.NumberFormat('vi-VN', {
          style: 'currency',
          currency: 'VND',
        }).format(amount)
      } catch (e) {
        return '0 ₫'
      }
    },

    // Hàm lấy tên trạng thái
    getStatusLabel(status) {
      const statusMap = {
        PENDING: 'Đang chờ',
        CONFIRMED: 'Đã xác nhận',
        PREPARING: 'Đang chuẩn bị',
        READY: 'Sẵn sàng phục vụ',
        COMPLETED: 'Hoàn thành',
        CANCELLED: 'Đã hủy',
      }
      return statusMap[status] || status
    },

    // Xác nhận thanh toán
    confirmPayment() {
      if (!this.selectedPaymentMethod) return

      this.$emit('confirm-payment', {
        orderId: this.order.id,
        paymentMethod: this.selectedPaymentMethod,
      })
    },

    // Đóng modal
    close() {
      this.$emit('close')
    },
  },
}
</script>

<style scoped>
.order-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.order-modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  width: 80%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
}

.order-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #eee;
}

.order-modal-header h2 {
  margin: 0;
  font-size: 20px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.order-modal-body {
  padding: 20px;
}

.order-info {
  background-color: #f9f9f9;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.order-info p {
  margin: 8px 0;
}

.order-items-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.order-items-table th,
.order-items-table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

.order-items-table th {
  background-color: #f5f5f5;
}

.total-label {
  text-align: right;
  font-weight: bold;
}

.total-amount {
  font-weight: bold;
}

.no-items {
  text-align: center;
  padding: 40px;
  color: #888;
}

.payment-section {
  margin-top: 20px;
}

.payment-methods {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.payment-method-item {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.1s;
}

.payment-method-item:hover {
  background-color: #f5f5f5;
  transform: translateY(-1px);
  color: inherit;
}

.selected {
  background-color: #f0f0f0;
}

.payment-method-name {
  font-weight: bold;
}

.payment-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s, transform 0.1s;
}

.btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  color: white;
}

.btn:active {
  transform: translateY(0);
}

.btn-cancel {
  background-color: #f44336;
  color: white;
}

.btn-cancel:hover {
  background-color: #e53935;
  color: white;
}

.btn-confirm-payment {
  background-color: #4caf50;
  color: white;
}

.btn-confirm-payment:hover {
  background-color: #43a047;
  color: white;
}

.btn-confirm-payment:disabled {
  background-color: #cccccc;
  color: #888888;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.btn-confirm-payment:disabled:hover {
  background-color: #cccccc;
  box-shadow: none;
  transform: none;
}
</style> 