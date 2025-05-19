<template>
  <div class="sidebar-overlay" v-if="isOpen" @click="close"></div>
  <div class="sidebar" :class="{ open: isOpen }">
    <div class="sidebar-header">
      <button class="btn p-0 me-2 fs-5 refresh-btn" @click="refresh" title="Làm mới">
        <i class="bi bi-arrow-clockwise"></i>
      </button>
      <h5 class="mb-0 flex-grow-1 text-center fw-bold" style="flex: 1">Đơn hàng</h5>
      <button class="btn p-0 fs-5 close-btn" @click="close" title="Đóng">
        <i class="bi bi-x-lg"></i>
      </button>
    </div>
    <div class="sidebar-content">
      <div v-if="!orders || orders.length === 0" class="text-center p-4">
        <p>Chưa có đơn hàng nào</p>
      </div>
      <div v-else class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-header">
            <span class="order-id">Đơn #{{ order.id }}</span>
            <span class="order-status" :class="order.status?.toLowerCase()">{{
              getStatusLabel(order.status)
            }}</span>
          </div>
          <div class="order-items">
            <div v-for="item in order.orderItems || []" :key="item.id" class="order-item-detail">
              <div class="product-info">
                <img
                  :src="item.product.images[0].url"
                  class="product-thumbnail"
                  :alt="item.product.name"
                />
                <span class="item-name">{{ item.product.name }}</span>
              </div>
              <span class="item-quantity">x{{ item.quantity }}</span>
              <span class="item-price">{{ formatPrice(item.product.price || 0) }}đ</span>
            </div>
          </div>
          <div v-if="order.note" class="order-note">
            <i class="bi bi-info-circle me-1"></i>
            <span>Ghi chú: {{ order.note }}</span>
          </div>
          <div class="order-footer">
            <div class="order-total">
              <span>Tổng tiền:</span>
              <span class="total-amount">{{ formatPrice(order.totalAmount || 0) }}đ</span>
            </div>
            <div class="order-actions-row">
              <button
                class="btn btn-sm delete-btn"
                @click="cancelOrder(order.id)"
                title="Xóa đơn hàng"
              >
                <i class="bi bi-trash"></i>
              </button>
              <div class="order-time">{{ formatDate(order.createdAt) }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal thông báo -->
  <div v-if="showModal" class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h5 class="modal-title">{{ modalTitle }}</h5>
        <button type="button" class="btn-close" @click="closeModal"></button>
      </div>
      <div class="modal-body">
        {{ modalMessage }}
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" @click="closeModal">Đóng</button>
        <button
          v-if="modalType === 'confirm'"
          type="button"
          class="btn btn-danger"
          @click="confirmAction"
        >
          Xác nhận
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'OrderPage',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
    tableId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      orders: [],
      showModal: false,
      modalTitle: '',
      modalMessage: '',
      modalType: 'info', // 'info' hoặc 'confirm'
      pendingAction: null,
    }
  },
  computed: {},
  methods: {
    close() {
      this.$emit('update:isOpen', false)
    },
    formatPrice(price) {
      if (!price) return '0'
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
    },
    formatDate(dateString) {
      if (!dateString) return ''
      try {
        const date = new Date(dateString)
        return date.toLocaleString('vi-VN')
      } catch (error) {
        console.error('Lỗi khi định dạng ngày:', error)
        return ''
      }
    },
    getStatusLabel(status) {
      const statusMap = {
        PENDING: 'Đang chờ nhân viên xác nhận',
        CONFIRMED: 'Đã xác nhận',
        PREPARING: 'Đang chuẩn bị',
        READY: 'Sẵn sàng phục vụ',
        COMPLETED: 'Hoàn thành',
        CANCELLED: 'Đã hủy',
      }
      return statusMap[status] || status
    },
    goHome() {
      this.$emit('go-home')
      this.close()
    },
    refresh() {
      this.$emit('refresh-order')
      this.fetchOrders()
    },
    async fetchOrders() {
      try {
        const response = await axios.get('http://localhost:8081/order', {
          params: {
            tableId: this.tableId,
          },
        })
        if (response.data && response.data.result) {
          this.orders = response.data.result
        } else {
          this.orders = []
        }
      } catch (error) {
        console.error('Lỗi khi lấy danh sách đơn hàng:', error)
        this.orders = []
      }
    },
    async updateOrder(orderId, updateData) {
      try {
        // Tìm đơn hàng hiện tại
        const currentOrder = this.orders.find((o) => o.id === orderId)
        if (!currentOrder) {
          throw new Error('Không tìm thấy đơn hàng')
        }

        // Tạo OrderReq với dữ liệu hiện tại và cập nhật mới
        const orderReq = {
          orderId: orderId,
          tableId: currentOrder.table?.id,
          orderItems: currentOrder.orderItems,
          totalAmount: currentOrder.totalAmount,
          status: updateData.status || currentOrder.status,
          note: currentOrder.note,
          paymentMethod: updateData.paymentMethod || currentOrder.paymentMethod,
          isPaid: updateData.isPaid || currentOrder.isPaid,
          customerName: currentOrder.customerName,
          customerPhone: currentOrder.customerPhone,
        }

        // Gọi API để cập nhật đơn hàng
        await axios.put(`http://localhost:8081/order/updateOrder`, orderReq)

        // Cập nhật lại danh sách đơn hàng
        const index = this.orders.findIndex((o) => o.id === orderId)
        if (index !== -1) {
          this.orders[index] = { ...this.orders[index], ...updateData }
        }

        // Hiển thị thông báo thành công
        console.log(`Đã cập nhật đơn hàng #${orderId}`)
      } catch (error) {
        console.error('Lỗi khi cập nhật đơn hàng:', error)
        alert('Đã xảy ra lỗi khi cập nhật đơn hàng')
      }
    },
    showMessage(title, message, type = 'info', action = null) {
      this.modalTitle = title
      this.modalMessage = message
      this.modalType = type
      this.pendingAction = action
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
      this.pendingAction = null
    },
    confirmAction() {
      if (this.pendingAction) {
        this.pendingAction()
      }
      this.closeModal()
    },
    async cancelOrder(orderId) {
      try {
        // Tìm đơn hàng hiện tại
        const currentOrder = this.orders.find((o) => o.id === orderId)
        if (!currentOrder) {
          throw new Error('Không tìm thấy đơn hàng')
        }

        // Kiểm tra nếu đơn hàng đang trong trạng thái PREPARING
        if (currentOrder.status === 'PREPARING') {
          this.showMessage(
            'Không thể hủy đơn hàng',
            'Bếp đang chuẩn bị món cho quý khách, không thể hủy đơn hàng'
          )
          return
        }

        this.showMessage(
          'Xác nhận hủy đơn hàng',
          'Bạn có chắc chắn muốn hủy đơn hàng này không?',
          'confirm',
          async () => {
            await this.updateOrder(orderId, {
              status: 'CANCELLED',
            })
            this.showMessage('Thành công', 'Đã hủy đơn hàng thành công')
            this.fetchOrders()
          }
        )
      } catch (error) {
        console.error('Lỗi khi hủy đơn hàng:', error)
        this.showMessage('Lỗi', 'Đã xảy ra lỗi khi hủy đơn hàng')
      }
    },
  },
  watch: {
    isOpen(newVal) {
      if (newVal) {
        this.fetchOrders()
      }
    },
  },
  emits: ['update:isOpen', 'go-home', 'delete-order', 'refresh-order'],
}
</script>

<style scoped>
.sidebar-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 3000;
}

.sidebar {
  position: fixed;
  top: 0;
  right: -100%;
  width: 100%;
  height: 100%;
  background-color: white;
  z-index: 3001;
  transition: right 0.5s ease;
  display: flex;
  flex-direction: column;
}

.sidebar.open {
  right: 0;
}
.close-btn {
  border: none;
  background: none;
}
.refresh-btn {
  border: none;
  background: none;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.sidebar-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
}

.order-list {
  padding: 16px;
}

.order-item {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
  padding: 16px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.order-id {
  font-weight: bold;
  font-size: 16px;
}

.order-status {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 13px;
  color: white;
}

.order-status.pending {
  background-color: #ff9800;
}

.order-status.confirmed {
  background-color: #2196f3;
}

.order-status.preparing {
  background-color: #9c27b0;
}

.order-status.ready {
  background-color: #4caf50;
}

.order-status.completed {
  background-color: #00897b;
}

.order-status.cancelled {
  background-color: #f44336;
}

.order-items {
  margin-bottom: 12px;
}

.order-item-detail {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
}

.product-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.product-thumbnail {
  width: 30px;
  height: 30px;
  border-radius: 4px;
  object-fit: cover;
  margin-right: 8px;
}

.item-name {
  flex: 1;
}

.item-quantity {
  margin: 0 8px;
  color: #666;
}

.item-price {
  font-weight: 500;
}

.order-footer {
  border-top: 1px solid #eee;
  padding-top: 12px;
}

.order-total {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
  margin-bottom: 8px;
}

.order-actions-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-time {
  font-size: 12px;
  color: #666;
}

.delete-btn {
  padding: 2px 6px;
  color: #dc3545;
  background: none;
  border: none;
}

.delete-btn:hover {
  background-color: #f8d7da;
  border-radius: 4px;
}

.order-note {
  font-size: 13px;
  color: #666;
  margin: 8px 0;
  padding: 6px 10px;
  background-color: #f8f9fa;
  border-left: 3px solid #6c757d;
  border-radius: 3px;
  display: flex;
  align-items: center;
}

.order-note i {
  margin-right: 6px;
  color: #6c757d;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 4000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.modal-header {
  padding: 16px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.modal-body {
  padding: 16px;
  font-size: 16px;
}

.modal-footer {
  padding: 16px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.btn-close {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  color: #666;
}

.btn-close:hover {
  color: #333;
}

.btn {
  padding: 8px 16px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background-color: #5a6268;
}

.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-danger:hover {
  background-color: #c82333;
}
</style> 