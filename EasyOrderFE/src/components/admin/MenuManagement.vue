<template>
  <div class="page-heading">
    <div class="page-title">
      <div class="row">
        <div class="col-12 col-md-6 order-md-1 order-last">
          <h3>Quản lý menu</h3>
          <p class="text-subtitle text-muted">Quản lý menu trong cửa hàng.</p>
        </div>
      </div>
    </div>
    <section class="section">
      <MenuList
        :categories="categories"
        :loading="loading"
        :error="error"
        @open-add-modal="openAddModal"
        @open-add-category-modal="openAddCategoryModal"
        @edit-item="openEditModal"
        @delete-item="deleteItem"
      />
    </section>

    <EditProductModal
      ref="editModal"
      :categories="categories"
      :item="editingItem"
      @update-item="updateItem"
    />

    <AddProductModal ref="addModal" :categories="categories" @add-item="addItem" />

    <AddCategoryModal ref="addCategoryModal" @add-category="addCategory" />

    <!-- Loading Spinner -->
    <div v-if="isAddingItem || isAddingCategory" class="loading-overlay">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <!-- Success Modal -->
    <div v-show="showSuccessModal" class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Thông báo</h5>
          <button type="button" class="btn-close" @click="showSuccessModal = false"></button>
        </div>
        <div class="modal-body">
          {{ successMessage }}
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" @click="showSuccessModal = false">
            Đóng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import MenuList from './MenuList.vue'
import EditProductModal from './EditProductModal.vue'
import AddProductModal from './AddProductModal.vue'
import AddCategoryModal from './AddCategoryModal.vue'

export default {
  name: 'MenuManagement',
  components: {
    MenuList,
    EditProductModal,
    AddProductModal,
    AddCategoryModal,
  },
  data() {
    return {
      categories: [],
      loading: true,
      error: null,
      isAddingItem: false,
      isAddingCategory: false,
      showSuccessModal: false,
      successMessage: '',
      editingItem: {
        id: null,
        name: '',
        price: 0,
        description: '',
        categoryId: null,
        images: [],
      },
    }
  },
  methods: {
    async fetchMenuItems() {
      try {
        this.loading = true
        const response = await axios.get('http://localhost:8081/menu')
        if (response.data.code === 1000) {
          this.categories = response.data.result.categories
        } else {
          this.error = 'Không thể tải danh sách món ăn. Vui lòng thử lại sau.'
        }
      } catch (error) {
        this.error = 'Không thể tải danh sách món ăn. Vui lòng thử lại sau.'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    async deleteItem(itemId) {
      if (!confirm('Bạn có chắc chắn muốn xóa món ăn này?')) {
        return
      }

      try {
        const response = await axios.delete(`http://localhost:8081/menu/product`, {
          params: {
            id: itemId,
          },
        })
        if (response.data.code === 1000) {
          this.fetchMenuItems()
          alert('Xóa món ăn thành công!')
        } else {
          this.error = response.data.message || 'Không thể xóa món ăn. Vui lòng thử lại sau.'
        }
      } catch (error) {
        console.error('Lỗi khi xóa món ăn:', error)
        if (error.response) {
          this.error = error.response.data.message || 'Lỗi server: ' + error.response.status
        } else {
          this.error = 'Không thể kết nối đến server. Vui lòng thử lại sau.'
        }
        alert(this.error)
      }
    },
    openEditModal(item) {
      this.editingItem = item
      this.$refs.editModal.show()
    },
    async updateItem(item, image) {
      try {
        const formData = new FormData()
        formData.append('id', item.id)
        formData.append('name', item.name)
        formData.append('price', item.price)
        formData.append('description', item.description)
        formData.append('categoryId', item.categoryId)

        if (image) {
          for (let i = 0; i < image.length; i++) {
            formData.append('images', image[i])
          }
        }

        const response = await axios.put(`http://localhost:8081/menu/product`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        })

        if (response.data.code === 1000) {
          this.fetchMenuItems()
          this.$refs.editModal.hide()
          alert('Cập nhật món ăn thành công!')
        } else {
          this.error = response.data.message || 'Không thể cập nhật món ăn. Vui lòng thử lại sau.'
        }
      } catch (error) {
        console.error('Lỗi khi cập nhật món ăn:', error)
        if (error.response) {
          this.error = error.response.data.message || 'Lỗi server: ' + error.response.status
        } else {
          this.error = 'Không thể kết nối đến server. Vui lòng thử lại sau.'
        }
        alert(this.error)
      }
    },
    openAddModal() {
      this.$refs.addModal.show()
    },
    async addItem(item, image) {
      try {
        this.isAddingItem = true
        const formData = new FormData()
        formData.append('name', item.name)
        formData.append('price', item.price)
        formData.append('description', item.description)
        formData.append('categoryId', item.categoryId)

        if (image) {
          for (let i = 0; i < image.length; i++) {
            formData.append('images', image[i])
          }
        }

        const response = await axios.post(`http://localhost:8081/menu/product`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        })

        if (response.data.code === 1000) {
          this.fetchMenuItems()
          this.$refs.addModal.hide()
          this.successMessage = 'Thêm món ăn thành công!'
          this.showSuccessModal = true
        } else {
          this.error = response.data.message || 'Không thể thêm món ăn. Vui lòng thử lại sau.'
          alert(this.error)
        }
      } catch (error) {
        console.error('Lỗi khi thêm món ăn:', error)
        if (error.response) {
          this.error = error.response.data.message || 'Lỗi server: ' + error.response.status
        } else {
          this.error = 'Không thể kết nối đến server. Vui lòng thử lại sau.'
        }
        alert(this.error)
      } finally {
        this.isAddingItem = false
      }
    },
    openAddCategoryModal() {
      this.$refs.addCategoryModal.show()
    },
    async addCategory(category) {
      try {
        this.isAddingCategory = true
        const response = await axios.post(
          `http://localhost:8081/menu/category`,
          {
            name: category.name,
          },
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
          }
        )

        if (response.data.code === 1000) {
          this.fetchMenuItems()
          this.$refs.addCategoryModal.hide()
          this.successMessage = 'Thêm danh mục thành công!'
          this.showSuccessModal = true
        } else {
          this.error = response.data.message || 'Không thể thêm danh mục. Vui lòng thử lại sau.'
          alert(this.error)
        }
      } catch (error) {
        console.error('Lỗi khi thêm danh mục:', error)
        if (error.response) {
          this.error = error.response.data.message || 'Lỗi server: ' + error.response.status
        } else {
          this.error = 'Không thể kết nối đến server. Vui lòng thử lại sau.'
        }
        alert(this.error)
      } finally {
        this.isAddingCategory = false
      }
    },
  },
  created() {
    this.fetchMenuItems()
  },
}
</script>

<style scoped>
.page-heading {
  margin: 1.5rem;
}

.page-title {
  margin-bottom: 2rem;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.spinner-border {
  width: 3rem;
  height: 3rem;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.modal-header {
  padding: 1rem;
  border-bottom: 1px solid #dee2e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-body {
  padding: 1rem;
}

.modal-footer {
  padding: 1rem;
  border-top: 1px solid #dee2e6;
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0.5rem;
}

.btn-primary {
  background-color: #0d6efd;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-primary:hover {
  background-color: #0b5ed7;
}
</style> 