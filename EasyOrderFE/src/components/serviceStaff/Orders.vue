<template>
  <div class="order-manager">
    <h1>Quản lý đơn hàng</h1>

    <!-- Phần lọc đơn hàng -->
    <div class="filter-section">
      <select v-model="statusFilter" class="filter-select">
        <option value="">Tất cả trạng thái</option>
        <option value="PENDING">Đang chờ</option>
        <option value="CONFIRMED">Đã xác nhận</option>
        <option value="PREPARING">Đang chuẩn bị</option>
        <option value="READY">Sẵn sàng phục vụ</option>
        <option value="COMPLETED">Hoàn thành</option>
        <option value="CANCELLED">Đã hủy</option>
      </select>

      <input
        type="text"
        v-model="searchQuery"
        placeholder="Tìm kiếm đơn hàng..."
        class="search-input"
      />
    </div>

    <!-- Bảng đơn hàng -->
    <div class="order-table-container">
      <table class="order-table">
        <thead>
          <tr>
            <th @click="sortBy('table')" style="cursor: pointer">
              Bàn
              <span v-if="sortKey === 'table'">
                <span v-if="sortOrder === 1">&#9650;</span>
                <span v-else>&#9660;</span>
              </span>
            </th>
            <th @click="sortBy('created_at')" style="cursor: pointer">
              Thời gian
              <span v-if="sortKey === 'created_at'">
                <span v-if="sortOrder === 1">&#9650;</span>
                <span v-else>&#9660;</span>
              </span>
            </th>
            <th @click="sortBy('totalAmount')" style="cursor: pointer">
              Tổng tiền
              <span v-if="sortKey === 'totalAmount'">
                <span v-if="sortOrder === 1">&#9650;</span>
                <span v-else>&#9660;</span>
              </span>
            </th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="order in filteredOrders"
            :key="order.id"
            :class="{ highlight: order.status === 'PENDING' }"
          >
            <td>{{ order.table ? order.table.name : 'Không có thông tin' }}</td>
            <td>{{ formatDateTime(order.created_at) }}</td>
            <td>{{ formatCurrency(order.totalAmount) }}</td>

            <td>
              <span class="status-badge" :class="order.status.toLowerCase()">
                {{ getStatusLabel(order.status) }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <template v-if="order.status === 'PENDING'">
                  <button
                    @click="updateOrderStatus({ ...order, status: 'CONFIRMED' })"
                    class="btn btn-confirm"
                  >
                    Xác nhận
                  </button>
                  <button
                    @click="updateOrderStatus({ ...order, status: 'CANCELLED' })"
                    class="btn btn-cancel"
                  >
                    Hủy
                  </button>
                </template>
                <button
                  v-if="order.status === 'READY'"
                  @click="showPaymentModal(order)"
                  class="btn btn-payment"
                >
                  Thanh toán
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="filteredOrders.length === 0">
            <td colspan="6" class="no-orders">Không có đơn hàng nào</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Sử dụng component OrderPaymentModal -->
    <OrderPaymentModal
      v-if="paymentOrder"
      :order="paymentOrder"
      @close="paymentOrder = null"
      @confirm-payment="processPayment"
    />
  </div>
</template>

<script>
import OrderPaymentModal from './OrderPaymentModal.vue'

export default {
  name: 'Orders',
  components: {
    OrderPaymentModal,
  },
  props: {
    orders: {
      type: Array,
      required: true,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    error: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      statusFilter: '',
      searchQuery: '',
      paymentOrder: null,
      sortKey: '',
      sortOrder: 1,
    }
  },
  computed: {
    filteredOrders() {
      if (!this.orders || !Array.isArray(this.orders)) {
        return []
      }

      let result = this.orders.filter((order) => {
        const statusMatch = !this.statusFilter || order.status === this.statusFilter
        const searchLower = this.searchQuery.toLowerCase()
        const searchMatch =
          !this.searchQuery ||
          (order.id && order.id.toString().includes(searchLower)) ||
          (order.table &&
            order.table.name &&
            order.table.name.toString().toLowerCase().includes(searchLower))

        return statusMatch && searchMatch
      })

      // Sắp xếp
      if (this.sortKey) {
        result = result.slice().sort((a, b) => {
          let aValue, bValue
          if (this.sortKey === 'table') {
            aValue = a.table?.name || ''
            bValue = b.table?.name || ''
          } else if (this.sortKey === 'created_at') {
            aValue = a.created_at
            bValue = b.created_at
          } else {
            aValue = a[this.sortKey]
            bValue = b[this.sortKey]
          }

          if (aValue < bValue) return -1 * this.sortOrder
          if (aValue > bValue) return 1 * this.sortOrder
          return 0
        })
      }

      return result
    },
  },
  methods: {
    formatDateTime(dateTimeString) {
      if (!dateTimeString) return 'N/A'
      try {
        const date = new Date(dateTimeString)
        const hours = date.getHours().toString().padStart(2, '0')
        const minutes = date.getMinutes().toString().padStart(2, '0')
        const seconds = date.getSeconds().toString().padStart(2, '0')
        return `${hours}:${minutes}:${seconds}`
      } catch (e) {
        return 'N/A'
      }
    },

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

    getPaymentMethodLabel(method) {
      const methodMap = {
        CASH: 'Tiền mặt',
        CARD: 'Thẻ',
        TRANSFER: 'Chuyển khoản',
        OTHER: 'Khác',
      }
      return methodMap[method] || method
    },

    showPaymentModal(order) {
      this.paymentOrder = order
    },

    async processPayment(paymentData) {
      console.log('Xử lý thanh toán đơn hàng:', this.paymentOrder.id)
      try {
        await this.$emit('process-payment', {
          orderId: this.paymentOrder.id,
          ...paymentData,
        })
        this.paymentOrder = null
      } catch (error) {
        console.error('Lỗi khi xử lý thanh toán:', error)
      }
    },

    async updateOrderStatus(order) {
      await this.$emit('update-order-status', order)
    },

    sortBy(key) {
      if (this.sortKey === key) {
        this.sortOrder = -this.sortOrder
      } else {
        this.sortKey = key
        this.sortOrder = 1
      }
    },
  },
  created() {
    // Không cần gọi fetchOrders nữa vì sẽ được gọi từ component cha
  },
}
</script>

<style scoped>
.order-manager {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  color: #333;
  margin-bottom: 24px;
  font-size: 24px;
}

.filter-section {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.filter-select,
.search-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filter-select {
  width: 200px;
}

.search-input {
  width: 300px;
}

.order-table-container {
  overflow-x: auto;
}

.order-table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.order-table th,
.order-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}

.order-table th {
  background-color: #f5f5f5;
  font-weight: 600;
}

.order-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.order-table tr:hover {
  background-color: #f0f0f0;
}

.highlight {
  background-color: #fff8e1 !important;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 13px;
  color: white;
}

.status-badge.pending {
  background-color: #ff9800;
}

.status-badge.confirmed {
  background-color: #2196f3;
}

.status-badge.preparing {
  background-color: #9c27b0;
}

.status-badge.ready {
  background-color: #4caf50;
}

.status-badge.completed {
  background-color: #00897b;
}

.status-badge.cancelled {
  background-color: #f44336;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.btn-confirm {
  background-color: #4caf50;
  color: white;
}

.btn-confirm:hover {
  background-color: #388e3c;
}

.btn-cancel {
  background-color: #f44336;
  color: white;
}

.btn-cancel:hover {
  background-color: #d32f2f;
}

.btn-payment {
  background-color: #ff9800;
  color: white;
}

.btn-payment:hover {
  background-color: #f57c00;
  color: white;
}

.btn-primary {
  background-color: #4caf50;
  color: white;
  padding: 8px 16px;
}

.no-orders {
  text-align: center;
  padding: 40px;
  color: #888;
}
</style>
