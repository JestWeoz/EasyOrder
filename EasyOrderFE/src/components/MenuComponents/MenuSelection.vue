<template>
  <div>
    <MenuItem
      v-for="(item, index) in items"
      :key="index"
      :item="item"
      :cart="cart"
      @add-item="$emit('add-item', item)"
      @update-quantity="
        (id, quantity) => {
          $emit('update-quantity', id, quantity)
        }
      "
    >
    </MenuItem>
  </div>
</template>
  
<script>
import MenuItem from './MenuItem.vue'

export default {
  name: 'MenuSection',
  components: {
    MenuItem,
  },
  props: {
    items: {
      type: Array,
      required: true,
    },
    cart: {
      type: Array,
      required: true,
      default: () => [],
    },
  },
  emits: ['add-item', 'update-quantity'],
}
</script>
  
<style scoped>
.category-title {
  font-weight: bold;
  margin: 20px 0 10px 0;
  font-size: 20px;
}
</style>