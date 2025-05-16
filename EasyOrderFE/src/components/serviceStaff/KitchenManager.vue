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
                    <h5>Bàn {{ order.tableNumber }}</h5>
                    <span class="badge bg-warning">{{ formatOrderStatus(order.status) }}</span>
                  </div>
                  <div class="order-details">
                    <p><strong>Mã đơn:</strong> #{{ order.id }}</p>
                    <p><strong>Thời gian:</strong> {{ formatTime(order.createdAt) }}</p>
                  </div>
                  <div class="order-items">
                    <h6>Món ăn:</h6>
                    <ul>
                      <li v-for="item in order.items" :key="item.id">
                        {{ item.quantity }}x {{ item.name }}
                        <span v-if="item.note" class="item-note">
                          <i class="bi bi-info-circle"></i>
                          {{ item.note }}
                        </span>
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
                  </div>
                  <div class="order-items">
                    <h6>Món ăn:</h6>
                    <ul>
                      <li v-for="item in order.items" :key="item.id">
                        {{ item.quantity }}x {{ item.name }}
                        <span v-if="item.note" class="item-note">
                          <i class="bi bi-info-circle"></i>
                          {{ item.note }}
                        </span>
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
export default {
  name: 'KitchenManager',
  data() {
    return {
      pendingOrders: [
        {
          id: 1001,
          tableNumber: '01',
          status: 'pending',
          createdAt: new Date(),
          items: [
            { id: 1, name: 'Gà rán', quantity: 2, note: 'Không cay' },
            { id: 2, name: 'Hamburger bò', quantity: 1, note: '' },
            { id: 3, name: 'Khoai tây chiên', quantity: 2, note: 'Thêm sốt cà' },
          ],
        },
        {
          id: 1002,
          tableNumber: '03',
          status: 'pending',
          createdAt: new Date(Date.now() - 10 * 60000),
          items: [
            { id: 4, name: 'Pizza hải sản', quantity: 1, note: 'Không hành' },
            { id: 5, name: 'Mỳ Ý', quantity: 1, note: '' },
          ],
        },
      ],
      processingOrders: [
        {
          id: 1000,
          tableNumber: '02',
          status: 'processing',
          createdAt: new Date(Date.now() - 15 * 60000),
          items: [
            { id: 6, name: 'Lẩu Thái', quantity: 1, note: 'Cay vừa' },
            { id: 7, name: 'Cơm chiên', quantity: 2, note: '' },
          ],
        },
      ],
    }
  },
  methods: {
    formatOrderStatus(status) {
      switch (status) {
        case 'pending':
          return 'Chờ chế biến'
        case 'processing':
          return 'Đang chế biến'
        case 'completed':
          return 'Hoàn thành'
        default:
          return status
      }
    },
    formatTime(date) {
      return new Date(date).toLocaleTimeString('vi-VN', {
        hour: '2-digit',
        minute: '2-digit',
      })
    },
    startProcessing(orderId) {
      // Tìm đơn hàng cần xử lý
      const orderIndex = this.pendingOrders.findIndex((order) => order.id === orderId)
      if (orderIndex >= 0) {
        const order = { ...this.pendingOrders[orderIndex] }
        order.status = 'processing'

        // Xóa khỏi danh sách chờ và thêm vào danh sách đang xử lý
        this.pendingOrders.splice(orderIndex, 1)
        this.processingOrders.push(order)

        // Gửi yêu cầu API để cập nhật trạng thái (trong thực tế)
        // this.updateOrderStatus(orderId, 'processing')
      }
    },
    completeOrder(orderId) {
      // Tìm đơn hàng trong cả hai danh sách
      let orderIndex = this.pendingOrders.findIndex((order) => order.id === orderId)
      let fromPending = true

      if (orderIndex < 0) {
        orderIndex = this.processingOrders.findIndex((order) => order.id === orderId)
        fromPending = false
      }

      if (orderIndex >= 0) {
        // Xóa đơn hàng khỏi danh sách tương ứng
        if (fromPending) {
          this.pendingOrders.splice(orderIndex, 1)
        } else {
          this.processingOrders.splice(orderIndex, 1)
        }

        // Gửi yêu cầu API để cập nhật trạng thái (trong thực tế)
        // this.updateOrderStatus(orderId, 'completed')
      }
    },
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