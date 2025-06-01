<template>
  <div class="container-fluid admin-container p-0">
    <ServiceStaffSideBar
      @toggle-sidebar="toggleSidebar"
      :active-component="currentRouteName"
      @change-component="setCurrentComponent"
    />
    <div id="main" class="layout-navbar">
      <StaffServiceHeader
        @toggle-sidebar="toggleSidebar"
        @logout="logout"
        :userInfo="userInfo"
        :messages="messages"
      />
      <router-view v-slot="{ Component }">
        <component
          :is="Component"
          :messages="messages"
          :tables="tables"
          :showTableModal="showTableModal"
          :selectedTable="selectedTable || {}"
          :orders="orders"
          :loading="loading"
          :error="error"
          :formatDateTime="formatDateTime"
          :formatCurrency="formatCurrency"
          :getStatusLabel="getStatusLabel"
          :getPaymentMethodLabel="getPaymentMethodLabel"
          @table-click="handleTableClick"
          @update-table-status="updateTableStatus"
          @clear-messages="clearMessages"
          @update:showTableModal="showTableModal = $event"
          @show-payment-modal="showPaymentModal"
          @update-order-status="updateOrderStatus"
          @process-payment="processPayment"
        />
      </router-view>
    </div>
  </div>
</template>

<script>
import ServiceStaffSideBar from '@/components/serviceStaff/SideBar.vue'
import StaffServiceHeader from '@/components/serviceStaff/Header.vue'
import axios from 'axios'
import { jwtDecode } from 'jwt-decode'
import { connectWebSocket, subscribe, disconnectWebSocket } from '@/utils/websocket'

export default {
  name: 'StaffView',
  components: {
    ServiceStaffSideBar,
    StaffServiceHeader,
  },
  data() {
    return {
      currentRouteName: '',
      userInfo: {
        name: '',
        role: '',
        avatar: '',
      },
      messages: [],
      subscription: null,
      tables: [],
      showTableModal: false,
      selectedTable: null,
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
    pageTitle() {
      return ` ${this.userInfo.name || 'Nhân viên'}`
    },
  },
  async created() {
    this.introspectToken()
    // Cập nhật component hiện tại dựa trên route
    this.updateCurrentComponent()
    this.fetchOrders()

    // Lấy thông tin người dùng từ token
    this.getUserInfo()
    
    // Cập nhật title
    document.title = this.pageTitle

    try {
      await this.getTables()
      this.loadMessagesFromLocalStorage()

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
  watch: {
    // Theo dõi route thay đổi để cập nhật component đang active
    $route() {
      this.updateCurrentComponent()
    },
  },
  methods: {
    toggleSidebar() {
      // Xử lý toggle sidebar
      const sidebar = document.querySelector('#sidebar')
      if (sidebar) {
        sidebar.classList.toggle('active')
      }
      const mainContent = document.querySelector('#main')
      if (mainContent) {
        mainContent.classList.toggle('expanded')
      }
    },
    updateCurrentComponent() {
      const routePath = this.$route.path
      if (routePath === '/service-staff') {
        this.currentRouteName = 'Dashboard'
      } else if (routePath.includes('dashboard')) {
        this.currentRouteName = 'Dashboard'
      } else if (routePath.includes('orders')) {
        this.currentRouteName = 'Orders'
      } else if (routePath.includes('kitchen-management')) {
        this.currentRouteName = 'KitchenManager'
      }
    },
    setCurrentComponent(componentName) {
      this.currentRouteName = componentName
    },
    logout() {
      // Xử lý đăng xuất
      localStorage.removeItem('token')
      this.$router.push('/login')
    },
    getUserInfo() {
      const token = localStorage.getItem('token')
      if (!token) {
        // Nếu không có token, chuyển về trang đăng nhập
        this.$router.push('/login')
        return
      }

      try {
        // Giải mã token để lấy thông tin cơ bản
        const decodedToken = jwtDecode(token)
        // Gọi API để lấy thông tin chi tiết của người dùng
        axios
          .get('http://localhost:8081/user/getInfo', {
            headers: {
              Authorization: `Bearer ${token}`,
            },
            params: {
              userName: decodedToken.sub,
            },
          })
          .then((response) => {
            // Cập nhật thông tin người dùng
            const userData = response.data.result
            this.userInfo = {
              name:
                userData.firstName === null
                  ? 'Admin'
                  : userData.firstName + ' ' + userData.lastName,
              role:
                userData.roles[0].name ||
                (Array.isArray(decodedToken.scope)
                  ? decodedToken.scope.join(', ')
                  : decodedToken.scope || 'Nhân viên'),
              avatar: userData.avatar || '/src/assets/images/faces/1.jpg',
            }
          })
          .catch((error) => {
            console.error('Lỗi khi lấy thông tin người dùng:', error)
            // Sử dụng thông tin cơ bản từ token
            this.userInfo = {
              name: decodedToken.sub || 'Người dùng',
              role:
                userData.roles[0].name ||
                (Array.isArray(decodedToken.scope)
                  ? decodedToken.scope.join(', ')
                  : decodedToken.scope || 'Nhân viên'),
              avatar: '/src/assets/images/faces/1.jpg',
            }
          })
      } catch (error) {
        console.error('Lỗi khi giải mã token:', error)
        this.$router.push('/login')
      }
    },
    formatTime(timestamp) {
      return new Date(timestamp).toLocaleString('vi-VN')
    },
    handleNewMessage(response) {
      console.log('Nhận message WebSocket:', response)

      if (response.result.type === 'NEW_ORDER') {
        this.fetchOrders()
        this.getTables()
      }

      const message = response.result
      if (!message) {
        console.warn('Message không có dữ liệu result:', response)
        return
      }

      console.log('Xử lý tin nhắn mới:', message)
      const newMessage = {
        id: Date.now(),
        tableId: message.tableId,
        content: message.message,
        type: message.type,
        timestamp: new Date().toISOString(),
      }
      this.messages.unshift(newMessage)
      this.saveMessagesToLocalStorage()
    },
    getStatusText(status) {
      return status === 'empty' ? 'Trống' : 'Đang sử dụng'
    },
    handleTableClick(table) {
      this.selectedTable = { ...table }
      this.showTableModal = true
    },
    introspectToken() {
      const token = localStorage.getItem('token')
      const formData = new FormData()
      formData.append('token', token)
      axios
        .post('http://localhost:8081/auth/introspect', formData)
        .then((response) => {
          if (!response.data.result.valid) {
            localStorage.removeItem('token')
            this.$router.push('/login')
          }
        })
        .catch((error) => {
          console.error('Lỗi khi introspect token:', error)
          this.$router.push('/login')
        })
    },
    async getTables() {
      try {
        const response = await axios.get('http://localhost:8081/table/getAll')
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
    async updateTableStatus(tableInfo) {
      try {
        const formData = new FormData()
        formData.append('id', tableInfo.id)
        formData.append('status', tableInfo.status === 'empty' ? 1 : 0)
        formData.append('name', tableInfo.name)
        formData.append('description', tableInfo.description)
        formData.append('capacity', tableInfo.capacity)
        formData.append('url', tableInfo.url)

        await axios.put(`http://localhost:8081/table`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        })
        await this.getTables()
        this.showTableModal = false
      } catch (error) {
        alert('Cập nhật trạng thái bàn thất bại!')
      }
    },
    saveMessagesToLocalStorage() {
      localStorage.setItem('staff_messages', JSON.stringify(this.messages))
    },
    loadMessagesFromLocalStorage() {
      const savedMessages = localStorage.getItem('staff_messages')
      if (savedMessages) {
        this.messages = JSON.parse(savedMessages)
      }
    },
    clearMessages() {
      this.messages = []
      this.saveMessagesToLocalStorage()
    },
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
      if (!order) return
      const orderCopy = { ...order }
      if (!orderCopy.items) orderCopy.items = []
      if (!orderCopy.totalAmount) orderCopy.totalAmount = 0
      this.paymentOrder = orderCopy
    },
    async updateOrder(orderId, updateData) {
      try {
        this.loading = true
        const currentOrder = this.orders.find((o) => o.id === orderId)
        if (!currentOrder) {
          throw new Error('Không tìm thấy đơn hàng')
        }

        const orderReq = {
          orderId: orderId,
          tableId: currentOrder.table?.id,
          orderItems: currentOrder.items,
          totalAmount: currentOrder.totalAmount,
          status: updateData.status || currentOrder.status,
          note: currentOrder.note,
          paymentMethod: updateData.paymentMethod || currentOrder.paymentMethod,
          isPaid: updateData.isPaid || currentOrder.isPaid,
          customerName: currentOrder.customerName,
          customerPhone: currentOrder.customerPhone,
        }

        await axios.put(`http://localhost:8081/order/updateOrder`, orderReq)

        const index = this.orders.findIndex((o) => o.id === orderId)
        if (index !== -1) {
          this.orders[index] = { ...this.orders[index], ...updateData }
        }

        if (this.selectedOrder && this.selectedOrder.id === orderId) {
          this.selectedOrder = { ...this.selectedOrder, ...updateData }
        }
        this.getTables()
      } catch (error) {
        this.error = 'Lỗi khi cập nhật đơn hàng'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    async processPayment(paymentData) {
      try {
        await this.updateOrder(paymentData.orderId, {
          status: 'COMPLETED',
          paymentMethod: paymentData.paymentMethod,
          isPaid: true,
        })

        this.paymentOrder = null
        this.fetchOrders()
      } catch (error) {
        this.error = 'Lỗi khi xử lý thanh toán'
        console.error(error)
      }
    },
    async updateOrderStatus(order) {
      try {
        await this.updateOrder(order.id, {
          status: order.status,
        })
      } catch (error) {
        this.error = 'Lỗi khi cập nhật trạng thái đơn hàng'
        console.error(error)
      }
    },
    async fetchOrders() {
      try {
        this.loading = true
        const response = await axios.get('http://localhost:8081/order/getAll')
        this.orders = response.data.result || []
      } catch (error) {
        console.error('Lỗi khi fetch orders:', error)
        this.error = 'Lỗi khi tải danh sách đơn hàng'
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
.admin-container {
  min-height: 100vh;
  background-color: #f4f6f9;
}

#main {
  margin-left: 300px;
  padding: 1.5rem;
  transition: margin-left 0.5s;
}

#main.expanded {
  margin-left: 0;
}

@media (max-width: 1199px) {
  #main {
    margin-left: 0;
  }
}
</style>