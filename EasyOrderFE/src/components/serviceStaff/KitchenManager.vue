<template>
  <div class="page-heading">
    <div class="page-title">
      <div class="row">
        <div class="col-12 col-md-6 order-md-1 order-last">
          <h3>Quản lý Bếp</h3>
          <p class="text-subtitle text-muted">Theo dõi và quản lý đơn hàng đang chờ chế biến</p>
        </div>
      </div>
    </div>

    <section class="section">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h4 class="card-title">Đơn hàng chờ chế biến</h4>
            </div>
            <div class="card-body">
              <div v-if="pendingOrders.length === 0" class="text-center p-5">
                <p>Không có đơn hàng nào đang chờ chế biến</p>
              </div>
              <div v-else class="order-grid">
                <div v-for="order in pendingOrders" :key="order.id" class="order-card">
                  <div class="order-header">
                    <h5>{{ order.table.name }}</h5>
                    <span class="badge bg-warning">{{ formatOrderStatus(order.status) }}</span>
                  </div>
                  <div class="order-details">
                    <p><strong>Mã đơn:</strong> #{{ order.id }}</p>
                    <p><strong>Thời gian:</strong> {{ formatTime(order.created_at) }}</p>
                    <p v-if="order.note"><strong>Ghi chú:</strong> {{ order.note }}</p>
                  </div>
                  <div class="order-items">
                    <h6>Món ăn:</h6>
                    <ul>
                      <li v-for="item in order.orderItems" :key="item.id">
                        {{ item.quantity }}x {{ item.product.name }}
                      </li>
                    </ul>
                  </div>
                  <div class="order-actions">
                    <button class="btn btn-primary btn-sm" @click="startProcessing(order.id)">
                      Bắt đầu chế biến
                    </button>
                    <button class="btn btn-success btn-sm" @click="completeOrder(order.id)">
                      Hoàn thành
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="section">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h4 class="card-title">Đơn hàng đang chế biến</h4>
            </div>
            <div class="card-body">
              <div v-if="processingOrders.length === 0" class="text-center p-5">
                <p>Không có đơn hàng nào đang chế biến</p>
              </div>
              <div v-else class="order-grid">
                <div
                  v-for="order in processingOrders"
                  :key="order.id"
                  class="order-card processing"
                >
                  <div class="order-header">
                    <h5>Bàn {{ order.tableNumber }}</h5>
                    <span class="badge bg-info">{{ formatOrderStatus(order.status) }}</span>
                  </div>
                  <div class="order-details">
                    <p><strong>Mã đơn:</strong> #{{ order.id }}</p>
                    <p><strong>Thời gian:</strong> {{ formatTime(order.createdAt) }}</p>
                    <p v-if="order.note"><strong>Ghi chú:</strong> {{ order.note }}</p>
                  </div>
                  <div class="order-items">
                    <h6>Món ăn:</h6>
                    <ul>
                      <li v-for="item in order.items" :key="item.id">
                        {{ item.quantity }}x {{ item.name }}
                      </li>
                    </ul>
                  </div>
                  <div class="order-actions">
                    <button class="btn btn-success btn-sm" @click="completeOrder(order.id)">
                      Hoàn thành
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'KitchenManager',
  data() {
    return {
      pendingOrders: [],
      processingOrders: [],
      loading: false,
      error: null,
    }
  },
  methods: {
    formatOrderStatus(status) {
      switch (status) {
        case 'PENDING':
          return 'Chờ xác nhận'
        case 'CONFIRMED':
          return 'Chờ chế biến'
        case 'PREPARING':
          return 'Đang chế biến'
        case 'READY':
          return 'Sẵn sàng phục vụ'
        case 'COMPLETED':
          return 'Hoàn thành'
        case 'CANCELLED':
          return 'Đã hủy'
        default:
          return status
      }
    },
    formatTime(date) {
      if (!date) return 'N/A'
      try {
        const dateObj = new Date(date)
        return dateObj.toLocaleTimeString('vi-VN', {
          hour: '2-digit',
          minute: '2-digit',
        })
      } catch (e) {
        return 'N/A'
      }
    },
    async startProcessing(orderId) {
      try {
        // Tìm đơn hàng cần xử lý
        const orderIndex = this.pendingOrders.findIndex((order) => order.id === orderId)
        if (orderIndex >= 0) {
          const order = { ...this.pendingOrders[orderIndex] }

          // Gọi API để cập nhật trạng thái
          await this.updateOrder(orderId, {
            status: 'PREPARING',
          })

          // Cập nhật UI
          order.status = 'PREPARING'
          this.pendingOrders.splice(orderIndex, 1)
          this.processingOrders.push(order)
        }
      } catch (error) {
        this.error = 'Lỗi khi bắt đầu chế biến đơn hàng'
        console.error(error)
      }
    },
    async completeOrder(orderId) {
      try {
        // Tìm đơn hàng trong cả hai danh sách
        let orderIndex = this.pendingOrders.findIndex((order) => order.id === orderId)
        let fromPending = true

        if (orderIndex < 0) {
          orderIndex = this.processingOrders.findIndex((order) => order.id === orderId)
          fromPending = false
        }

        if (orderIndex >= 0) {
          // Gọi API để cập nhật trạng thái
          await this.updateOrder(orderId, {
            status: 'READY',
          })

          // Cập nhật UI
          if (fromPending) {
            this.pendingOrders.splice(orderIndex, 1)
          } else {
            this.processingOrders.splice(orderIndex, 1)
          }
        }
      } catch (error) {
        this.error = 'Lỗi khi hoàn thành đơn hàng'
        console.error(error)
      }
    },
    async updateOrder(orderId, updateData) {
      try {
        this.loading = true
        // Tìm đơn hàng hiện tại
        const currentOrder = [...this.pendingOrders, ...this.processingOrders].find(
          (o) => o.id === orderId
        )
        if (!currentOrder) {
          throw new Error('Không tìm thấy đơn hàng')
        }

        // Tạo OrderReq với dữ liệu hiện tại và cập nhật mới
        const orderReq = {
          orderId: orderId,
          tableId: currentOrder.table?.id,
          orderItems: currentOrder.items,
          totalAmount: currentOrder.totalAmount,
          status: updateData.status || currentOrder.status,
          note: currentOrder.note,
          paymentMethod: currentOrder.paymentMethod,
          isPaid: currentOrder.isPaid,
          customerName: currentOrder.customerName,
          customerPhone: currentOrder.customerPhone,
        }

        // Gọi API để cập nhật đơn hàng
        await axios.put(`http://localhost:8081/order/updateOrder`, orderReq)
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },
    async fetchOrders() {
      try {
        this.loading = true
        const response = await axios.get('http://localhost:8081/order/getAll')
        const allOrders = response.data.result || []
        console.log(allOrders)
        // Phân loại đơn hàng
        this.pendingOrders = allOrders.filter((order) => order.status === 'CONFIRMED')
        this.processingOrders = allOrders.filter((order) => order.status === 'PREPARING')
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
.order-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 10px;
}

.order-card {
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  padding: 20px;
  position: relative;
  transition: all 0.3s ease;
}

.order-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.order-card.processing {
  border-left: 4px solid #3f51b5;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.order-header h5 {
  margin: 0;
  font-weight: 600;
}

.order-details {
  margin-bottom: 15px;
}

.order-details p {
  margin: 5px 0;
  color: #555;
}

.order-items {
  margin-bottom: 15px;
}

.order-items h6 {
  margin-bottom: 10px;
  font-weight: 600;
}

.order-items ul {
  list-style: none;
  padding-left: 0;
}

.order-items li {
  padding: 5px 0;
  border-bottom: 1px dashed #eee;
}

.order-items li:last-child {
  border-bottom: none;
}

.item-note {
  display: block;
  font-size: 0.85em;
  color: #777;
  padding-left: 5px;
  margin-top: 3px;
}

.item-note i {
  margin-right: 3px;
}

.order-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 10px;
}
</style> 