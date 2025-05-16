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
            <th>Phương thức thanh toán</th>
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
            <td>{{ order.tableNumber }}</td>
            <td>{{ formatDateTime(order.orderTime) }}</td>
            <td>{{ formatCurrency(order.totalAmount) }}</td>
            <td>{{ getPaymentMethodLabel(order.paymentMethod) }}</td>
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
              <button @click="viewOrderDetails(order)" class="btn btn-view">Chi tiết</button>
            </td>
          </tr>
          <tr v-if="filteredOrders.length === 0">
            <td colspan="7" class="no-orders">Không có đơn hàng nào</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal chi tiết đơn hàng -->
    <div v-if="selectedOrder" class="order-modal">
      <div class="order-modal-content">
        <div class="order-modal-header">
          <h2>Chi tiết đơn hàng #{{ selectedOrder.id }}</h2>
          <button @click="selectedOrder = null" class="close-btn">&times;</button>
        </div>
        <div class="order-modal-body">
          <div class="order-info">
            <p><strong>Bàn:</strong> {{ selectedOrder.tableNumber }}</p>
            <p><strong>Thời gian:</strong> {{ formatDateTime(selectedOrder.orderTime) }}</p>
            <p><strong>Trạng thái:</strong> {{ getStatusLabel(selectedOrder.status) }}</p>
            <p>
              <strong>Phương thức thanh toán:</strong>
              {{ getPaymentMethodLabel(selectedOrder.paymentMethod) }}
            </p>
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
              <tr v-for="(item, index) in selectedOrder.items" :key="index">
                <td>{{ item.name }}</td>
                <td>{{ item.quantity }}</td>
                <td>{{ formatCurrency(item.price) }}</td>
                <td>{{ formatCurrency(item.price * item.quantity) }}</td>
              </tr>
            </tbody>
            <tfoot>
              <tr>
                <td colspan="3" class="total-label">Tổng cộng:</td>
                <td class="total-amount">{{ formatCurrency(selectedOrder.totalAmount) }}</td>
              </tr>
            </tfoot>
          </table>

          <div class="order-actions">
            <select v-model="selectedOrder.status" class="status-select">
              <option value="PENDING">Đang chờ</option>
              <option value="CONFIRMED">Đã xác nhận</option>
              <option value="PREPARING">Đang chuẩn bị</option>
              <option value="READY">Sẵn sàng phục vụ</option>
              <option value="COMPLETED">Hoàn thành</option>
              <option value="CANCELLED">Đã hủy</option>
            </select>
            <button @click="updateOrderStatus(selectedOrder)" class="btn btn-primary">
              Cập nhật trạng thái
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Orders',
  data() {
    return {
      orders: [],
      selectedOrder: null,
      statusFilter: '',
      searchQuery: '',
      loading: false,
      error: null,
    }
  },
  computed: {
    filteredOrders() {
      return this.orders.filter((order) => {
        // Lọc theo trạng thái
        const statusMatch = !this.statusFilter || order.status === this.statusFilter

        // Lọc theo từ khóa tìm kiếm
        const searchLower = this.searchQuery.toLowerCase()
        const searchMatch =
          !this.searchQuery ||
          order.id.toString().includes(searchLower) ||
          order.tableNumber.toString().includes(searchLower)

        return statusMatch && searchMatch
      })
    },
  },
  methods: {
    // Hàm định dạng thời gian
    formatDateTime(dateTimeString) {
      const date = new Date(dateTimeString)
      return new Intl.DateTimeFormat('vi-VN', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      }).format(date)
    },

    // Hàm định dạng tiền tệ
    formatCurrency(amount) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
      }).format(amount)
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

    // Hàm xem chi tiết đơn hàng
    viewOrderDetails(order) {
      this.selectedOrder = { ...order }
    },

    // Hàm cập nhật trạng thái đơn hàng
    async updateOrderStatus(order) {
      try {
        this.loading = true
        // Gọi API để cập nhật trạng thái
        // Ví dụ: await api.updateOrderStatus(order.id, order.status);

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
        alert(
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
        // Trong ví dụ này, tạo dữ liệu mẫu
        this.orders = [
          {
            id: 1001,
            tableNumber: 5,
            orderTime: new Date().toISOString(),
            totalAmount: 250000,
            paymentMethod: 'CASH',
            status: 'PENDING',
            items: [
              { name: 'Cà phê đen', quantity: 2, price: 30000 },
              { name: 'Bánh mì thịt', quantity: 3, price: 35000 },
              { name: 'Nước cam', quantity: 1, price: 50000 },
            ],
          },
          {
            id: 1002,
            tableNumber: 8,
            orderTime: new Date(Date.now() - 30 * 60000).toISOString(),
            totalAmount: 475000,
            paymentMethod: 'CARD',
            status: 'CONFIRMED',
            items: [
              { name: 'Mì xào hải sản', quantity: 2, price: 120000 },
              { name: 'Salad trộn', quantity: 1, price: 75000 },
              { name: 'Nước ngọt', quantity: 4, price: 40000 },
            ],
          },
          {
            id: 1003,
            tableNumber: 12,
            orderTime: new Date(Date.now() - 90 * 60000).toISOString(),
            totalAmount: 850000,
            paymentMethod: 'TRANSFER',
            status: 'PREPARING',
            items: [
              { name: 'Bò bít tết', quantity: 2, price: 250000 },
              { name: 'Súp cua', quantity: 2, price: 100000 },
              { name: 'Rượu vang', quantity: 1, price: 250000 },
            ],
          },
        ]
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
}

.btn-view {
  background-color: #2196f3;
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

/* Modal styles */
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

.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>
