<template>
  <div class="cart-overlay" v-if="isVisible && cart.length > 0" @click="$emit('close')"></div>
  <div class="cart-slideup" :class="{ open: isVisible && cart.length > 0 }">
    <div class="cart-header">
      <h6 class="mb-0">Giỏ hàng của bạn</h6>
      <button class="btn p-0" @click="$emit('close')">
        <i class="bi bi-x-lg"></i>
      </button>
    </div>
    <div class="cart-items">
      <div v-for="item in cart" :key="item.name" class="cart-item">
        <div class="item-info">
          <img :src="item.images[0].url" :alt="item.name" class="item-image" />
          <div class="item-details">
            <div class="item-name">{{ item.name }}</div>
            <div class="item-price">{{ formatPrice(item.price) }}đ</div>
          </div>
        </div>
        <div class="quantity-controls">
          <button
            class="quantity-btn"
            @click="$emit('update-quantity', item.id, item.quantity - 1)"
          >
            <i class="bi bi-dash"></i>
          </button>
          <span class="quantity">{{ item.quantity }}</span>
          <button
            class="quantity-btn"
            @click="$emit('update-quantity', item.id, item.quantity + 1)"
          >
            <i class="bi bi-plus"></i>
          </button>
        </div>
      </div>
    </div>
    <div class="cart-note">
      <textarea
        v-model="note"
        placeholder="Nhập ghi chú cho đơn hàng của bạn..."
        class="note-input"
      ></textarea>
    </div>
    <div class="cart-footer">
      <div class="total">
        <span>Tổng cộng</span>
        <span class="total-price">{{ formatPrice(totalPrice) }}đ</span>
      </div>
      <button class="checkout-btn" @click="$emit('checkout', note)">Đặt món</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CartSlideup',
  props: {
    cart: {
      type: Array,
      required: true,
    },
    isVisible: {
      type: Boolean,
      required: true,
    },
  },
  computed: {
    totalPrice() {
      return this.cart.reduce((total, item) => total + item.price * item.quantity, 0)
    },
  },
  methods: {
    formatPrice(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
    },
  },
  emits: ['close', 'update-quantity', 'checkout'],
  data() {
    return {
      note: '',
    }
  },
}
</script>

<style scoped>
.cart-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.cart-slideup {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  right: 0;
  background: white;
  border-radius: 10px;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  transition: transform 0.6s ease;
  transform: translateY(100%);
}

.cart-slideup.open {
  transform: translateY(0);
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.cart-items {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.item-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.item-image {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  object-fit: cover;
}

.item-details {
  display: flex;
  flex-direction: column;
}

.item-name {
  font-weight: 600;
  font-size: 14px;
}

.item-price {
  color: #000000;
  font-weight: 600;
  font-size: 13px;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.quantity-btn {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  border: 1px solid #1877f2;
  background: #1877f2;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.quantity-btn i {
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

.cart-footer {
  padding: 16px;
  border-top: 1px solid #eee;
}

.total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  font-weight: 600;
}

.total-price {
  color: #1877f2;
  font-size: 18px;
}

.checkout-btn {
  width: 100%;
  padding: 12px;
  background: #1877f2;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
}

.cart-note {
  padding: 16px;
  border-top: 1px solid #eee;
}

.note-input {
  width: 100%;
  min-height: 80px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  resize: none;
  font-size: 14px;
}

.note-input:focus {
  outline: none;
  border-color: #1877f2;
}

.note-input::placeholder {
  color: #999;
}

/* Xóa các animation cũ */
.slide-up-enter-active,
.slide-up-leave-active,
.slide-up-enter-from,
.slide-up-leave-to {
  display: none;
}
</style> 