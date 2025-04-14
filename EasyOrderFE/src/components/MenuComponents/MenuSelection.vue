<template>
  <div>
    <MenuItem
      v-for="(item, index) in items"
      :key="index"
      :item="item"
      @add-item="$emit('add-item', item)"
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
  },
  emits: ['add-item'],
}
</script>
  
  <style scoped>
.category-title {
  font-weight: bold;
  margin: 20px 0 10px 0;
  font-size: 20px;
}
</style>