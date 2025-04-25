<template>
  <div class="menu-item shadow-box">
    <img :src="item.images[0].url" :alt="item.name" class="menu-item-image" />
    <div class="menu-item-details">
      <div>
        <div class="menu-item-name">{{ item.name }}</div>
        <div v-if="item.description" class="menu-item-description">
          {{ item.description }}
        </div>
      </div>
      <div class="menu-item-price">{{ formattedPrice }} đ</div>
    </div>
    <div v-if="cartItem && cartItem.quantity > 0" class="quantity-controls">
      <button class="quantity-btn" @click="handleUpdateQuantity(cartItem.quantity - 1)">
        <i class="bi bi-dash"></i>
      </button>
      <span class="quantity">{{ cartItem.quantity }}</span>
      <button class="quantity-btn" @click="handleUpdateQuantity(cartItem.quantity + 1)">
        <i class="bi bi-plus"></i>
      </button>
    </div>
    <button v-else class="add-button" @click="$emit('add-item')">
      <i class="bi bi-plus"></i>
    </button>
  </div>
</template>

<script>
export default {
  name: 'MenuItem',
  props: {
    item: {
      type: Object,
      required: true,
    },
    cart: {
      type: Array,
      required: true,
      default: () => [],
    },
  },
  emits: ['add-item', 'update-quantity'],
  computed: {
    formattedPrice() {
      return this.item.price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
    },
    cartItem() {
      return this.cart.find((cartItem) => cartItem.id === this.item.id)
    },
  },
  methods: {
    handleUpdateQuantity(newQuantity) {
      this.$emit('update-quantity', this.item.id, newQuantity)
    },
  },
}
</script>

<style scoped>
.menu-item {
  display: flex;
  padding: 12px;
  border-radius: 12px;
  background-color: white;
  margin-bottom: 12px;
  position: relative;
  min-height: 116px;
}

.shadow-box {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.menu-item-image {
  width: 92px;
  height: 92px;
  object-fit: cover;
  border-radius: 8px;
}

.menu-item-details {
  padding: 0 15px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 92px;
}

.menu-item-name {
  font-weight: 600;
  font-size: 16px;
  color: #000;
  margin-bottom: 4px;
}

.menu-item-description {
  font-size: 13px;
  color: #7a7a7a;
}

.menu-item-price {
  font-size: 15px;
  color: #000;
  margin-top: auto;
  font-weight: 600;
}

.add-button {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #1877f2;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  font-size: 18px;
  position: absolute;
  bottom: 12px;
  right: 12px;
  transition: background-color 0.2s ease;
}

.add-button:hover {
  background-color: #155ed8;
}

.quantity-controls {
  position: absolute;
  bottom: 12px;
  right: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.quantity-btn {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border: none;
  background-color: #1877f2;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  cursor: pointer;
  padding: 0;
  transition: background-color 0.2s ease;
  line-height: 1;
}

.quantity-btn i {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.add-button i {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.quantity {
  font-weight: 600;
  min-width: 24px;
  text-align: center;
}
</style>
