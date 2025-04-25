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
          },
        })

        if (response.data.code === 1000) {
          this.fetchMenuItems()
          this.$refs.addModal.hide()
          alert('Thêm món ăn thành công!')
        } else {
          this.error = response.data.message || 'Không thể thêm món ăn. Vui lòng thử lại sau.'
        }
      } catch (error) {
        console.error('Lỗi khi thêm món ăn:', error)
        if (error.response) {
          this.error = error.response.data.message || 'Lỗi server: ' + error.response.status
        } else {
          this.error = 'Không thể kết nối đến server. Vui lòng thử lại sau.'
        }
        alert(this.error)
      }
    },
    openAddCategoryModal() {
      this.$refs.addCategoryModal.show()
    },
    async addCategory(category) {
      try {
        const response = await axios.post(`http://localhost:8081/menu/category`, {
          name: category.name,
        })

        if (response.data.code === 1000) {
          this.fetchMenuItems()
          this.$refs.addCategoryModal.hide()
          alert('Thêm danh mục thành công!')
        } else {
          this.error = response.data.message || 'Không thể thêm danh mục. Vui lòng thử lại sau.'
        }
      } catch (error) {
        console.error('Lỗi khi thêm danh mục:', error)
        if (error.response) {
          this.error = error.response.data.message || 'Lỗi server: ' + error.response.status
        } else {
          this.error = 'Không thể kết nối đến server. Vui lòng thử lại sau.'
        }
        alert(this.error)
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
</style> 