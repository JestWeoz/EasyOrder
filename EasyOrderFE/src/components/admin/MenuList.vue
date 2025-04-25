<template>
  <div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
      <div>Danh sách món ăn</div>
      <div>
        <button class="btn btn-primary me-2" @click="$emit('open-add-modal')">
          <i class="bi bi-plus-circle"></i> Thêm món
        </button>
        <button class="btn btn-success" @click="$emit('open-add-category-modal')">
          <i class="bi bi-plus-circle"></i> Thêm danh mục
        </button>
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
        <div class="mb-4">
          <label for="categoryFilter" class="form-label">Chọn danh mục:</label>
          <select class="form-select" id="categoryFilter" v-model="selectedCategoryId">
            <option value="all">Tất cả danh mục</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        <table class="table table-striped">
          <thead>
            <tr>
              <th>STT</th>
              <th>Hình ảnh</th>
              <th>Tên món</th>
              <th>Giá</th>
              <th>Mô tả</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in filteredProducts" :key="item.id">
              <td>{{ index + 1 }}</td>
              <td>
                <img
                  v-if="item.images && item.images.length > 0"
                  :src="item.images[0].url"
                  alt="Hình ảnh món ăn"
                  class="menu-item-image"
                />
              </td>
              <td>{{ item.name }}</td>
              <td>{{ formatPrice(item.price) }}</td>
              <td>{{ item.description }}</td>
              <td>
                <button class="btn btn-sm btn-primary me-2" @click="$emit('edit-item', item)">
                  Sửa
                </button>
                <button class="btn btn-sm btn-danger" @click="$emit('delete-item', item.id)">
                  Xóa
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MenuList',
  props: {
    categories: {
      type: Array,
      required: true,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    error: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      selectedCategoryId: 'all',
    }
  },
  computed: {
    filteredProducts() {
      if (this.selectedCategoryId === 'all') {
        return this.categories.reduce((acc, category) => {
          return acc.concat(category.products || [])
        }, [])
      }
      const selectedCategory = this.categories.find((c) => c.id === this.selectedCategoryId)
      return selectedCategory ? selectedCategory.products || [] : []
    },
  },
  methods: {
    formatPrice(price) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
      }).format(price)
    },
  },
}
</script>

<style scoped>
.table {
  margin-bottom: 0;
}

.menu-item-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.card {
  margin-bottom: 2rem;
}

.card-header {
  margin-bottom: 1rem;
}

.btn {
  margin: 0.25rem;
}
</style> 