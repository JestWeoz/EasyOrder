<template>
  <div class="container-fluid p-0">
    <Header title="Bàn 37" @call-staff="showStaffModal" @request-checkout="requestCheckout" />
    <ActionButtons />
    <CategoryTabs :categories="tabs" :activeTab="activeTab" @update:tab="activeTab = $event" />

    <div class="container-fluid p-2">
      <div v-for="category in categories" :key="category.id">
        <MenuSection :items="category.products" @add-item="addItem" />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import Header from '@/components/MenuComponents/Header.vue'
import ActionButtons from '@/components/MenuComponents/ActionButtons.vue'
import CategoryTabs from '@/components/MenuComponents/CategoryTabs.vue'
import MenuSection from '@/components/MenuComponents/MenuSelection.vue'
import { sendMessage } from '@/utils/websocket'

export default {
  name: 'App',
  components: {
    Header,
    ActionButtons,
    CategoryTabs,
    MenuSection,
  },
  setup() {
    const activeTab = ref('all')
    const cart = ref([])
    const categories = ref([])
    const tabs = ref([{ id: 'all', name: 'Tất cả' }])

    const filteredProducts = computed(() => {
      if (activeTab.value === 'all') {
        return categories.value
      }
      return categories.value.filter((cat) => cat.id === activeTab.value)
    })

    const getMenu = async () => {
      try {
        const response = await axios.get('http://localhost:8081/menu')
        console.log(response.data)
        categories.value = response.data.result.categories
        tabs.value = [{ id: 'all', name: 'Tất cả' }, ...response.data.result.categories]
        console.log(tabs.value)
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu menu:', error)
      }
    }

    onMounted(async () => {
      await getMenu()
    })

    const addItem = (item) => {
      const existingItem = cart.value.find((cartItem) => cartItem.name === item.name)

      if (existingItem) {
        existingItem.quantity += 1
      } else {
        cart.value.push({
          ...item,
          quantity: 1,
        })
      }

      console.log('Item added to cart:', item.name)
      console.log('Current cart:', cart.value)
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

    return {
      activeTab,
      addItem,
      cart,
      categories: filteredProducts,
      tabs,
      getMenu,
      showStaffModal,
      requestCheckout,
    }
  },
}
</script>

