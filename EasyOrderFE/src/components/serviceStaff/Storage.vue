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
                          <span :class="getStatusClass(item.status)">{{ item.status }}</span>
                        </td>
                        <td>
                          <button class="btn btn-sm btn-warning" @click="openEditModal(item)">
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
                <option value="Còn hàng">Còn hàng</option>
                <option value="Hết hàng">Hết hàng</option>
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
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { Modal } from 'bootstrap'
import axios from 'axios'

export default {
  name: 'Storage',
  setup() {
    const categories = ref([])
    const loading = ref(false)
    const error = ref(null)
    const isEditing = ref(false)
    const searchQuery = ref('')
    const formData = ref({
      id: null,
      name: '',
      status: 'Còn hàng',
    })
    let productModal = null

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
        if (response.data.code === 1000) {
          // Thêm trạng thái mặc định cho các món ăn nếu chưa có
          categories.value = response.data.result.categories.map((category) => ({
            ...category,
            products: category.products.map((product) => ({
              ...product,
              status: product.status || 'Còn hàng',
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

    const openEditModal = (item) => {
      isEditing.value = true
      formData.value = {
        id: item.id,
        name: item.name,
        status: item.status || 'Còn hàng',
      }
      productModal.show()
    }

    const handleSubmit = async () => {
      try {
        // Tìm và cập nhật trạng thái của món ăn
        for (const category of categories.value) {
          const productIndex = category.products.findIndex((p) => p.id === formData.value.id)
          if (productIndex !== -1) {
            category.products[productIndex].status = formData.value.status
            break
          }
        }

        // TODO: Gọi API để cập nhật trạng thái trên server
        // const response = await axios.put(`http://localhost:8081/menu/product/status`, {
        //   id: formData.value.id,
        //   status: formData.value.status
        // })

        productModal.hide()
      } catch (error) {
        console.error('Lỗi khi cập nhật trạng thái:', error)
        alert('Không thể cập nhật trạng thái. Vui lòng thử lại sau.')
      }
    }

    const getStatusClass = (status) => {
      switch (status) {
        case 'Còn hàng':
          return 'badge bg-success'
        case 'Hết hàng':
          return 'badge bg-danger'
        default:
          return 'badge bg-secondary'
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
      formatPrice,
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
</style> 