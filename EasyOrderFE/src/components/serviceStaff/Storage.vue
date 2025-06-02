<template>
  <div class="page-heading">
    <h3>Quản lý trạng thái món ăn</h3>
  </div>
  <div class="page-content">
    <section class="row">
      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <div class="d-flex justify-content-between align-items-center">
              <h4 class="card-title">Danh sách món ăn</h4>
              <div class="search-box">
                <div class="input-group">
                  <input
                    type="text"
                    class="form-control"
                    placeholder="Tìm kiếm món ăn..."
                    v-model="searchQuery"
                    @input="handleSearch"
                  />
                  <button class="btn btn-outline-secondary" type="button" @click="clearSearch">
                    <i class="bi bi-x-lg"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="card-body">
            <div v-if="loading" class="text-center">
              <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
            </div>
            <div v-else-if="error" class="alert alert-danger">
              {{ error }}
            </div>
            <div v-else>
              <div v-if="filteredCategories.length === 0" class="text-center py-4">
                <p class="text-muted">Không tìm thấy món ăn nào</p>
              </div>
              <div v-else v-for="category in filteredCategories" :key="category.id" class="mb-4">
                <h5 class="mb-3">{{ category.name }}</h5>
                <div class="table-responsive">
                  <table class="table table-hover">
                    <thead>
                      <tr>
                        <th>Tên món</th>
                        <th>Giá</th>
                        <th>Mô tả</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="item in category.products" :key="item.id">
                        <td>{{ item.name }}</td>
                        <td>{{ formatPrice(item.price) }}đ</td>
                        <td>{{ item.description }}</td>
                        <td>
                          <span :class="getStatusClass(item.status)">{{
                            getStatusText(item.status)
                          }}</span>
                        </td>
                        <td>
                          <button
                            class="btn btn-sm btn-warning"
                            @click="openEditModal(item, category)"
                          >
                            <i class="bi bi-pencil"></i>
                          </button>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>

  <!-- Modal Sửa trạng thái món ăn -->
  <div class="modal fade" id="productModal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Cập nhật trạng thái món ăn</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="mb-3">
              <label class="form-label">Tên món</label>
              <input type="text" class="form-control" v-model="formData.name" disabled />
            </div>
            <div class="mb-3">
              <label class="form-label">Trạng thái</label>
              <select class="form-select" v-model="formData.status" required>
                <option value="1">Còn hàng</option>
                <option value="0">Hết hàng</option>
              </select>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
          <button type="button" class="btn btn-primary" @click="handleSubmit">Lưu</button>
        </div>
      </div>
    </div>
  </div>

  <div v-if="notify.show" :class="['custom-toast', notify.type]">
    {{ notify.message }}
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { Modal } from 'bootstrap'
import axios from 'axios'

export default {
  name: 'Storage',
  props: {
    messages: {
      type: Array,
      default: () => [],
    },
    tables: {
      type: Array,
      default: () => [],
    },
    showTableModal: {
      type: Boolean,
      default: false,
    },
    selectedTable: {
      type: Object,
      default: null,
    },
    orders: {
      type: Array,
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
    error: {
      type: String,
      default: '',
    },
    formatDateTime: {
      type: Function,
      default: null,
    },
    formatCurrency: {
      type: Function,
      default: null,
    },
    getStatusLabel: {
      type: Function,
      default: null,
    },
    getPaymentMethodLabel: {
      type: Function,
      default: null,
    },
    formatTime: {
      type: Function,
      default: null,
    },
    formatOrderStatus: {
      type: Function,
      default: null,
    },
  },
  emits: [
    'tableClick',
    'updateTableStatus',
    'clearMessages',
    'showPaymentModal',
    'updateOrderStatus',
    'processPayment',
  ],
  setup() {
    const categories = ref([])
    const loading = ref(false)
    const error = ref(null)
    const isEditing = ref(false)
    const searchQuery = ref('')
    const formData = ref({
      id: null,
      name: '',
      status: '0',
    })
    let productModal = null
    const notify = ref({
      show: false,
      message: '',
      type: 'success',
    })

    onMounted(() => {
      // Khởi tạo modal
      productModal = new Modal(document.getElementById('productModal'))
      // Load dữ liệu menu
      fetchMenuItems()
    })

    const fetchMenuItems = async () => {
      try {
        loading.value = true
        const response = await axios.get('http://localhost:8081/menu')
        console.log('response', response.data)
        if (response.data.code === 1000) {
          categories.value = response.data.result.categories.map((category) => ({
            ...category,
            products: category.products.map((product) => ({
              ...product,
            })),
          }))
        } else {
          error.value = 'Không thể tải danh sách món ăn. Vui lòng thử lại sau.'
        }
      } catch (error) {
        error.value = 'Không thể tải danh sách món ăn. Vui lòng thử lại sau.'
        console.error(error)
      } finally {
        loading.value = false
      }
    }

    const filteredCategories = computed(() => {
      if (!searchQuery.value) return categories.value

      return categories.value
        .map((category) => ({
          ...category,
          products: category.products.filter((product) =>
            product.name.toLowerCase().includes(searchQuery.value.toLowerCase())
          ),
        }))
        .filter((category) => category.products.length > 0)
    })

    const handleSearch = () => {
      // Có thể thêm debounce ở đây nếu cần
    }

    const clearSearch = () => {
      searchQuery.value = ''
    }

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN').format(price)
    }

    const openEditModal = (item, category) => {
      isEditing.value = true
      formData.value = {
        id: item.id,
        name: item.name,
        status: item.status,
        price: item.price,
        description: item.description,
        categoryId: category.id,
      }
      productModal.show()
      setTimeout(() => {
        const firstInput = document.querySelector('#productModal input, #productModal select')
        if (firstInput) firstInput.focus()
      }, 300)
    }

    const showNotify = (message, type = 'success') => {
      notify.value = { show: true, message, type }
      setTimeout(() => {
        notify.value.show = false
      }, 2000)
    }

    const handleSubmit = async () => {
      try {
        // Kiểm tra dữ liệu trước khi gửi
        if (!formData.value.id || !formData.value.categoryId) {
          showNotify('Thiếu thông tin cần thiết. Vui lòng thử lại.', 'error')
          return
        }

        const formDataToSend = new FormData()
        formDataToSend.append('id', formData.value.id)
        formDataToSend.append('status', formData.value.status)
        formDataToSend.append('name', formData.value.name)
        formDataToSend.append('price', formData.value.price)
        formDataToSend.append('description', formData.value.description)
        formDataToSend.append('categoryId', formData.value.categoryId.toString())

        console.log('formDataToSend', {
          id: formDataToSend.get('id'),
          status: formDataToSend.get('status'),
          name: formDataToSend.get('name'),
          price: formDataToSend.get('price'),
          description: formDataToSend.get('description'),
          categoryId: formDataToSend.get('categoryId'),
        })

        const response = await axios.put('http://localhost:8081/menu/product', formDataToSend, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        })
        console.log('response', response.data)
        if (response.data.code === 1000 && response.data.result === 'Success') {
          await fetchMenuItems()
          productModal.hide()
          showNotify('Cập nhật trạng thái thành công!', 'success')
        } else {
          showNotify(response.data.result || 'Cập nhật thất bại', 'error')
          throw new Error(response.data.result || 'Cập nhật thất bại')
        }
      } catch (error) {
        console.error('Lỗi khi cập nhật trạng thái:', error)
        showNotify(
          error.response?.data?.message || 'Không thể cập nhật trạng thái. Vui lòng thử lại sau.',
          'error'
        )
      }
    }

    const getStatusClass = (status) => {
      switch (Number(status)) {
        case 1:
          return 'badge bg-success'
        case 0:
          return 'badge bg-danger'
        default:
          return 'badge bg-secondary'
      }
    }

    const getStatusText = (status) => {
      switch (Number(status)) {
        case 1:
          return 'Còn hàng'
        case 0:
          return 'Hết hàng'
        default:
          return 'Không xác định'
      }
    }

    return {
      categories,
      filteredCategories,
      loading,
      error,
      isEditing,
      formData,
      searchQuery,
      openEditModal,
      handleSubmit,
      handleSearch,
      clearSearch,
      getStatusClass,
      getStatusText,
      formatPrice,
      notify,
      showNotify,
    }
  },
}
</script>

<style scoped>
.badge {
  padding: 0.5em 0.8em;
}

.img-thumbnail {
  padding: 0.25rem;
  background-color: #fff;
  border: 1px solid #dee2e6;
  border-radius: 0.25rem;
}

.search-box {
  width: 300px;
}

.input-group .btn-outline-secondary {
  border-color: #ced4da;
}

.input-group .btn-outline-secondary:hover {
  background-color: #f8f9fa;
  color: #212529;
}

.custom-toast {
  position: fixed;
  top: 32px;
  right: 32px;
  min-width: 240px;
  background: #fff;
  color: #333;
  border-radius: 8px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.15);
  padding: 16px 24px;
  z-index: 9999;
  text-align: left;
  font-size: 16px;
  font-weight: 500;
  opacity: 1;
  transition: opacity 0.5s;
  border-left: 6px solid #28a745;
}
.custom-toast.success {
  border-left-color: #28a745;
}
.custom-toast.error {
  border-left-color: #dc3545;
  color: #dc3545;
}
</style> 