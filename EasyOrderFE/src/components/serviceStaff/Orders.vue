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
            <th>Mã đơn</th>
            <th>Bàn</th>
            <th>Thời gian</th>
            <th>Tổng tiền</th>
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
            <td>{{ order.id }}</td>
            <td>{{ order.table ? order.table.name : 'Không có thông tin' }}</td>
            <td>{{ formatDateTime(order.created_at) }}</td>
            <td>{{ formatCurrency(order.totalAmount) }}</td>

            <td>
              <span class="status-badge" :class="order.status.toLowerCase()">
                {{ getStatusLabel(order.status) }}
              </span>
            </td>
            <td>
              <select
                v-model="order.status"
                @change="updateOrderStatus(order)"
                class="status-select"
              >
                <option value="PENDING">Đang chờ</option>
                <option value="CONFIRMED">Đã xác nhận</option>
                <option value="PREPARING">Đang chuẩn bị</option>
                <option value="READY">Sẵn sàng phục vụ</option>
                <option value="COMPLETED">Hoàn thành</option>
                <option value="CANCELLED">Đã hủy</option>
              </select>

              <button
                @click="showPaymentModal(order)"
                class="btn btn-payment"
                v-if="order.status !== 'COMPLETED' && order.status !== 'CANCELLED'"
              >
                Thanh toán
              </button>
            </td>
          </tr>
          <tr v-if="filteredOrders.length === 0">
            <td colspan="7" class="no-orders">Không có đơn hàng nào</td>
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
import axios from 'axios'
import OrderPaymentModal from './OrderPaymentModal.vue'

export default {
  name: 'Orders',
  components: {
    OrderPaymentModal,
  },
  data() {
    return {
      orders: [],
      selectedOrder: null,
      paymentOrder: null,
      statusFilter: '',
      searchQuery: '',
      loading: false,
      error: null,
    }
  },
  computed: {
    filteredOrders() {
      if (!this.orders || !Array.isArray(this.orders)) {
        return []
      }

      return this.orders.filter((order) => {
        // Lọc theo trạng thái
        const statusMatch = !this.statusFilter || order.status === this.statusFilter

        // Lọc theo từ khóa tìm kiếm
        const searchLower = this.searchQuery.toLowerCase()
        const searchMatch =
          !this.searchQuery ||
          (order.id && order.id.toString().includes(searchLower)) ||
          (order.table &&
            order.table.name &&
            order.table.name.toString().toLowerCase().includes(searchLower))

        return statusMatch && searchMatch
      })
    },
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
        // Định dạng: DD/MM/YYYY HH:MM:SS
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

    // Hàm lấy tên phương thức thanh toán
    getPaymentMethodLabel(method) {
      const methodMap = {
        CASH: 'Tiền mặt',
        CARD: 'Thẻ',
        TRANSFER: 'Chuyển khoản',
        OTHER: 'Khác',
      }
      return methodMap[method] || method
    },

    // Hàm hiển thị modal thanh toán
    showPaymentModal(order) {
      // Kiểm tra xem order có đầy đủ thông tin không
      if (!order) return

      // Tạo bản sao của đơn hàng để tránh thay đổi trực tiếp
      const orderCopy = { ...order }

      // Đảm bảo các trường cần thiết đều tồn tại
      if (!orderCopy.items) orderCopy.items = []
      if (!orderCopy.totalAmount) orderCopy.totalAmount = 0

      this.paymentOrder = orderCopy
    },

    // Hàm xử lý thanh toán
    async processPayment(paymentData) {
      try {
        this.loading = true
        // Gọi API để cập nhật trạng thái và phương thức thanh toán
        await axios.post(`http://localhost:8081/order/payment/${paymentData.orderId}`, {
          paymentMethod: paymentData.paymentMethod,
        })

        // Cập nhật trạng thái đơn hàng thành COMPLETED
        await this.updateOrderStatus({
          id: paymentData.orderId,
          status: 'COMPLETED',
        })

        // Đóng modal thanh toán
        this.paymentOrder = null

        // Hiển thị thông báo thành công
        console.log(`Đã thanh toán đơn hàng #${paymentData.orderId}`)

        // Reload danh sách đơn hàng
        this.fetchOrders()
      } catch (error) {
        this.error = 'Lỗi khi xử lý thanh toán'
        console.error(error)
      } finally {
        this.loading = false
      }
    },

    // Hàm cập nhật trạng thái đơn hàng
    async updateOrderStatus(order) {
      try {
        this.loading = true
        // Gọi API để cập nhật trạng thái
        await axios.put(`http://localhost:8081/order/updateStatus/${order.id}`, {
          status: order.status,
        })

        // Cập nhật lại danh sách đơn hàng
        const index = this.orders.findIndex((o) => o.id === order.id)
        if (index !== -1) {
          this.orders[index].status = order.status
        }

        // Đóng modal nếu đang mở
        if (this.selectedOrder && this.selectedOrder.id === order.id) {
          this.selectedOrder.status = order.status
        }

        // Hiển thị thông báo thành công
        console.log(
          `Đã cập nhật trạng thái đơn hàng #${order.id} thành ${this.getStatusLabel(order.status)}`
        )
      } catch (error) {
        this.error = 'Lỗi khi cập nhật trạng thái đơn hàng'
        console.error(error)
      } finally {
        this.loading = false
      }
    },

    // Hàm tải danh sách đơn hàng
    async fetchOrders() {
      try {
        this.loading = true
        // Gọi API để lấy danh sách đơn hàng
        const response = await axios.get('http://localhost:8081/order/getAll')
        console.log(response.data)
        this.orders = response.data.result || []
        console.log(this.orders[0].created_at)
      } catch (error) {
        this.error = 'Lỗi khi tải danh sách đơn hàng'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
  },
  created() {
    // Tải danh sách đơn hàng khi component được tạo
    this.fetchOrders()
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

.status-select {
  padding: 6px;
  border-radius: 4px;
  border: 1px solid #ddd;
  margin-right: 8px;
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

.btn-view {
  background-color: #2196f3;
  color: white;
  margin-right: 5px;
}

.btn-view:hover {
  background-color: #0d8aee;
  color: white;
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
