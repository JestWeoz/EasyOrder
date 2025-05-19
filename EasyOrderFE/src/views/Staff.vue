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
          @table-click="handleTableClick"
          @update-table-status="updateTableStatus"
          @clear-messages="clearMessages"
          @update:showTableModal="showTableModal = $event"
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
    }
  },
  computed: {
    pageTitle() {
      return ` ${this.userInfo.name || 'Nhân viên'}`
    },
  },
  async created() {
    // Cập nhật component hiện tại dựa trên route
    this.updateCurrentComponent()

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
      const message = response.result
      console.log('Nhận tin nhắn mới:', message)
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