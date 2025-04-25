<template>
  <div class="page-wrapper">
    <div class="content-wrapper">
      <div class="container-fluid">
        <Header
          title="Bàn 37"
          @call-staff="showStaffModal"
          @request-checkout="requestCheckout"
          @add-to-cart="addItem"
          v-model:isSidebarOpen="isSidebarOpen"
          v-model:isSearchPageOpen="isSearchPageOpen"
        />
        <ActionButtons />
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
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import Header from '@/components/menu/Header.vue'
import ActionButtons from '@/components/menu/ActionButtons.vue'
import CategoryTabs from '@/components/menu/CategoryTabs.vue'
import MenuSection from '@/components/menu/MenuSelection.vue'
import CartSlideup from '@/components/menu/CartSlideup.vue'
import CartBar from '@/components/menu/CartBar.vue'
import { sendMessage } from '@/utils/websocket'

export default {
  name: 'App',
  components: {
    Header,
    ActionButtons,
    CategoryTabs,
    MenuSection,
    CartSlideup,
    CartBar,
  },
  setup() {
    const activeTab = ref('all')
    const cart = ref([])
    const categories = ref([])
    const tabs = ref([{ id: 'all', name: 'Tất cả' }])
    const isSidebarOpen = ref(false)
    const isCartVisible = ref(false)
    const isSearchPageOpen = ref(false)

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

    onMounted(async () => {
      await getMenu()
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

    const checkout = () => {
      // Xử lý đặt món tại đây
      console.log('Đặt món:', cart.value)
    }

    const showStaffModal = () => {
      const staffModalRef = document.querySelector('#staffModal')
      if (staffModalRef) {
        const modal = new bootstrap.Modal(staffModalRef)
        modal.show()
      }
    }

    const requestCheckout = () => {
      const message = {
        tableId: '37',
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
      isCartVisible,
      updateQuantity,
      checkout,
      filteredCategories,
      allProducts,
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
</style>

