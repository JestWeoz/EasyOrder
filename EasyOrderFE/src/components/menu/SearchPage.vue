<template>
  <div class="sidebar-overlay" v-if="isOpen" @click="close"></div>
  <div class="sidebar" :class="{ open: isOpen }">
    <div class="search-header">
      <h2 class="search-title">Tìm kiếm</h2>
      <button class="btn p-0 close-btn" @click="close">
        <i class="bi bi-x-lg"></i>
      </button>
    </div>

    <div class="search-container">
      <div class="search-input-wrapper">
        <i class="bi bi-search search-icon"></i>
        <input
          type="text"
          :value="keyword"
          class="search-input"
          placeholder="Tìm kiếm sản phẩm"
          @keyup.enter="updateKeywordAndSearch($event.target.value)"
        />
      </div>

      <div class="search-results">
        <div v-if="!keyword" class="no-results">
          <div class="search-icon-wrapper">
            <i class="bi bi-search"></i>
          </div>
          <p>Nhập từ khóa để tìm kiêm</p>
        </div>

        <div v-else-if="keyword && products.length === 0 && loading === false" class="no-results">
          <div class="search-icon-wrapper">
            <i class="bi bi-search"></i>
          </div>
          <p>Không tìm thấy sản phẩm phù hợp</p>
        </div>

        <div v-else class="product-list">
          <div v-for="product in products" :key="product.id" class="product-item">
            <img :src="product.images[0].url" :alt="product.name" class="product-image" />
            <div class="product-info">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-description">{{ product.description }}</p>
              <p class="product-price">{{ formatPrice(product.price) }} đ</p>
            </div>
            <button class="add-btn" @click="addToCart(product)">
              <i class="bi bi-plus-lg"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'SearchPage',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      keyword: '',
      products: [],
      loading: false,
    }
  },

  methods: {
    close() {
      this.$emit('update:isOpen', false)
    },
    formatPrice(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
    },
    updateKeywordAndSearch(value) {
      this.keyword = value
      this.getProducts()
    },
    async getProducts() {
      this.loading = true
      if (!this.keyword.trim()) {
        this.products = []
        this.loading = false
        return
      }

      try {
        const response = await axios.get('http://localhost:8081/menu/product', {
          params: {
            keyword: this.keyword,
          },
        })
        this.products = response.data.result || []
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu sản phẩm:', error)
        this.products = []
      } finally {
        this.loading = false
      }
    },
    addToCart(product) {
      this.$emit('add-to-cart', product)
    },
  },
  emits: ['update:isOpen', 'add-to-cart'],
}
</script>

<style scoped>
.sidebar-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 3000;
}

.sidebar {
  position: fixed;
  top: 0;
  right: -100%;
  width: 100%;
  height: 100%;
  background-color: white;
  z-index: 3001;
  transition: right 0.5s ease;
  display: flex;
  flex-direction: column;
}

.sidebar.open {
  right: 0;
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.search-title {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 600;
}

.close-btn {
  border: none;
  background: none;
}

.search-container {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-input-wrapper {
  position: relative;
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 12px 40px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
}

.no-results {
  text-align: center;
  padding: 40px 0;
  color: #666;
}

.search-icon-wrapper {
  margin-bottom: 16px;
  font-size: 2rem;
}

.search-results {
  flex: 1;
  overflow-y: auto;
}

.product-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-bottom: 16px;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid #eee;
}

.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  margin-right: 16px;
}

.product-info {
  flex: 1;
}

.product-name {
  margin: 0 0 4px;
  font-size: 1rem;
  font-weight: 600;
}

.product-description {
  margin: 0 0 4px;
  font-size: 0.9rem;
  color: #666;
}

.product-price {
  margin: 0;
  font-weight: 600;
  color: #000;
}

.add-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #1877f2;
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.add-btn:hover {
  background-color: #1666d8;
}
</style> 