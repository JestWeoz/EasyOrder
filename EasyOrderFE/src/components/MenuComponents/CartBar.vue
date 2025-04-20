<template>
  <div v-if="cart.length > 0" class="cart-bar" @click="$emit('show-cart')">
    <div class="cart-bar-info">
      <div class="cart-info">Giỏ Hàng - {{ totalItems }} món</div>
      <div class="cart-total">{{ formatPrice(totalPrice) }}đ</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CartBar',
  props: {
    cart: {
      type: Array,
      required: true,
    },
  },
  computed: {
    totalItems() {
      return this.cart.reduce((total, item) => total + item.quantity, 0)
    },
    totalPrice() {
      return this.cart.reduce((total, item) => total + item.price * item.quantity, 0)
    },
  },
  methods: {
    formatPrice(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
    },
  },
  emits: ['show-cart'],
}
</script>

<style scoped>
.cart-bar {
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  background: #ffffff;
  padding: 12px 16px;
  display: flex;
  cursor: pointer;
  z-index: 999;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.cart-bar-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #1877f2;
  border-radius: 10px;
  padding: 12px 16px;
  width: 100%;
  color: white;
  transition: all 0.3s ease;
}

.cart-bar-info:hover {
  background: #166fe5;
  transform: translateY(-2px);
}

.cart-info {
  font-size: 16px;
  font-weight: bold;
}

.cart-total {
  font-weight: 700;
  font-size: 16px;
}
</style> 