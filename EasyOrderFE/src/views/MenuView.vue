<template>
  <div class="page-wrapper">
    <div class="content-wrapper">
      <div class="container-fluid">
        <Header
          :title="`${tableInfo.name || tableId}`"
          :cart="cart"
          :tableId="tableId"
          @call-staff="showStaffModal(tableId)"
          @add-to-cart="addItem"
          @get-order="getOrder"
          v-model:isSidebarOpen="isSidebarOpen"
          v-model:isSearchPageOpen="isSearchPageOpen"
          v-model:isOrderPageOpen="isOrderPageOpen"
        />
        <ActionButtons :tableId="tableId" :tableInfo="tableInfo" />
        <CategoryTabs
          :categories="tabs"
          :activeTab="activeTab"
          :isSidebarOpen="isSidebarOpen"
          @update:tab="activeTab = $event"
        />

        <div class="container-fluid p-2">
          <div v-for="category in filteredCategories" :key="category.id">
            <MenuSection
              :items="category.products"
              :cart="cart"
              @add-item="addItem"
              @update-quantity="(itemId, newQuantity) => updateQuantity(itemId, newQuantity)"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="cart-wrapper">
      <CartBar :cart="cart" @show-cart="isCartVisible = true" />
    </div>
  </div>
  <CartSlideup
    :cart="cart"
    :isVisible="isCartVisible"
    @close="isCartVisible = false"
    @update-quantity="updateQuantity"
    @checkout="checkout"
  />
  <OrderPage v-model:isOpen="isOrderPageOpen" :tableId="tableId" />
  <StaffCallModal ref="staffModalRef" />
  <button v-show="showScrollTop" @click="scrollToTop" class="scroll-top-btn">
    <i class="bi bi-arrow-up-short"></i>
  </button>
  <footer class="footer-info">
    <div class="footer-content">
      <div class="footer-title">Easy Order</div>
      <div class="footer-address">11 ngõ 7, Yên Phúc, Hà Đông, Hà Nội</div>
      <div class="footer-phone"><b>Phone number:</b> 0961891998</div>
      <div class="footer-email">
        <b>Email:</b> <a href="mailto:kebincoffeeinbed@gmail.com">Tuannguyenquoc057@gmail.com</a>
      </div>
      <a href="https://facebook.com" target="_blank" class="footer-facebook">
        <i class="bi bi-facebook"></i>
      </a>
    </div>
  </footer>
</template>

<script>
import { ref, onMounted, computed, onUnmounted, watch } from 'vue'
import axios from 'axios'
import Header from '@/components/menu/Header.vue'
import ActionButtons from '@/components/menu/ActionButtons.vue'
import CategoryTabs from '@/components/menu/CategoryTabs.vue'
import MenuSection from '@/components/menu/MenuSelection.vue'
import CartSlideup from '@/components/menu/CartSlideup.vue'
import CartBar from '@/components/menu/CartBar.vue'
import OrderPage from '@/components/menu/OrderPage.vue'
import StaffCallModal from '@/components/menu/StaffCallModal.vue'
import { sendMessage } from '@/utils/websocket'
import { useRoute } from 'vue-router'

export default {
  name: 'App',
  components: {
    Header,
    ActionButtons,
    CategoryTabs,
    MenuSection,
    CartSlideup,
    CartBar,
    OrderPage,
    StaffCallModal,
  },
  setup() {
    const activeTab = ref('all')
    const cart = ref([])
    const categories = ref([])
    const tabs = ref([{ id: 'all', name: 'Tất cả' }])
    const isSidebarOpen = ref(false)
    const isCartVisible = ref(false)
    const isSearchPageOpen = ref(false)
    const isOrderPageOpen = ref(false)
    const showScrollTop = ref(false)
    const order = ref([])
    const route = useRoute()
    const tableId = Number(route.query.idTable)
    const tableInfo = ref({})
    const staffModalRef = ref(null)

    const allProducts = computed(() => {
      return categories.value.reduce((acc, category) => {
        return acc.concat(category.products || [])
      }, [])
    })

    const getMenu = async () => {
      try {
        const response = await axios.get('http://localhost:8081/menu')
        categories.value = response.data.result.categories
        tabs.value = [{ id: 'all', name: 'Tất cả' }, ...response.data.result.categories]
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu menu:', error)
      }
    }

    const getTable = async () => {
      try {
        const response = await axios.get('http://localhost:8081/table', {
          params: {
            tableId: tableId,
          },
        })
        tableInfo.value = response.data.result
        console.log('Thông tin bàn:', tableInfo.value)
      } catch (error) {
        console.error('Lỗi khi lấy thông tin bàn:', error)
      }
    }

    onMounted(async () => {
      await getTable()
      await getMenu()
      window.addEventListener('scroll', handleScroll)
    })

    watch(isOrderPageOpen, (newValue) => {
      if (newValue === true) {
        getOrder()
      }
    })

    onUnmounted(() => {
      window.removeEventListener('scroll', handleScroll)
    })

    const addItem = (item) => {
      const existingItem = cart.value.find((cartItem) => cartItem.id === item.id)

      if (existingItem) {
        existingItem.quantity += 1
      } else {
        cart.value.push({
          ...item,
          quantity: 1,
        })
      }
    }

    const updateQuantity = (itemId, newQuantity) => {
      console.log(cart.value)
      const existingItem = cart.value.find((cartItem) => cartItem.id === itemId)

      if (existingItem) {
        if (newQuantity <= 0) {
          cart.value = cart.value.filter((cartItem) => cartItem.id !== itemId)
          if (cart.value.length === 0) {
            isCartVisible.value = false
          }
        } else {
          existingItem.quantity = newQuantity
        }
      }
    }

    const checkout = async (note) => {
      const orderItems = cart.value.map((item) => ({
        productId: item.id,
        quantity: item.quantity,
      }))

      const orderRequest = {
        tableId: tableId,
        orderItems: orderItems,
        totalAmount: cart.value.reduce((total, item) => total + item.price * item.quantity, 0),
        status: 'PENDING',
        note: note || '',
        paymentMethod: 'CASH',
        isPaid: false,
        customerName: '',
        customerPhone: '',
      }

      try {
        const response = await axios.post('http://localhost:8081/order', orderRequest)
        console.log(response)
        cart.value = []
        isCartVisible.value = false
      } catch (error) {
        console.error('Lỗi khi đặt món:', error)
        alert('Có lỗi xảy ra khi đặt món. Vui lòng thử lại sau.')
      }
    }

    const showStaffModal = (tableId) => {
      if (staffModalRef.value) {
        staffModalRef.value.tableId = tableId
        staffModalRef.value.tableInfo = tableInfo.value
        staffModalRef.value.showModal()
      }
    }

    const requestCheckout = () => {
      const message = {
        tableId: tableId,
        tableName: tableInfo.value.name,
        type: 'CHECKOUT_REQUEST',
      }

      if (sendMessage('/app/checkout', message)) {
        alert('Đã gửi yêu cầu thanh toán')
      } else {
        alert('Không thể gửi yêu cầu thanh toán. Vui lòng thử lại sau.')
      }
    }

    const filteredCategories = computed(() => {
      if (activeTab.value === 'all') {
        return categories.value
      }
      return categories.value.filter((category) => category.id === activeTab.value)
    })

    const handleScroll = () => {
      showScrollTop.value = window.scrollY > 100
    }

    const scrollToTop = () => {
      window.scrollTo({
        top: 0,
        behavior: 'smooth',
      })
    }

    const getOrder = async () => {
      try {
        const response = await axios.get('http://localhost:8081/order', {
          params: {
            tableId: tableId,
          },
        })
        order.value = response.data.result
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu đơn hàng:', error)
      }
    }
    return {
      activeTab,
      addItem,
      cart,
      categories,
      tabs,
      showStaffModal,
      requestCheckout,
      isSidebarOpen,
      isSearchPageOpen,
      isOrderPageOpen,
      isCartVisible,
      updateQuantity,
      checkout,
      filteredCategories,
      allProducts,
      showScrollTop,
      scrollToTop,
      getOrder,
      tableId,
      tableInfo,
      staffModalRef,
    }
  },
}
</script>

<style>
.page-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content-wrapper {
  flex: 1;
}

.container-fluid {
  padding-top: 60px;
}

.cart-wrapper {
  width: 100%;
  background-color: white;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.scroll-top-btn {
  position: fixed;
  bottom: 80px;
  right: 20px;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  font-size: 24px;
  background-color: #ffffffff;
  color: #0585fdff;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 3px 20px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  z-index: 1000;
  padding: 0;
  line-height: 1;
}

.scroll-top-btn i {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.scroll-top-btn:hover {
  background-color: #bdcadaff;
  transform: translateY(-2px);
}

.footer-info {
  width: 100%;

  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.07);
  padding: 24px 0 16px 0;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.footer-content {
  text-align: center;
}
.footer-title {
  font-weight: bold;
  font-size: 22px;
  margin-bottom: 4px;
}
.footer-address {
  color: #888;
  font-size: 14px;
  margin-bottom: 8px;
}
.footer-phone,
.footer-email {
  font-size: 16px;
  margin-bottom: 4px;
}
.footer-email a {
  color: #0585fdff;
  text-decoration: none;
}
.footer-facebook {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 12px auto 0 auto;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #0585fdff;
  color: #fff;
  font-size: 28px;
  transition: background 0.2s;
}
.footer-facebook:hover {
  background: #0056b3;
}
.footer-facebook i {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>

