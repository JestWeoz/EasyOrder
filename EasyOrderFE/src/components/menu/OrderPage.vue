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
              order.status
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
                @click="deleteOrder(order.id)"
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
    goHome() {
      this.$emit('go-home')
      this.close()
    },
    refresh() {
      this.$emit('refresh-order')
      this.fetchOrders()
    },
    deleteOrder(orderId) {
      try {
        if (confirm('Bạn có chắc chắn muốn xóa đơn hàng này không?')) {
          axios
            .delete(`http://localhost:8081/order`, {
              params: {
                id: orderId,
              },
            })
            .then((response) => {
              if (response.status === 200 || response.status === 204) {
                alert('Xóa đơn hàng thành công')
                this.fetchOrders()
              } else {
                alert('Không thể xóa đơn hàng')
              }
            })
            .catch((error) => {
              console.error('Lỗi khi xóa đơn hàng:', error)
              alert('Đã xảy ra lỗi khi xóa đơn hàng')
            })
        }
      } catch (error) {
        console.error('Lỗi khi xóa đơn hàng:', error)
        alert('Đã xảy ra lỗi khi xóa đơn hàng')
      }
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
  border-radius: 4px;
  font-size: 14px;
}

.order-status.pending {
  background: #fff3cd;
  color: #856404;
}

.order-status.completed {
  background: #d4edda;
  color: #155724;
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
</style> 